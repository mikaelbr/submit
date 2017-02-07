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
    , i : Int
    }


type alias UploadedImageData =
    { url : String
    , submission : String
    , speaker : String
    , i : Int
    }


port fileSelected : ImagePostData -> Cmd msg


port fileUploadSucceeded : (UploadedImageData -> msg) -> Sub msg


port fileUploadFailed : (String -> msg) -> Sub msg
