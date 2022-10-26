$(document).ready(function () {
    var websocket;
    if (typeof (WebSocket) == "undefined") {
        alert("Sorry, your browser does not support Web Socket");
    } else {
        websocket = new WebSocket("ws://" + document.location.host + "/main");
    }

    websocket.onerror = function () {

    }

    websocket.onopen = function () {
        // logout
    }

    websocket.onmessage = function (event) {
        var dataStr = event.data;
        let dataJson = JSON.parse(dataStr);
        console.log(dataJson);
        if (dataJson.isSystem) {
            if (dataJson.message === "new") {
                
            }
        } else {

        }
    }

    websocket.onclose = function () {
    }

    window.onbeforeunload = function () {
        closeWebSocket();
    }

    function closeWebSocket() {
        websocket.close();
    }
})