module Nav.Requests
    exposing
        ( getSubmissions
        , getSubmission
        , getLoginToken
        , createSubmission
        , saveSubmission
        , deleteLoginToken
        , loginFailed
        )

import Http
import Login.Message
import Submissions.Messages
import Submissions.Decoder
import Submission.Messages
import Submission.Model
import Submission.Decoder
import Submission.Encoder
import Json.Decode exposing (Decoder)
import LocalStorage
import Lazy
import Navigation


-- This is so horribly, horribly wrong, but right now
-- the only way I see to both remove token and
-- navigate to register page in one function


loginFailed : Lazy.Lazy (Cmd msg)
loginFailed =
    Lazy.lazy <|
        \_ -> Navigation.newUrl ((\_ -> "#") <| LocalStorage.remove "login_token")


getLoginToken : String -> Cmd Login.Message.Msg
getLoginToken email =
    Http.send Login.Message.Submit <|
        jsonPost Http.expectString Http.emptyBody <|
            url [ "users", "authtoken" ]
                ++ "?email="
                ++ email


deleteLoginToken : Lazy.Lazy (Cmd Submissions.Messages.Msg)
deleteLoginToken =
    Lazy.lazy <|
        \() ->
            Http.send Submissions.Messages.LoggedOut <|
                Http.request
                    { method = "DELETE"
                    , headers = headers []
                    , url = url [ "users", "authtoken" ] ++ "?token=" ++ (Maybe.withDefault "" <| LocalStorage.get "login_token")
                    , body = Http.emptyBody
                    , expect = Http.expectString
                    , timeout = Nothing
                    , withCredentials = False
                    }


getSubmissions : Lazy.Lazy (Cmd Submissions.Messages.Msg)
getSubmissions =
    Lazy.lazy <|
        \() ->
            Http.send Submissions.Messages.Get <|
                jsonGet Submissions.Decoder.decoder <|
                    url [ "submissions" ]


getSubmission : String -> Cmd Submission.Messages.Msg
getSubmission id =
    Http.send Submission.Messages.Get <|
        jsonGet Submission.Decoder.decoder <|
            url [ "submissions", id ]


saveSubmission : Submission.Model.Submission -> Cmd Submission.Messages.Msg
saveSubmission submission =
    Http.send Submission.Messages.Saved <|
        jsonPut
            (Http.expectJson Submission.Decoder.decoder)
            (Http.jsonBody <| Submission.Encoder.encoder submission)
        <|
            url [ "submissions", submission.id ]


createSubmission : Lazy.Lazy (Cmd Submissions.Messages.Msg)
createSubmission =
    Lazy.lazy <|
        \() ->
            Http.send Submissions.Messages.Created <|
                jsonPost (Http.expectJson Submission.Decoder.decoder) Http.emptyBody <|
                    url [ "submissions" ]


url : List String -> String
url ls =
    "https://submit.javazone.no/api/" ++ String.join "/" ls


get : Json.Decode.Decoder a -> String -> Http.Request a
get =
    flip Http.get


jsonPost : Http.Expect a -> Http.Body -> String -> Http.Request a
jsonPost expect body url =
    Http.request
        { method = "POST"
        , headers = headers [ Http.header "Accept" "application/json" ]
        , url = url
        , body = body
        , expect = expect
        , timeout = Nothing
        , withCredentials = False
        }


jsonPut : Http.Expect a -> Http.Body -> String -> Http.Request a
jsonPut expect body url =
    Http.request
        { method = "PUT"
        , headers = headers [ Http.header "Accept" "application/json" ]
        , url = url
        , body = body
        , expect = expect
        , timeout = Nothing
        , withCredentials = False
        }


jsonGet : Decoder a -> String -> Http.Request a
jsonGet decoder url =
    Http.request
        { method = "GET"
        , headers = headers [ Http.header "Accept" "application/json" ]
        , url = url
        , body = Http.emptyBody
        , expect = Http.expectJson decoder
        , timeout = Nothing
        , withCredentials = False
        }


headers : List Http.Header -> List Http.Header
headers ls =
    case LocalStorage.get "login_token" of
        Just token ->
            Http.header "X-token" token :: ls

        _ ->
            ls
