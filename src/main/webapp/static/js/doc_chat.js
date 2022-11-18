function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

function message_from_current_user(user_message, hours, minutes) {
    return `
<div class="container">
  <div class="name">Me</div>
  <p class="message">${user_message}</p>
  <span class="time-right">${hours}:${minutes}</span>
</div>`
}

function message_from_another_user(user_firstName, user_lastName, user_message, hours, minutes) {
    return `
<div class="container darker">
  <div class="name">Dr. ${user_firstName} ${user_lastName}</div>
  <p class="message">${user_message}</p>
  <span class="time-right">${hours}:${minutes}</span>
</div>`
}

$(document).ready(function () {
    let chatList = $("#chat_list");
    let resident_id = getUrlParam("resident_id");
    let doctor_id = getUrlParam("doctor_id");

    let msg_count = localStorage_getCountFromId(doctor_id, resident_id);
    console.log("current msg_count: " + msg_count);

    for (let i = 0; i < msg_count; i++) {
        let message_json = localStorage_getMessage(doctor_id, resident_id, i);
        if (message_json.fromSelf === LOCALSTORAGE_TRUE) {
            chatList.append(
                message_from_current_user(message_json.message, message_json.hours, message_json.minutes));
        } else {
            chatList.append(
                message_from_another_user(message_json.firstname, message_json.lastname,
                    message_json.message, message_json.hours, message_json.minutes));
        }
    }

    var websocket;
    if (typeof (WebSocket) == "undefined") {
        alert("Sorry, your browser does not support Web Socket");
    } else {
        websocket = new WebSocket("ws://" + document.location.host + window.location.pathname + window.location.search);
    }

    websocket.onerror = function () {
    }

    websocket.onopen = function () {
    }

    websocket.onmessage = function (event) {
        var dataStr = event.data;
        let dataJson = JSON.parse(dataStr);
        console.log(dataJson);
        if (dataJson.system) {

        } else {
            let date = new Date();
            localStorage_setMessage(LOCALSTORAGE_FALSE, doctor_id, resident_id, dataJson.firstname,
                dataJson.lastname, dataJson.message, date.getHours(), date.getMinutes());
            chatList.append(message_from_another_user(dataJson.firstname, dataJson.lastname,
                dataJson.message, date.getHours(), date.getMinutes()));
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

    let messageText = $("#message_text");

    $("#send_message").on("click", function (e) {
        let message = messageText.val();
        chatList.append(message_from_current_user(message));
        messageText.val("");
        var json = {"toId": resident_id, "toResident": true, "message": message};

        let date = new Date();
        localStorage_setMessage(LOCALSTORAGE_TRUE, doctor_id, resident_id, null,
            null, message, date.getHours(), date.getMinutes());
        websocket.send(JSON.stringify(json));
    });

    $("#make_appointment").on("click", function (e) {
        window.open('/make_appointment?resident_id='+ resident_id +'&doctor_id=' + doctor_id, '_blank', 'height=80, width=500, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
    })
})