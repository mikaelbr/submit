module Model exposing (Model, Flags, AppConfig, initModel)

import Model.Login
import Nav.Model exposing (Page(..))
import Model.Submissions
import Model.Submission


type alias Flags =
    { host : String
    , token : String
    }


type alias AppConfig =
    { host : String
    , submissionsOpen : Bool
    , token : String
    }


type alias Model =
    { login : Model.Login.Model
    , submissions : Model.Submissions.SubmissionsModel
    , submission : Model.Submission.Model
    , page : Page
    , appConfig : AppConfig
    }


initModel : Flags -> Page -> Model
initModel flags page =
    Model
        Model.Login.init
        Model.Submissions.initModel
        Model.Submission.initModel
        page
        (initAppConfig flags)


initAppConfig : Flags -> AppConfig
initAppConfig flags =
    AppConfig flags.host True flags.token
