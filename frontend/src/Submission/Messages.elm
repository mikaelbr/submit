module Submission.Messages exposing (Msg(..))

import Submission.Model exposing (..)
import Http
import Time


type Msg
    = Message
    | Get (Result Http.Error Submission)
    | Save
    | Saved (Result Http.Error Submission)
    | TimeUpdated Time.Time
    | Title String
    | Abstract String
    | Equipment String
    | Format String
    | IntendedAudience String
    | Language String
    | Outline String
    | AddSpeaker
    | SpeakerName Int String
    | SpeakerEmail Int String
    | SpeakerBio Int String
    | RemoveSpeaker Int
