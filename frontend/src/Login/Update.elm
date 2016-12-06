module Login.Update exposing (update)

import Login.Model exposing (Model)
import Login.Message exposing (Msg(..))
import Nav.Nav exposing (toHash)
import Nav.Model exposing (Page(..))
import Nav.Requests exposing (getLoginToken)
import Navigation


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Email email ->
            ( { model | email = email }, Cmd.none )

        SubmitEmail ->
            ( model, getLoginToken model.email )

        Submit (Err _) ->
            ( model, Cmd.none )

        Submit (Ok _) ->
            ( model, Navigation.newUrl <| toHash Thanks )
