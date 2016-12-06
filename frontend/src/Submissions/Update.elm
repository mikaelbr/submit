module Submissions.Update exposing (update)

import Submissions.Model exposing (..)
import Submissions.Messages exposing (..)
import Navigation
import Nav.Nav exposing (toHash)
import Nav.Model


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Message ->
            ( model, Cmd.none )

        Get (Err _) ->
            ( model, Cmd.none )

        Get (Ok data) ->
            ( data, Cmd.none )

        Created (Err _) ->
            ( model, Cmd.none )

        Created (Ok id) ->
            ( model, Navigation.newUrl << toHash <| Nav.Model.Submission id )
