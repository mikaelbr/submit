module Nav.Requests
    exposing
        ( getSubmissions
        , getSubmission
        , createSubmission
        , saveSubmission
        , saveComment
        )

import Http exposing (Error, Request)
import Messages
import Decoder.Submissions
import Decoder.Submission
import Encoder.Submission
import Model.Submission
import Json.Decode exposing (Decoder)


send : (Result Error a -> Messages.Msg) -> Request a -> Cmd Messages.Msg
send msg request =
    Http.send msg request
        |> Cmd.map
            (\m ->
                case m of
                    Messages.SubmissionsGet (Err (Http.BadStatus res)) ->
                        checkStatus res m

                    Messages.GetSubmission (Err (Http.BadStatus res)) ->
                        checkStatus res m

                    Messages.SavedSubmission (Err (Http.BadStatus res)) ->
                        checkStatus res m

                    Messages.SubmissionsCreated (Err (Http.BadStatus res)) ->
                        checkStatus res m

                    Messages.CommentSent (Err (Http.BadStatus res)) ->
                        checkStatus res m

                    _ ->
                        m
            )


checkStatus : Http.Response body -> Messages.Msg -> Messages.Msg
checkStatus res msg =
    if res.status.code == 403 then
        Messages.Reauthenticate
    else
        msg


getSubmissions : String -> Cmd Messages.Msg
getSubmissions token =
    send Messages.SubmissionsGet <|
        jsonGet Decoder.Submissions.decoder token <|
            url [ "submissions" ]


getSubmission : String -> String -> Cmd Messages.Msg
getSubmission id token =
    send Messages.GetSubmission <|
        jsonGet Decoder.Submission.decoder token <|
            url [ "submissions", id ]


saveSubmission : Model.Submission.Submission -> String -> Cmd Messages.Msg
saveSubmission submission token =
    send Messages.SavedSubmission <|
        jsonPut
            (Http.expectJson Decoder.Submission.decoder)
            (Http.jsonBody <| Encoder.Submission.encoder submission)
            token
        <|
            url [ "submissions", submission.id ]


createSubmission : String -> Cmd Messages.Msg
createSubmission token =
    send Messages.SubmissionsCreated <|
        jsonPost (Http.expectJson Decoder.Submission.decoder) Http.emptyBody token <|
            url [ "submissions" ]


saveComment : Model.Submission.Model -> Model.Submission.Submission -> String -> Cmd Messages.Msg
saveComment model submission token =
    send Messages.CommentSent <|
        jsonPost
            (Http.expectJson Decoder.Submission.decoder)
            (Http.jsonBody <| Encoder.Submission.encodeComment model)
            token
        <|
            url [ "submissions", submission.id, "comments" ]


url : List String -> String
url ls =
    "/api/" ++ String.join "/" ls


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
    Http.header "Authorization" <| "Bearer " ++ token
