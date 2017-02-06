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

        // TODO: fix correct token and correct URL

        var myHeaders = new Headers();
        myHeaders.append("x-token", "c3e28586-4427-454c-b623-7ababaaa6bf1");

        fetch('https://submit.javazone.no/api/submissions/22db550f32394928b35198e3f146d2b0/speakers/411d19199e8a47e08b1c6d8679c75b6f/picture', {
                method: 'POST',
                body: data,
                headers: myHeaders
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
