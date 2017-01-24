module Submission.Subscriptions exposing (subscriptions)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)
import Time exposing (every, second)


subscriptions : Model -> Sub Msg
subscriptions model =
    if model.autosave then
        every (30 * second) Save
    else
        Sub.none
