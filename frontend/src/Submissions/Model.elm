module Submissions.Model exposing (Model, Year, Submission, initModel)


type alias Model =
    { years : List Year
    }


type alias Year =
    { year : String
    , submissions : List Submission
    }


type alias Submission =
    { id : Int
    , name : String
    }


initModel : Model
initModel =
    Model []
