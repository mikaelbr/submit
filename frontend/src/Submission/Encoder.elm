module Submission.Encoder exposing (encoder)

import Submission.Model exposing (..)
import Json.Encode exposing (Value, object, string, list)


encoder : Submission -> Value
encoder submission =
    object
        [ ( "status", string submission.status )
        , ( "title", string submission.title )
        , ( "abstract", string submission.abstract )
        , ( "intendedAudience", string submission.intendedAudience )
        , ( "equipment", string submission.equipment )
        , ( "format", string submission.format )
        , ( "language", string submission.language )
        , ( "outline", string submission.outline )
        , ( "speakers", list <| List.map encodeSpeaker submission.speakers )
        ]


encodeSpeaker : ( Int, Speaker ) -> Value
encodeSpeaker ( i, speaker ) =
    object
        [ ( "id", string speaker.id )
        , ( "name", string speaker.name )
        , ( "email", string speaker.email )
        , ( "bio", string speaker.bio )
        , ( "zipCode", string speaker.zipCode )
        ]
