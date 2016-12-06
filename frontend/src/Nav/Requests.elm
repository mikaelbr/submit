module Nav.Requests exposing (getLoginCookie, getSubmissions, getSubmission)

import Http
import Usetoken.Messages
import Submissions.Messages
import Submissions.Decoder
import Submission.Messages
import Submission.Decoder
import Json.Decode


getLoginCookie : String -> Cmd Usetoken.Messages.Msg
getLoginCookie token =
    Http.send Usetoken.Messages.Get <|
        postWithoutBody (Http.expectString) <|
            url [ "users", "authtoken", "use" ]
                ++ "?token="
                ++ token


getSubmissions : Cmd Submissions.Messages.Msg
getSubmissions =
    Http.send Submissions.Messages.Get <|
        get Submissions.Decoder.decoder <|
            url [ "submissions" ]


getSubmission : Int -> Cmd Submission.Messages.Msg
getSubmission id =
    Http.send Submission.Messages.Get <|
        get Submission.Decoder.decoder <|
            url [ "submissions", toString id ]


createSubmission : Cmd Submissions.Messages.Msg
createSubmission =
    Http.send Submissions.Messages.Created <|
        postWithoutBody (Http.expectJson Json.Decode.int) "insert-url-here"


url : List String -> String
url ls =
    "api/" ++ String.join "/" ls


get : Json.Decode.Decoder a -> String -> Http.Request a
get =
    flip Http.get


postWithoutBody : Http.Expect a -> String -> Http.Request a
postWithoutBody expect url =
    Http.request
        { method = "POST"
        , headers = []
        , url = url
        , body = Http.emptyBody
        , expect = expect
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
