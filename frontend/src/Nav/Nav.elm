module Nav.Nav exposing (..)

import Navigation
import UrlParser exposing (Parser, oneOf, map, s, parseHash, top, (</>), string, int)
import Nav.Model exposing (Page(..))
import Maybe
import LocalStorage


toHash : Page -> String
toHash page =
    case page of
        Register ->
            "#"

        Thanks ->
            "#thanks"

        UseToken token ->
            "#usetoken/" ++ token

        Submissions ->
            "#submissions"

        Submission id ->
            "#submissions/" ++ id


hashParser : Navigation.Location -> Page
hashParser location =
    case LocalStorage.get "login_token" of
        Just _ ->
            parseLoggedIn <| Maybe.withDefault Submissions <| parseHash pageParser location

        Nothing ->
            Register


parseLoggedIn : Page -> Page
parseLoggedIn page =
    case page of
        Register ->
            Submissions

        _ ->
            page


pageParser : Parser (Page -> a) a
pageParser =
    oneOf
        [ map Register top
        , map Thanks (s "thanks")
        , map UseToken (s "usetoken" </> string)
        , map Submissions (s "submissions")
        , map Submission (s "submissions" </> string)
        ]
