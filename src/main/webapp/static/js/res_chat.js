const Request_For_Health_Info = "Request_For_Health_Info";

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

function health_request(user_firstName, user_lastName) {
    return `
<div class="container">
  <div class="name">Dr. ${user_firstName} ${user_lastName} sent a request to see your patient history form</div>
  <div class="msger-send-btn accept_button" style="display: inline-block; width: 40%">Accept</div>
  <div class="msger-send-btn reject_button" style="display: inline-block; width: 40%">Reject</div>
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

function appointment_box(json) {
    return `
<div class="container darker">
  <p class="message"><div>Doctor booked an appointment for you</div></p>
  <div><b>Reason</b>: ${json.reason}</div>
  <div><b>Appointment time</b>: ${json.hour}:${json.min} ${json.date}</div>
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
        if (message_json.message === Request_For_Health_Info) {
            // let health_request_element = $(health_request(message_json.firstname, message_json.lastname)).appendTo(chatList);
            // health_request_element.find(".accept_button").on("click", function () {
            //
            // })
        }
        else if (message_json.fromSelf === LOCALSTORAGE_TRUE) {
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

            if (dataJson.message === Request_For_Health_Info)
            {
                let health_request_element = $(health_request(dataJson.firstname, dataJson.lastname)).appendTo(chatList);
                health_request_element.find(".accept_button").on("click", function () {
                    let json = {"toId": doctor_id, "toResident": false, "message": Request_For_Health_Info + "accept"};
                    websocket.send(JSON.stringify(json));
                    health_request_element.html("You sent your health info to the doctor!");
                });
                health_request_element.find(".reject_button").on("click", function () {
                    let json = {"toId": doctor_id, "toResident": false, "message": Request_For_Health_Info + "reject"};
                    websocket.send(JSON.stringify(json));
                    health_request_element.html("You rejected from sending your health info to the doctor!");
                });
            }
            else {
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
                localStorage_addNewContactor(doctor_id, resident_id);
            }
        }
        else {
            if (dataJson.message === "make_appointment") {
                if (dataJson.type === "new") {
                    $(appointment_box(dataJson)).appendTo(chatList);
                }
            }
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
        let json = {"toId": doctor_id, "toResident": false, "message": message};
        websocket.send(JSON.stringify(json));
        localStorage_setMessage(LOCALSTORAGE_TRUE, resident_id, doctor_id,
            null, null, message, date.getHours(), date.getMinutes());
    }

    $("#send_message").on("click", send_message);
})