module Submission.Decoder exposing (decoder)

import Submission.Model exposing (..)
import Json.Decode exposing (Decoder, string, list, map, bool)
import Json.Decode.Pipeline exposing (decode, required, optional)


decoder : Decoder Submission
decoder =
    decode Submission
        |> required "abstract" string
        |> required "conferenceId" string
        |> required "equipment" string
        |> required "format" string
        |> required "id" string
        |> required "intendedAudience" string
        |> required "language" string
        |> optional "length" string "60"
        |> required "outline" string
        |> required "speakers" (map toTuples <| list decodeSpeaker)
        |> required "status" string
        |> required "title" string
        |> required "editable" bool


toTuples : List Speaker -> List ( Int, Speaker )
toTuples speakers =
    List.map2 (,) (List.range 0 (List.length speakers)) speakers


decodeSpeaker : Decoder Speaker
decodeSpeaker =
    decode Speaker
        |> required "bio" string
        |> required "email" string
        |> required "id" string
        |> required "name" string
        |> optional "zipCode" string ""
