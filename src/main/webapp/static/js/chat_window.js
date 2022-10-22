$(document).ready(function () {
    var username;

    $(function () {
        $.ajax({
            url:"getUsername",
            success: function (res) {
                username = res;
            },
            async: false
        })
    })

    var input = $("#message_box");
    input.on("keypress", function(e) {
        if (e.which == 13) {
            $.get("login", $(""))
        }
    })

    var websocket;
    if (typeof (WebSocket) == "undefined") {
        alert("Sorry, your browser does not support Web Socket");
    } else {
        websocket = new WebSocket("ws://" + document.location.host + "/chat");
    }

    websocket.onerror = function () {
    }

    websocket.onopen = function () {
        // logout
    }

    websocket.onmessage = function (event) {
        var dataStr = event.data;
        let dataJson = JSON.parse(dataStr);
        if (dataJson.isSystem) {

        } else {

        }
    }

    websocket.onclose = function () {
        // logout
    }

    window.onbeforeunload = function () {
        closeWebSocket();
    }

    function closeWebSocket() {
        websocket.close();
    }
})