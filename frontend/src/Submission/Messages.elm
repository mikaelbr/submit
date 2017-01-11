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
    | Abstract String
    | IntendedAudience String
    | Outline String
