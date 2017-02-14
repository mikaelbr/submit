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
import Messages
import Decoder.Submissions
import Decoder.Submission
import Encoder.Submission
import Model.Submission
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


getLoginToken : String -> Cmd Messages.Msg
getLoginToken email =
    Http.send Messages.LoginSubmit <|
        jsonPost Http.expectString Http.emptyBody <|
            url [ "users", "authtoken" ]
                ++ "?email="
                ++ email


deleteLoginToken : Lazy.Lazy (Cmd Messages.Msg)
deleteLoginToken =
    Lazy.lazy <|
        \() ->
            Http.send Messages.SubmissionsLoggedOut <|
                Http.request
                    { method = "DELETE"
                    , headers = headers []
                    , url = url [ "users", "authtoken" ] ++ "?token=" ++ (Maybe.withDefault "" <| LocalStorage.get "login_token")
                    , body = Http.emptyBody
                    , expect = Http.expectString
                    , timeout = Nothing
                    , withCredentials = False
                    }


getSubmissions : Lazy.Lazy (Cmd Messages.Msg)
getSubmissions =
    Lazy.lazy <|
        \() ->
            Http.send Messages.SubmissionsGet <|
                jsonGet Decoder.Submissions.decoder <|
                    url [ "submissions" ]


getSubmission : String -> Cmd Messages.Msg
getSubmission id =
    Http.send Messages.GetSubmission <|
        jsonGet Decoder.Submission.decoder <|
            url [ "submissions", id ]


saveSubmission : Model.Submission.Submission -> Cmd Messages.Msg
saveSubmission submission =
    Http.send Messages.SavedSubmission <|
        jsonPut
            (Http.expectJson Decoder.Submission.decoder)
            (Http.jsonBody <| Encoder.Submission.encoder submission)
        <|
            url [ "submissions", submission.id ]


createSubmission : Lazy.Lazy (Cmd Messages.Msg)
createSubmission =
    Lazy.lazy <|
        \() ->
            Http.send Messages.SubmissionsCreated <|
                jsonPost (Http.expectJson Decoder.Submission.decoder) Http.emptyBody <|
                    url [ "submissions" ]


url : List String -> String
url ls =
    "/api/" ++ String.join "/" ls


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
