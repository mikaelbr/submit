module Submissions.Model exposing (Model, initModel)

import Submissions.Messages exposing (..)


type alias Model =
    { entry : String
    }


initModel : Model
initModel =
    Model ""
