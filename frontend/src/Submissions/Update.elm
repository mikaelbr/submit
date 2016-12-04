module Submissions.Update exposing (update)

import Submissions.Model exposing (..)
import Submissions.Messages exposing (..)


update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    case msg of
        Message ->
            (model, Cmd.none)
