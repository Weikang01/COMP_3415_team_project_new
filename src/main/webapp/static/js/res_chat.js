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
    var date = new Date();
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

    var input = $("#message_box");
    
    let msg_count = localStorage_getCountFromId(resident_id, doctor_id);
    for (let i = 0; i < msg_count; i++) {
        let message_json = localStorage_getMessage(resident_id, doctor_id, i);
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
        // logout
    }

    websocket.onmessage = function (event) {
        var dataStr = event.data;
        let dataJson = JSON.parse(dataStr);
        if (!dataJson.system) {
            let date = new Date;

            localStorage.setItem("", JSON.stringify({
                "firstname": dataJson.firstname,
                "lastname": dataJson.lastname,
                "hours": date.getHours()
            }))

            chatList.append(
                message_from_another_user(
                    dataJson.firstname, dataJson.lastname, dataJson.message,
                    date.getHours(), date.getMinutes()));
            localStorage_setMessage(LOCALSTORAGE_FALSE, resident_id, doctor_id,
                dataJson.firstname, dataJson.lastname, dataJson.message,date.getHours(), date.getMinutes());
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

    let messageText = $("#message_text");

    function send_message(e) {
        let message = messageText.val();
        let date = new Date();

        chatList.append(message_from_current_user(message, date.getHours(), date.getMinutes()));
        messageText.val("");
        var json = {"toId": doctor_id, "toResident": false, "message": message};
        websocket.send(JSON.stringify(json));
        localStorage_setMessage(LOCALSTORAGE_TRUE, resident_id, doctor_id,
            null, null, message, date.getHours(), date.getMinutes());
    }

    $("#send_message").on("click", send_message);
})