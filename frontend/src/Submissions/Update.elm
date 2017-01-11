module Submissions.Update exposing (update)

import Submissions.Model exposing (..)
import Submissions.Messages exposing (..)
import Navigation
import Nav.Nav exposing (toHash)
import Nav.Model
import Nav.Requests exposing (createSubmission)
import Backend.Network exposing (RequestStatus(..))


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Message ->
            ( model, Cmd.none )

        CreateTalk ->
            ( model, createSubmission )

        Get (Err message) ->
            ( { model | submissions = Error <| toString message }, Cmd.none )

        Get (Ok submissions) ->
            ( { model | submissions = Complete submissions }, Cmd.none )

        Created (Err _) ->
            ( model, Cmd.none )

        Created (Ok id) ->
            ( model, Navigation.newUrl << toHash <| Nav.Model.Submission id )
