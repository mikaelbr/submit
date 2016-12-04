module Submissions.Subscriptions exposing (subscriptions)

import Submissions.Model exposing (..)
import Submissions.Messages exposing (..)


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none
