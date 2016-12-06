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
    Http.send Usetoken.Messages.Get <|
        postWithoutBody Json.Decode.string <|
            url [ baseUrl, "users", "authtoken", "use" ]
                ++ "?token="
                ++ token


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


createSubmission : Cmd Submissions.Messages.Msg
createSubmission =
    Http.send Submissions.Messages.Created <|
        postWithoutBody Json.Decode.int "insert-url-here"


url : List String -> String
url =
    String.join "/"


get : Json.Decode.Decoder a -> String -> Http.Request a
get =
    flip Http.get


postWithoutBody : Json.Decode.Decoder a -> String -> Http.Request a
postWithoutBody decoder url =
    Http.request
        { method = "POST"
        , headers = []
        , url = url
        , body = Http.emptyBody
        , expect = Http.expectJson decoder
        , timeout = Nothing
        , withCredentials = False
        }



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
