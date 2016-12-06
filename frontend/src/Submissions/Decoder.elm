module Submissions.Decoder exposing (decoder)

import Submissions.Model exposing (Model, Year, Submission)
import Json.Decode exposing (Decoder, string, field, map, map2, int, list)


decoder : Decoder Model
decoder =
    map Model <| field "years" (list decodeYear)


decodeYear : Decoder Year
decodeYear =
    map2 Year
        (field "year" string)
        (field "submissions" (list decodeSubmission))


decodeSubmission : Decoder Submission
decodeSubmission =
    map2 Submission
        (field "id" int)
        (field "title" string)
