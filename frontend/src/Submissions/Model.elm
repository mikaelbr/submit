module Submissions.Model exposing (Model, Year, Submission, initModel)


type alias Model =
    { years : List Year
    }


type alias Year =
    { year : Int
    , submissions : List Submission
    }


type alias Submission =
    { id : String
    , name : String
    }


initModel : Model
initModel =
    Model []
