module Submission.Update exposing (update)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)
import Backend.Network exposing (RequestStatus(..))
import Nav.Requests exposing (saveSubmission, loginFailed)
import Time
import Task
import Lazy
import Ports exposing (fileSelected)


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Message ->
            ( model, Cmd.none )

        Get (Err error) ->
            ( { model | submission = Error <| toString error }, Lazy.force loginFailed )

        Get (Ok submission) ->
            ( { model | submission = Complete submission }, Cmd.none )

        Save _ ->
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

        ToggleAutosave ->
            ( { model | autosave = not model.autosave }, Cmd.none )

        Title title ->
            updateField model <|
                \s -> { s | title = title }

        Abstract abstract ->
            updateField model <|
                \s -> { s | abstract = abstract }

        Equipment equipment ->
            updateField model <|
                \s -> { s | equipment = equipment }

        Format format ->
            updateField model <|
                \s -> { s | format = format, length = getLength format }

        Status status ->
            updateField model <|
                \s -> { s | status = status, length = getLength status }

        IntendedAudience intendedAudience ->
            updateField model <|
                \s -> { s | intendedAudience = intendedAudience }

        Language language ->
            updateField model <|
                \s -> { s | language = language }

        Length length ->
            updateField model <|
                \s -> { s | length = length }

        Outline outline ->
            updateField model <|
                \s -> { s | outline = outline }

        Level level ->
            updateField model <|
                \s -> { s | level = level }

        SuggestedKeywords suggestedKeywords ->
            updateField model <|
                \s -> { s | suggestedKeywords = suggestedKeywords }

        InfoToProgramCommittee infoToProgramCommittee ->
            updateField model <|
                \s -> { s | infoToProgramCommittee = infoToProgramCommittee }

        AddSpeaker ->
            updateField model <|
                \s -> { s | speakers = s.speakers ++ [ initSpeaker s.speakers ] }

        SpeakerName i name ->
            updateField model <|
                \s ->
                    let
                        updatedSpeakers =
                            List.map
                                (\speaker -> updateSpeaker i speaker (\sp -> { sp | name = name }))
                                s.speakers
                    in
                        { s | speakers = updatedSpeakers }

        SpeakerEmail i email ->
            updateField model <|
                \s ->
                    let
                        updatedSpeakers =
                            List.map
                                (\speaker -> updateSpeaker i speaker (\sp -> { sp | email = email }))
                                s.speakers
                    in
                        { s | speakers = updatedSpeakers }

        SpeakerBio i bio ->
            updateField model <|
                \s ->
                    let
                        updatedSpeakers =
                            List.map
                                (\speaker -> updateSpeaker i speaker (\sp -> { sp | bio = bio }))
                                s.speakers
                    in
                        { s | speakers = updatedSpeakers }

        SpeakerZipCode i zipCode ->
            updateField model <|
                \s ->
                    let
                        speakers =
                            List.map
                                (\speaker -> updateSpeaker i speaker (\sp -> { sp | zipCode = zipCode }))
                                s.speakers
                    in
                        { s | speakers = speakers }

        SpeakerTwitter i twitter ->
            updateField model <|
                \s ->
                    let
                        speakers =
                            List.map
                                (\speaker -> updateSpeaker i speaker (\sp -> { sp | twitter = twitter }))
                                s.speakers
                    in
                        { s | speakers = speakers }

        RemoveSpeaker i ->
            updateField model <|
                \s ->
                    let
                        speakers =
                            List.filter
                                (\( j, _ ) -> j /= i)
                                s.speakers
                    in
                        { s | speakers = speakers }

        FileSelected id ->
            ( model, fileSelected id )

        FileUploaded fileData ->
            ( model, Cmd.none )


updateField : Model -> (Submission -> Submission) -> ( Model, Cmd Msg )
updateField model updateFunction =
    case model.submission of
        Complete submission ->
            ( { model | dirty = True, submission = Complete <| updateFunction submission }, Cmd.none )

        _ ->
            ( model, Cmd.none )


updateSpeaker : Int -> ( Int, Speaker ) -> (Speaker -> Speaker) -> ( Int, Speaker )
updateSpeaker i ( j, speaker ) updateFunction =
    if i == j then
        ( j, updateFunction speaker )
    else
        ( j, speaker )


getLength : String -> String
getLength format =
    case format of
        "presentation" ->
            "45"

        "lightning-talk" ->
            "10"

        _ ->
            "120"
