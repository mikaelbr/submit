module Submission.Subscriptions exposing (subscriptions)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none
