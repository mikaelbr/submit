module Submissions.Messages exposing (Msg(..))

import Submissions.Model exposing (..)
import Http


type Msg
    = Message
    | Get (Result Http.Error Model)
    | Created (Result Http.Error Int)
