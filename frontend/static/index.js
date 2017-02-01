(function() {
    var app = Elm.Main.fullscreen();

    function checkStatus(response) {
        if (response.status >= 200 && response.status < 300) {
            return response;
        } else {
            var error = new Error(response.statusText);
            error.response = response;
            throw error;
        }
    }

    function parseJson(response) {
        return response.json();
    }

    app.ports.fileSelected.subscribe(function(id) {
        var input = document.getElementById(id);
        if (input == null) {
            return;
        }

        var data = new FormData();
        data.append('image', input.files[0]);

        fetch('/image', {
                method: 'POST',
                body: data
            })
            .then(checkStatus)
            .then(parseJson)
            .then(function(data) {
                app.ports.fileUploadSucceeded.send(data);
            })
            .catch(function(error) {
                app.ports.fileUploadFailed.send(error.message);
            });
    });
})();
