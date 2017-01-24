module Main exposing (..)

import Html exposing (Html, div, map)
import Navigation
import Html.Attributes exposing (class)
import Login.Update as Login
import Nav.Nav exposing (hashParser, toHash)
import Model exposing (Model)
import Message exposing (Msg(..))
import Nav.Model exposing (Page(..))
import Login.Login as Login
import Login.Update as LoginUpdate
import Thanks.Thanks as Thanks
import Usetoken.View
import Usetoken.Model
import Usetoken.Update exposing (saveToken)
import Submissions.View
import Submissions.Model
import Submissions.Update
import Submission.View
import Submission.Model
import Submission.Update
import Submission.Subscriptions
import Nav.Requests exposing (getSubmissions, getSubmission)
import Lazy


initModel : Page -> Model
initModel page =
    Model
        (Login.initModel)
        (Thanks.initModel)
        (Usetoken.Model.initModel "")
        (Submissions.Model.initModel)
        (Submission.Model.initModel)
        page


init : Navigation.Location -> ( Model, Cmd Msg )
init location =
    let
        page =
            hashParser location
    in
        ( initModel page, Navigation.newUrl <| toHash page )


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        UpdateUrl page ->
            updatePage page model

        LoginMsg loginMsg ->
            let
                ( newLogin, loginCmd ) =
                    LoginUpdate.update loginMsg model.login

                mappedCmd =
                    Cmd.map LoginMsg loginCmd
            in
                ( { model | login = newLogin }, mappedCmd )

        ThanksMsg thanksMsg ->
            let
                newThanks =
                    Thanks.update thanksMsg model.thanks
            in
                ( { model | thanks = newThanks }, Cmd.none )

        UsetokenMsg tokenMsg ->
            let
                ( newUsetoken, tokenCmd ) =
                    Usetoken.Update.update tokenMsg model.usetoken

                mappedCmd =
                    Cmd.map UsetokenMsg tokenCmd
            in
                ( { model | usetoken = newUsetoken }, mappedCmd )

        SubmissionsMsg submissionsMsg ->
            let
                ( newSubmissions, submissionsCmd ) =
                    Submissions.Update.update submissionsMsg model.submissions

                mappedCmd =
                    Cmd.map SubmissionsMsg submissionsCmd
            in
                ( { model | submissions = newSubmissions }, mappedCmd )

        SubmissionMsg submissionMsg ->
            let
                ( newSubmission, submissionCmd ) =
                    Submission.Update.update submissionMsg model.submission

                mappedCmd =
                    Cmd.map SubmissionMsg submissionCmd
            in
                ( { model | submission = newSubmission }, mappedCmd )


updatePage : Page -> Model -> ( Model, Cmd Msg )
updatePage page m =
    let
        model =
            { m | page = page }
    in
        case page of
            UseToken token ->
                ( { model | usetoken = Usetoken.Model.initModel token }
                , Cmd.map UsetokenMsg <| saveToken token
                )

            Submissions ->
                ( model, Cmd.map SubmissionsMsg <| Lazy.force getSubmissions )

            Submission id ->
                ( model, Cmd.map SubmissionMsg <| getSubmission id )

            _ ->
                ( { model | page = page }, Cmd.none )


view : Model -> Html Msg
view model =
    div [ class "app" ] [ pageView model ]


pageView : Model -> Html Msg
pageView model =
    case model.page of
        Register ->
            map LoginMsg (Login.view model.login)

        Thanks ->
            map ThanksMsg (Thanks.view model.thanks)

        UseToken _ ->
            map UsetokenMsg (Usetoken.View.view model.usetoken)

        Submissions ->
            map SubmissionsMsg (Submissions.View.view model.submissions)

        Submission _ ->
            map SubmissionMsg (Submission.View.view model.submission)


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.map SubmissionMsg <| Submission.Subscriptions.subscriptions model.submission


main : Program Never Model Msg
main =
    Navigation.program (UpdateUrl << hashParser)
        { init = init
        , update = update
        , view = view
        , subscriptions = subscriptions
        }
