module Nav.Nav exposing (..)

import Navigation
import UrlParser exposing (Parser, oneOf, map, s, parseHash, top)
import Model exposing (Model)
import Message exposing (Msg)
import Nav.Model exposing (Page(..))
import Maybe
import Debug


toHash : Page -> String
toHash page =
    case page of
        Register ->
            "#"

        Thanks ->
            "#thanks"


hashParser : Navigation.Location -> Page
hashParser location =
    Maybe.withDefault Register <| parseHash pageParser location


pageParser : Parser (Page -> a) a
pageParser =
    oneOf
        [ map Register top
        , map Thanks (s "thanks")
        ]



-- urlUpdate : Result String Page -> Model -> ( Model, Cmd Msg )
-- urlUpdate result model =
--     case Debug.log "urlUpdate" result of
--         Err a ->
--             ( model, Navigation.modifyUrl (toHash model.page) )
--         Ok newPage ->
--             { model | page = newPage } ! []
