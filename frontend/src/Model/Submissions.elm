module Model.Submissions exposing (Model, Year, Submission, initModel, Submissions)

import Backend.Network exposing (RequestStatus(..))
import Model.Submission exposing (Comment)


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
    , status : String
    , comments : List Comment
    }


initModel : Model
initModel =
    Model Initial
