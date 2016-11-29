module Main exposing (..)

import Html exposing (Html, div, map)
import Navigation
import Html.Attributes exposing (class)
import Login.Update as Login
import Nav.Nav exposing (hashParser)
import Model exposing (Model)
import Flags exposing (Flags)
import Message exposing (Msg(..))
import Nav.Model exposing (Page(..))
import Login.Login as Login
import Login.Update as LoginUpdate
import Thanks.Thanks as Thanks


initModel : Flags -> Page -> Model
initModel flags page =
    Model flags (Login.initModel flags) (Thanks.initModel) page


init : Flags -> Navigation.Location -> ( Model, Cmd Msg )
init flags location =
    let
        page =
            hashParser location
    in
        ( initModel flags page, Cmd.none )


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        UpdateUrl page ->
            ( { model | page = page }, Cmd.none )

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
