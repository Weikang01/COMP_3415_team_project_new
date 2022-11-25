
function create_new_message_card(message_text, id, firstname, lastname) {
    return `
<div class="message_card resident_${id}">
    <div class="message_card_text">${message_text}</div>
    <div class="message_from">
        <div class="message_status_dot status_unread"></div>
        <span class="firstname">${firstname}</span> <span class="lastname">${lastname}</span>
    </div>
</div>
    `
}

$(document).ready(function () {
    let doctor_id = $("#doctor_id_holder").val();
    let floatingWindowList = $(".floating_window_list");

    let contactorCounter = localStorage_contactorCounter(doctor_id);

    for (let i = 0; i < contactorCounter; i++) {
        let contactorId = localStorage_getContactor(doctor_id, i);
        let countFromId = localStorage_getCountFromId(doctor_id, contactorId);

        if (countFromId !== 0) {
            let msg_card = $(`.message_card.resident_${contactorId}`);
            let dataJson = localStorage_getMessage(doctor_id, contactorId, countFromId - 1);

            if (msg_card.length === 0) {
                $("#message_card_list").append(create_new_message_card(
                    dataJson.message, dataJson.fromId, dataJson.firstname, dataJson.lastname));
                $(`.message_card.resident_${dataJson.fromId}`).on("click", function () {
                    window.open('/chat/from_doc?doctor_id=' + doctor_id + '&resident_id=' + dataJson.fromId,
                        'newwindow', 'height=600, width=600, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
                });
            } else {
                msg_card.find(".message_card_text").text(dataJson.message);
            }
        }
    }

    var websocket;
    if (typeof (WebSocket) == "undefined") {
        alert("Sorry, your browser does not support Web Socket");
    } else {
        websocket = new WebSocket("ws://" + document.location.host + "/main");


    }

    websocket.onerror = function () {
    }

    websocket.onopen = function () {
    }

    websocket.onmessage = function (event) {
        var dataStr = event.data;
        let dataJson = JSON.parse(dataStr);

        if (dataJson.system) {
            if (dataJson.message === "new") {
                
            } else if (dataJson.message === "make_appointment") {
                let msg;
                let $window = null;
                switch (dataJson.type) {
                    case "new":
                        msg = `You received a new appointment request in ${dataJson.date} ${dataJson.hour}:${dataJson.min}!`;
                        $window = $(create_appointment_floating_message_window(msg)).prependTo(floatingWindowList);
                        break;
                    case "cancel":
                        msg = `Appointment with ${dataJson.firstname} ${dataJson.lastname} in ${dataJson.date} ${dataJson.hour}:${dataJson.min} has been cancelled by patient!`;
                        $window = $(create_appointment_floating_message_window(msg)).prependTo(floatingWindowList);
                        break;
                }

                if ($window != null) {
                    $window.animate({left:"-= 250px"}, 0);
                    $window.find(".floating_window_close").on("click", function () {
                        $window.animate({left:"+= 250px"}, 0, function () {
                            $window.remove();
                        })
                    })
                }
            }
        } else {
            let msg_card = $(`.message_card.resident_${dataJson.fromId}`);

            let date = new Date();
            localStorage_setMessage(
                LOCALSTORAGE_FALSE, doctor_id, dataJson.fromId, dataJson.firstname, dataJson.lastname,
                dataJson.message, date.getHours(), date.getMinutes());

            if (msg_card.length === 0) {
                $("#message_card_list").append(create_new_message_card(
                    dataJson.message, dataJson.fromId, dataJson.firstname, dataJson.lastname));
                $(`.message_card.resident_${dataJson.fromId}`).on("click", function () {
                    window.open('/chat/from_doc?doctor_id=' + doctor_id + '&resident_id=' + dataJson.fromId,
                        'newwindow', 'height=600, width=600, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
                });
                localStorage_addNewContactor(doctor_id, dataJson.fromId);
            } else {
                msg_card.find(".message_card_text").text(dataJson.message);
            }
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