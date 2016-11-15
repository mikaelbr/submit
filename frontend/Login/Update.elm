module Login.Update exposing (update)

import Login.Model exposing (Model)
import Login.Message exposing (Msg(..))
import Nav.Nav exposing (toHash)
import Nav.Model exposing (Page(..))
import Http exposing (Error, Response)
import Json.Decode exposing (succeed)
import Navigation


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Email email ->
            ( { model | email = email }, Cmd.none )

        SubmitEmail ->
            ( model, register model.email )

        Submit (Err _) ->
            ( model, Cmd.none )

        Submit (Ok _) ->
            ( model, Navigation.newUrl <| toHash Thanks )


register : String -> Cmd Msg
register email =
    Http.send Submit <| Http.post "http://localhost:8081/users/authtoken" Http.emptyBody <| succeed ()
