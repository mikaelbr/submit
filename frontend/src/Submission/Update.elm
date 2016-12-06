module Submission.Update exposing (update)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Message ->
            ( model, Cmd.none )

        Get (Err _) ->
            ( model, Cmd.none )

        Get (Ok data) ->
            ( data, Cmd.none )
