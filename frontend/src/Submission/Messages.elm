module Submission.Messages exposing (Msg(..))

import Submission.Model exposing (Model)
import Http


type Msg
    = Message
    | Get (Result Http.Error Model)
