module Submission.Update exposing (update)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)
import Backend.Network exposing (RequestStatus(..))
import Nav.Requests exposing (saveSubmission, loginFailed)
import Time
import Task
import Lazy
import Ports exposing (fileSelected, ImagePostData)


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

        Saved (Ok s) ->
            case model.submission of
                Complete submission ->
                    ( { model
                        | dirty = False
                        , submission = Complete { submission | speakers = s.speakers }
                      }
                    , Task.perform TimeUpdated Time.now
                    )

                _ ->
                    ( model, Cmd.none )

        TimeUpdated time ->
            ( { model | lastSaved = Just time }, Cmd.none )

        ToggleAutosave ->
            ( { model | autosave = not model.autosave }, Cmd.none )

        Title title ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | title = title }

        Abstract abstract ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | abstract = abstract }

        Equipment equipment ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | equipment = equipment }

        Format format ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | format = format, length = getLength format }

        Status status ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | status = status }

        IntendedAudience intendedAudience ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | intendedAudience = intendedAudience }

        Language language ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | language = language }

        Length length ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | length = length }

        Outline outline ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | outline = outline }

        Level level ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | level = level }

        SuggestedKeywords suggestedKeywords ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | suggestedKeywords = suggestedKeywords }

        InfoToProgramCommittee infoToProgramCommittee ->
            updateField model (\_ -> Cmd.none) <|
                \s -> { s | infoToProgramCommittee = infoToProgramCommittee }

        AddSpeaker ->
            updateField model (\s -> saveSubmission s) <|
                \s -> { s | speakers = s.speakers ++ [ initSpeaker s.speakers ] }

        SpeakerName i name ->
            updateField model (\_ -> Cmd.none) <|
                \s ->
                    let
                        updatedSpeakers =
                            List.map
                                (\speaker -> updateSpeaker i speaker (\sp -> { sp | name = name }))
                                s.speakers
                    in
                        { s | speakers = updatedSpeakers }

        SpeakerEmail i email ->
            updateField model (\_ -> Cmd.none) <|
                \s ->
                    let
                        updatedSpeakers =
                            List.map
                                (\speaker -> updateSpeaker i speaker (\sp -> { sp | email = email }))
                                s.speakers
                    in
                        { s | speakers = updatedSpeakers }

        SpeakerBio i bio ->
            updateField model (\_ -> Cmd.none) <|
                \s ->
                    let
                        updatedSpeakers =
                            List.map
                                (\speaker -> updateSpeaker i speaker (\sp -> { sp | bio = bio }))
                                s.speakers
                    in
                        { s | speakers = updatedSpeakers }

        SpeakerZipCode i zipCode ->
            updateField model (\_ -> Cmd.none) <|
                \s ->
                    let
                        speakers =
                            List.map
                                (\speaker -> updateSpeaker i speaker (\sp -> { sp | zipCode = zipCode }))
                                s.speakers
                    in
                        { s | speakers = speakers }

        SpeakerTwitter i twitter ->
            updateField model (\_ -> Cmd.none) <|
                \s ->
                    let
                        speakers =
                            List.map
                                (\speaker -> updateSpeaker i speaker (\sp -> { sp | twitter = twitter }))
                                s.speakers
                    in
                        { s | speakers = speakers }

        RemoveSpeaker i ->
            updateField model (\s -> saveSubmission s) <|
                \s ->
                    let
                        speakers =
                            List.filter
                                (\( j, _ ) -> j /= i)
                                s.speakers
                    in
                        { s | speakers = speakers }

        FileSelected speaker i id ->
            case model.submission of
                Complete submission ->
                    ( model, fileSelected <| ImagePostData id submission.id speaker.id i )

                _ ->
                    ( model, Cmd.none )

        FileUploaded image ->
            updateField model (\_ -> Cmd.none) <|
                \s ->
                    let
                        speakers =
                            List.map
                                (\speaker -> updateSpeaker image.i speaker (\sp -> { sp | pictureUrl = image.url, hasPicture = True }))
                                s.speakers
                    in
                        { s | speakers = speakers }


updateField : Model -> (Submission -> Cmd Msg) -> (Submission -> Submission) -> ( Model, Cmd Msg )
updateField model cmdFn updateFunction =
    case model.submission of
        Complete submission ->
            let
                updated =
                    updateFunction submission
            in
                ( { model | dirty = True, submission = Complete updated }, cmdFn updated )

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
