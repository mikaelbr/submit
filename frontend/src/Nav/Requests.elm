module Nav.Requests
    exposing
        ( getSubmissions
        , getSubmission
        , createSubmission
        , saveSubmission
        , saveComment
        )

import Http
import Messages
import Decoder.Submissions
import Decoder.Submission
import Encoder.Submission
import Model.Submission
import Json.Decode exposing (Decoder)
import Lazy


getSubmissions : String -> Cmd Messages.Msg
getSubmissions token =
    Http.send Messages.SubmissionsGet <|
        jsonGet Decoder.Submissions.decoder token <|
            url [ "submissions" ]


getSubmission : String -> String -> Cmd Messages.Msg
getSubmission id token =
    Http.send Messages.GetSubmission <|
        jsonGet Decoder.Submission.decoder token <|
            url [ "submissions", id ]


saveSubmission : Model.Submission.Submission -> String -> Cmd Messages.Msg
saveSubmission submission token =
    Http.send Messages.SavedSubmission <|
        jsonPut
            (Http.expectJson Decoder.Submission.decoder)
            (Http.jsonBody <| Encoder.Submission.encoder submission)
            token
        <|
            url [ "submissions", submission.id ]


createSubmission : String -> Cmd Messages.Msg
createSubmission token =
    Http.send Messages.SubmissionsCreated <|
        jsonPost (Http.expectJson Decoder.Submission.decoder) Http.emptyBody token <|
            url [ "submissions" ]


saveComment : Model.Submission.Model -> Model.Submission.Submission -> String -> Cmd Messages.Msg
saveComment model submission token =
    Http.send Messages.CommentSent <|
        jsonPost
            (Http.expectJson Decoder.Submission.decoder)
            (Http.jsonBody <| Encoder.Submission.encodeComment model)
            token
        <|
            url [ "submissions", submission.id, "comments" ]


url : List String -> String
url ls =
    "https://test-submit.javazone.no/api/" ++ String.join "/" ls


get : Json.Decode.Decoder a -> String -> Http.Request a
get =
    flip Http.get


jsonPost : Http.Expect a -> Http.Body -> String -> String -> Http.Request a
jsonPost expect body token url =
    Http.request
        { method = "POST"
        , headers = [ Http.header "Accept" "application/json", tokenHeader token ]
        , url = url
        , body = body
        , expect = expect
        , timeout = Nothing
        , withCredentials = False
        }


jsonPut : Http.Expect a -> Http.Body -> String -> String -> Http.Request a
jsonPut expect body token url =
    Http.request
        { method = "PUT"
        , headers = [ Http.header "Accept" "application/json", tokenHeader token ]
        , url = url
        , body = body
        , expect = expect
        , timeout = Nothing
        , withCredentials = False
        }


jsonGet : Decoder a -> String -> String -> Http.Request a
jsonGet decoder token url =
    Http.request
        { method = "GET"
        , headers = [ Http.header "Accept" "application/json", tokenHeader token ]
        , url = url
        , body = Http.emptyBody
        , expect = Http.expectJson decoder
        , timeout = Nothing
        , withCredentials = False
        }


tokenHeader : String -> Http.Header
tokenHeader token =
    Http.header "X-token" token
