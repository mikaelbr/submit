module Submission.Update exposing (update)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)
import Backend.Network exposing (RequestStatus(..))
import Nav.Requests exposing (saveSubmission)
import Time
import Task


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Message ->
            ( model, Cmd.none )

        Get (Err error) ->
            ( { model | submission = Error <| toString error }, Cmd.none )

        Get (Ok submission) ->
            ( { model | submission = Complete submission }, Cmd.none )

        Save ->
            case model.submission of
                Complete submission ->
                    ( model, saveSubmission submission )

                _ ->
                    ( model, Cmd.none )

        Saved (Err _) ->
            ( model, Cmd.none )

        Saved (Ok _) ->
            ( model, Task.perform TimeUpdated Time.now )

        TimeUpdated time ->
            ( { model | lastSaved = Just time }, Cmd.none )

        Abstract abstract ->
            updateField model <|
                \submission -> { submission | abstract = abstract }

        IntendedAudience intendedAudience ->
            updateField model <|
                \submission -> { submission | intendedAudience = intendedAudience }

        Outline outline ->
            updateField model <|
                \submission -> { submission | outline = outline }


updateField : Model -> (Submission -> Submission) -> ( Model, Cmd Msg )
updateField model updateFunction =
    case model.submission of
        Complete submission ->
            ( { model | submission = Complete <| updateFunction submission }, Cmd.none )

        _ ->
            ( model, Cmd.none )
