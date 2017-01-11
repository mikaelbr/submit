module Submission.Model exposing (Model, initModel, Submission, Speaker)

import Backend.Network exposing (RequestStatus(..))
import Time


type alias Model =
    { submission : RequestStatus Submission
    , lastSaved : Maybe Time.Time
    }


type alias Submission =
    { abstract : String
    , conferenceId : String
    , format : String
    , id : String
    , intendedAudience : String
    , language : String
    , outline : String
    , speakers : List Speaker
    , status : String
    , title : String
    }


type alias Speaker =
    { bio : String
    , email : String
    , id : String
    , name : String
    }


initModel : Model
initModel =
    Model Initial Nothing
