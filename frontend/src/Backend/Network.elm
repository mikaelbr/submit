module Backend.Network exposing (RequestStatus(..))


type RequestStatus a
    = Initial
    | Loading
    | Complete a
    | Error String
