module Submissions.Decoder exposing (decoder)

import Submissions.Model exposing (..)
import Json.Decode exposing (Decoder, string, field, map, map2, int, list)
import Json.Decode.Pipeline exposing (decode, required)


decoder : Decoder Submissions
decoder =
    decode Submissions
        |> required "years" (list decodeYear)


decodeYear : Decoder Year
decodeYear =
    decode Year
        |> required "year" string
        |> required "submissions" (list decodeSubmission)


decodeSubmission : Decoder Submission
decodeSubmission =
    decode Submission
        |> required "id" string
        |> required "title" string
