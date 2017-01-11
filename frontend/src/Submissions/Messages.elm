module Submissions.Messages exposing (Msg(..))

import Submissions.Model exposing (..)
import Submission.Model
import Http


type Msg
    = Message
    | Get (Result Http.Error Submissions)
    | CreateTalk
    | Created (Result Http.Error Submission.Model.Submission)
    | Logout
    | LoggedOut (Result Http.Error String)
    | TokenRemoved ()
