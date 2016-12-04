module Main exposing (..)

import Html exposing (Html, div, map)
import Navigation
import Html.Attributes exposing (class)
import Login.Update as Login
import Nav.Nav exposing (hashParser, toHash)
import Model exposing (Model)
import Flags exposing (Flags)
import Message exposing (Msg(..))
import Nav.Model exposing (Page(..))
import Login.Login as Login
import Login.Update as LoginUpdate
import Thanks.Thanks as Thanks
import Usetoken.View
import Usetoken.Model
import Usetoken.Update
import Nav.Requests exposing (getLoginCookie)


initModel : Flags -> Page -> Model
initModel flags page =
    Model flags (Login.initModel flags) (Thanks.initModel) (Usetoken.Model.initModel flags "") page


init : Flags -> Navigation.Location -> ( Model, Cmd Msg )
init flags location =
    let
        page =
            hashParser location
    in
        ( initModel flags page, Navigation.newUrl <| toHash page )


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


updatePage : Page -> Model -> ( Model, Cmd Msg )
updatePage page m =
    let
        model =
            { m | page = page }
    in
        case page of
            UseToken token ->
                ( { model | usetoken = Usetoken.Model.initModel model.flags token }
                , Cmd.map UsetokenMsg <| getLoginCookie model.flags.url token
                )

            _ ->
                ( { model | page = page }, Cmd.none )


view : Model -> Html Msg
view model =
    div [ class "app" ] [ pageView model ]


pageView : Model -> Html Msg
pageView model =
    case Debug.log "page" model.page of
        Register ->
            map LoginMsg (Login.view model.login)

        Thanks ->
            map ThanksMsg (Thanks.view model.thanks)

        UseToken _ ->
            map UsetokenMsg (Usetoken.View.view model.usetoken)

        Submissions ->
            map UsetokenMsg (Usetoken.View.view model.usetoken)


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none


main : Program Flags Model Msg
main =
    Navigation.programWithFlags (UpdateUrl << hashParser)
        { init = init
        , update = update
        , view = view
        , subscriptions = subscriptions
        }
