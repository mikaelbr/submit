module Nav.Requests exposing (getLoginCookie, getSubmissions, getSubmission)

import Http
import Usetoken.Messages
import Submissions.Messages
import Submissions.Decoder
import Submission.Messages
import Submission.Decoder
import Json.Decode


getLoginCookie : String -> String -> Cmd Usetoken.Messages.Msg
getLoginCookie baseUrl token =
    let
        cookieUrl =
            url [ baseUrl, "users", "authtoken", "use" ]
                ++ "?token="
                ++ token
    in
        Http.send Usetoken.Messages.Get <|
            Http.request
                { method = "POST"
                , headers = []
                , url = cookieUrl
                , body = Http.emptyBody
                , expect = Http.expectString
                , timeout = Nothing
                , withCredentials = False
                }


getSubmissions : String -> Cmd Submissions.Messages.Msg
getSubmissions baseUrl =
    Http.send Submissions.Messages.Get <|
        get Submissions.Decoder.decoder <|
            url [ baseUrl, "submissions" ]


getSubmission : String -> Int -> Cmd Submission.Messages.Msg
getSubmission baseUrl id =
    Http.send Submission.Messages.Get <|
        get Submission.Decoder.decoder <|
            url [ baseUrl, "submissions", toString id ]


url : List String -> String
url =
    String.join "/"


get : Json.Decode.Decoder a -> String -> Http.Request a
get =
    flip Http.get



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
