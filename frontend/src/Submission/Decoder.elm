module Submission.Decoder exposing (decoder)

import Submission.Model exposing (..)
import Json.Decode exposing (Decoder, string, list)
import Json.Decode.Pipeline exposing (decode, required, optional)


decoder : Decoder Submission
decoder =
    decode Submission
        |> required "abstract" string
        |> required "conferenceId" string
        |> required "format" string
        |> required "id" string
        |> required "intendedAudience" string
        |> optional "keywords" (list string) []
        |> required "language" string
        |> required "outline" string
        |> required "speakers" (list decodeSpeaker)
        |> required "status" string
        |> required "title" string


decodeSpeaker : Decoder Speaker
decodeSpeaker =
    decode Speaker
        |> required "bio" string
        |> required "email" string
        |> required "id" string
        |> required "name" string
