port module Ports exposing (..)


type alias MediaPortData =
    { contents : String
    , filename : String
    }


type alias FileData =
    { location : String
    , filetype : String
    }


type alias ImagePostData =
    { id : String
    , submission : String
    , speaker : String
    }


port fileSelected : ImagePostData -> Cmd msg


port fileUploadSucceeded : (FileData -> msg) -> Sub msg


port fileUploadFailed : (String -> msg) -> Sub msg
