module Nav.Nav exposing (..)

import Navigation
import UrlParser exposing (Parser, oneOf, format, s, parse)
import String exposing (dropLeft)
import Model exposing (Model)
import Message exposing (Msg)
import Nav.Model exposing (Page(..))
import Debug


toHash : Page -> String
toHash page =
    case page of
        Register ->
            "#"

        Thanks ->
            "#thanks"


hashParser : Navigation.Location -> Result String Page
hashParser location =
    parse identity pageParser (dropLeft 1 location.hash)


pageParser : Parser (Page -> a) a
pageParser =
    oneOf
        [ format Register (s "")
        , format Thanks (s "thanks")
        ]


urlUpdate : Result String Page -> Model -> ( Model, Cmd Msg )
urlUpdate result model =
    case Debug.log "urlUpdate" result of
        Err a ->
            ( model, Navigation.modifyUrl (toHash model.page) )

        Ok newPage ->
            { model | page = newPage } ! []
