module Submissions.Model exposing (Model, Year, Submission, initModel, Submissions)

import Backend.Network exposing (RequestStatus(..))


type alias Model =
    { submissions : RequestStatus Submissions
    }


type alias Submissions =
    { years : List Year
    }


type alias Year =
    { year : String
    , submissions : List Submission
    }


type alias Submission =
    { id : String
    , name : String
    , status: String
    }


initModel : Model
initModel =
    Model Initial
