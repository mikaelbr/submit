module Submission.Messages exposing (Msg(..))

import Submission.Model exposing (..)
import Http
import Time
import Ports exposing (FileData)


type Msg
    = Message
    | Get (Result Http.Error Submission)
    | Save Time.Time
    | Saved (Result Http.Error Submission)
    | TimeUpdated Time.Time
    | ToggleAutosave
    | Title String
    | Abstract String
    | Equipment String
    | Format String
    | IntendedAudience String
    | Language String
    | Length String
    | Outline String
    | Level String
    | Status String
    | SuggestedKeywords String
    | InfoToProgramCommittee String
    | AddSpeaker
    | SpeakerName Int String
    | SpeakerEmail Int String
    | SpeakerBio Int String
    | SpeakerZipCode Int String
    | SpeakerTwitter Int String
    | SpeakerPictureId Int String
    | RemoveSpeaker Int
    | FileSelected Speaker String
    | FileUploaded FileData
