module Main exposing (..)

import Html exposing (Html, div)
import Html.App exposing (map)
import Navigation
import UrlParser exposing (Parser, oneOf, format, s)
import String exposing (dropLeft)
import Html.Attributes exposing (class)
import Login.Login as Login


type alias Model =
    { login : Login.Model
    , page : Page
    }


initModel : Model
initModel =
    Model (Login.initModel) Register


init : Result String Page -> ( Model, Cmd Msg )
init result =
    urlUpdate result initModel


type Msg
    = Login Login.Msg


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Login loginMsg ->
            let
                ( newLogin, loginCmd ) =
                    Login.update loginMsg model.login

                mappedCmd =
                    Cmd.map Login loginCmd
            in
                ( { model | login = newLogin }, mappedCmd )


view : Model -> Html Msg
view model =
    div [ class "app" ] [ map Login (Login.view model.login) ]


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none


type Page
    = Register


toHash : Page -> String
toHash page =
    case page of
        Register ->
            "#"


hashParser : Navigation.Location -> Result String Page
hashParser location =
    UrlParser.parse identity pageParser (dropLeft 1 location.hash)


pageParser : Parser (Page -> a) a
pageParser =
    oneOf
        [ format Register (s "") ]


urlUpdate : Result String Page -> Model -> ( Model, Cmd Msg )
urlUpdate result model =
    case result of
        Err _ ->
            ( model, Navigation.modifyUrl (toHash model.page) )

        Ok page ->
            ( { model | page = page }, Cmd.none )


main : Program Never
main =
    Navigation.program (Navigation.makeParser hashParser)
        { init = init
        , update = update
        , view = view
        , subscriptions = subscriptions
        , urlUpdate = urlUpdate
        }
