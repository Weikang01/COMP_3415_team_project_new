$(document).ready(function () {
    let res_username = $("#resident_username_holder").val();

    // setInterval(function () {
    //     _online_checker(res_username, "res");
    // }, 20000);

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

    $(".consult_button").each(function (index) {
        $(this).on("click", function () {
            let doctor_id = $(this).find("input[name='doctor_id']").val();
            window.open('/chat?doctor_id=' + doctor_id, 'newwindow', 'height=600, width=600, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
        })
    })
    $(".appointment_button").each(function (index) {
        $(this).on("click", function () {

        })
    })
})