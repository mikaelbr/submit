module Submission.Subscriptions exposing (subscriptions)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)
import Time exposing (every, second)
import Backend.Network exposing (RequestStatus(..))


subscriptions : Model -> Sub Msg
subscriptions model =
    case model.submission of
        Complete submission ->
            if model.autosave && model.dirty && submission.editable then
                every (30 * second) Save
            else
                Sub.none

        _ ->
            Sub.none
