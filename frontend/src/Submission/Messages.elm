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
    | Format String
    | IntendedAudience String
    | Language String
    | Outline String
