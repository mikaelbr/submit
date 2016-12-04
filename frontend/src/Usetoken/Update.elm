module Usetoken.Update exposing (update)

import Usetoken.Model exposing (..)
import Usetoken.Messages exposing (..)
import Navigation
import Nav.Model exposing (Page(..))
import Nav.Nav exposing (toHash)


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Update ->
            ( model, Cmd.none )

        Get (Err _) ->
            ( model, Cmd.none )

        Get (Ok _) ->
            ( model, Navigation.newUrl <| toHash Submissions )
