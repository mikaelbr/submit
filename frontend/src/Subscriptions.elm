module Subscriptions exposing (subscriptions)

import Model.Submission exposing (..)
import Messages exposing (..)
import Time exposing (every, second)
import Backend.Network exposing (RequestStatus(..))
import Ports exposing (fileUploadSucceeded, UploadedImageData)


subscriptions : Model -> Sub Msg
subscriptions model =
    let
        saveSub =
            every (30 * second) SaveSubmission

        uploadSub =
            fileUploadSucceeded <| (\a -> UpdateSubmission <| FileUploaded a)
    in
        case model.submission of
            Complete submission ->
                if model.autosave && model.dirty && submission.editable then
                    Sub.batch [ saveSub, uploadSub ]
                else
                    uploadSub

            _ ->
                uploadSub
