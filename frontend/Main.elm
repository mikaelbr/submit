module Main exposing (..)

import Html exposing (Html, div, map)
import Navigation
import Html.Attributes exposing (class)
import Nav.Nav exposing (hashParser, toHash)
import Model exposing (Model, Flags, initModel)
import Messages exposing (Msg(..))
import Nav.Model exposing (Page(..))
import View.Login
import View.Thanks
import View.Submissions
import View.Submission
import Model.Submission
import Submission exposing (updateSubmissionField)
import Subscriptions
import Nav.Requests exposing (getSubmissions, getSubmission, getLoginToken, createSubmission, loginFailed, deleteLoginToken, saveSubmission)
import Lazy
import Backend.Network exposing (RequestStatus(..))
import Task
import LocalStorage
import Time


init : Flags -> Navigation.Location -> ( Model, Cmd Msg )
init flags location =
    let
        page =
            hashParser location
    in
        ( initModel flags page, Navigation.newUrl <| toHash page )


saveToken : String -> Cmd Msg
saveToken token =
    Task.perform TokenSaved <|
        LocalStorage.set "login_token" token


removeLocalToken : Lazy.Lazy (Cmd Msg)
removeLocalToken =
    Lazy.lazy <| \() -> Task.perform SubmissionsTokenRemoved <| Task.succeed <| LocalStorage.remove "login_token"


update : Msg -> Model -> ( Model, Cmd Msg )
update msg ({ login, submissions, submission } as model) =
    case msg of
        UpdateUrl page ->
            updatePage page model

        LoginEmail email ->
            ( { model | login = { login | email = email } }, Cmd.none )

        LoginSubmitEmail ->
            ( { model | login = { login | loading = True } }, getLoginToken model.login.email )

        LoginSubmit (Err _) ->
            ( { model | login = { login | loading = False } }, Cmd.none )

        LoginSubmit (Ok _) ->
            ( model, Navigation.newUrl <| toHash Thanks )

        TokenSaved _ ->
            ( model, Navigation.newUrl <| toHash Submissions )

        SubmissionsCreateTalk ->
            ( model, Lazy.force createSubmission )

        SubmissionsGet (Err message) ->
            ( { model | submissions = { submissions | submissions = Error <| toString message } }, Lazy.force loginFailed )

        SubmissionsGet (Ok s) ->
            ( { model | submissions = { submissions | submissions = Complete s } }, Cmd.none )

        SubmissionsCreated (Err _) ->
            ( model, Lazy.force getSubmissions )

        SubmissionsCreated (Ok submission) ->
            ( model, Navigation.newUrl << toHash <| Nav.Model.Submission submission.id )

        SubmissionsLogout ->
            ( model, Lazy.force deleteLoginToken )

        SubmissionsLoggedOut (Err err) ->
            ( model, Lazy.force removeLocalToken )

        SubmissionsLoggedOut (Ok _) ->
            ( model, Lazy.force removeLocalToken )

        SubmissionsTokenRemoved _ ->
            ( model, Navigation.newUrl << toHash <| Nav.Model.Register )

        GetSubmission (Err error) ->
            ( { model | submission = { submission | submission = Error <| toString error } }, Lazy.force loginFailed )

        GetSubmission (Ok sub) ->
            ( { model | submission = { submission | submission = Complete sub } }, Cmd.none )

        SaveSubmission _ ->
            case submission.submission of
                Complete submission ->
                    ( model, saveSubmission submission )

                _ ->
                    ( model, Cmd.none )

        SavedSubmission (Err _) ->
            ( model, Cmd.none )

        SavedSubmission (Ok s) ->
            case submission.submission of
                Complete sub ->
                    ( { model
                        | submission =
                            { submission
                                | dirty = False
                                , submission = Complete { sub | speakers = s.speakers }
                            }
                      }
                    , Task.perform TimeUpdatedSubmission Time.now
                    )

                _ ->
                    ( model, Cmd.none )

        ToggleAutosaveSubmission ->
            ( { model | submission = { submission | autosave = not submission.autosave } }, Cmd.none )

        TimeUpdatedSubmission time ->
            ( { model | submission = { submission | lastSaved = Just time } }, Cmd.none )

        UpdateSubmission field ->
            let
                ( sub, cmd ) =
                    updateSubmissionField field submission
            in
                ( { model | submission = sub }, cmd )

        CommentSent (Err _) ->
            ( model, Cmd.none )

        CommentSent (Ok s) ->
            let
                submission =
                    model.submission
            in
                ( { model | submission = { submission | submission = Complete s, comment = "" } }, Cmd.none )


updatePage : Page -> Model -> ( Model, Cmd Msg )
updatePage page m =
    let
        leftModel =
            leftPage m.page m

        model =
            { leftModel | page = page }
    in
        case page of
            UseToken token ->
                ( model, saveToken token )

            Submissions ->
                let
                    submissions =
                        model.submissions
                in
                    ( { model
                        | submissions =
                            { submissions
                                | submissions = Loading
                            }
                      }
                    , Lazy.force getSubmissions
                    )

            Submission id ->
                let
                    submission =
                        model.submission
                in
                    ( { model
                        | submission =
                            { submission
                                | submission = Loading
                            }
                      }
                    , getSubmission id
                    )

            _ ->
                ( { model | page = page }, Cmd.none )


leftPage : Page -> Model -> Model
leftPage oldPage model =
    case oldPage of
        Submission _ ->
            { model | submission = Model.Submission.initModel }

        _ ->
            model


view : Model -> Html Msg
view model =
    div [ class "app" ] [ pageView model ]


pageView : Model -> Html Msg
pageView model =
    case model.page of
        Register ->
            View.Login.view model.login

        Thanks ->
            View.Thanks.view

        UseToken _ ->
            div [] []

        Submissions ->
            View.Submissions.view model.submissions

        Submission _ ->
            View.Submission.view model.submission


subscriptions : Model -> Sub Msg
subscriptions model =
    Subscriptions.subscriptions model.submission


main : Program Flags Model Msg
main =
    Navigation.programWithFlags (UpdateUrl << hashParser)
        { init = init
        , update = update
        , view = view
        , subscriptions = subscriptions
        }
