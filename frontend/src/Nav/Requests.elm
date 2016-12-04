module Nav.Requests exposing (getLoginCookie)

import Http
import Json.Decode exposing (string)
import Usetoken.Messages


getLoginCookie : String -> String -> Cmd Usetoken.Messages.Msg
getLoginCookie baseUrl token =
    let
        cookieUrl =
            url [ baseUrl, "users", "authtoken", "use" ]
                ++ "?token="
                ++ token
    in
        Http.send Usetoken.Messages.Get <|
            Http.post cookieUrl Http.emptyBody string


url : List String -> String
url =
    String.join "/"



-- jsonGet : Decoder a -> String -> Http.Request a
-- jsonGet decoder url =
--     Http.request
--         { method = "GET"
--         , headers = [ Http.header "Accept" "application/json" ]
--         , url = url
--         , body = Http.emptyBody
--         , expect = Http.expectJson decoder
--         , timeout = Nothing
--         , withCredentials = False
--         }
