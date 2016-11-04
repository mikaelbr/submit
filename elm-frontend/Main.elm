module Main exposing (..)

import Html exposing (Html, div)
import Html.App exposing (map)
import Navigation
import Html.Attributes exposing (class)
import Login.Update as Login
import Nav.Nav exposing (urlUpdate, hashParser)
import Model exposing (Model)
import Message exposing (Msg(..))
import Nav.Model exposing (Page(..))
import Login.Login as Login
import Login.Update as LoginUpdate
import Thanks.Thanks as Thanks
import Debug


initModel : Model
initModel =
    Model (Login.initModel) (Thanks.initModel) Register


init : Result String Page -> ( Model, Cmd Msg )
init result =
    urlUpdate result initModel


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    Debug.log (toString model) <|
        case msg of
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


main : Program Never
main =
    Navigation.program (Navigation.makeParser hashParser)
        { init = init
        , update = update
        , view = view
        , subscriptions = subscriptions
        , urlUpdate = urlUpdate
        }
