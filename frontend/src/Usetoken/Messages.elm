module Usetoken.Messages exposing (Msg(..))

import Http


type Msg
    = Update
    | Get (Result Http.Error String)
