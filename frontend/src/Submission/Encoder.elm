module Submission.Encoder exposing (encoder)

import Submission.Model exposing (..)
import Json.Encode exposing (Value, object, string)


encoder : Submission -> Value
encoder submission =
    object
        [ ( "abstract", string submission.abstract ) ]
