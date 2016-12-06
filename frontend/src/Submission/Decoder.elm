module Submission.Decoder exposing (decoder)

import Submission.Model exposing (..)
import Json.Decode exposing (Decoder, string, field, map)


decoder : Decoder Model
decoder =
    map Model <| field "entry" string
