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
    | Length String
    | Outline String
    | Level String
    | SuggestedKeywords String
    | InfoToProgramCommittee String
    | AddSpeaker
    | SpeakerName Int String
    | SpeakerEmail Int String
    | SpeakerBio Int String
    | SpeakerZipCode Int String
    | SpeakerTwitter Int String
    | RemoveSpeaker Int
