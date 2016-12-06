module Nav.Requests exposing (getLoginCookie, getSubmissions, getSubmission)

import Http
import Usetoken.Messages
import Submissions.Messages
import Submissions.Decoder
import Submission.Messages
import Submission.Decoder


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
    let
        getSubmissionsUrl =
            url [ baseUrl, "submissions" ]
    in
        Http.send Submissions.Messages.Get <|
            Http.get getSubmissionsUrl Submissions.Decoder.decoder


getSubmission : String -> Int -> Cmd Submission.Messages.Msg
getSubmission baseUrl id =
    let
        getSubmissionUrl =
            url [ baseUrl, "submissions", toString id ]
    in
        Http.send Submission.Messages.Get <|
            Http.get getSubmissionUrl Submission.Decoder.decoder


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
