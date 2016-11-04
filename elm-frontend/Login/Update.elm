module Login.Update exposing (update)

import Login.Model exposing (Model)
import Login.Message exposing (Msg(..))
import Nav.Nav exposing (toHash)
import Nav.Model exposing (Page(..))
import HttpBuilder exposing (..)
import Task exposing (..)
import Navigation


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Email email ->
            ( { model | email = email }, Cmd.none )

        SubmitEmail ->
            ( model, register model.email )

        SubmitSucceeded response ->
            ( model, Navigation.newUrl <| toHash Thanks )

        SubmitFailed response ->
            ( model, Cmd.none )


register : String -> Cmd Msg
register email =
    Task.perform SubmitFailed SubmitSucceeded <| registerTask email


registerTask : String -> Task.Task (Error ()) (Response ())
registerTask email =
    send unitReader unitReader <| post <| url "http://localhost:8081/users/authtoken" [ ( "email", email ) ]
