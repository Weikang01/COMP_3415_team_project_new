const Request_For_Health_Info = "Request_For_Health_Info";

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

function create_health_request() {
    return "<div>You sent a health info request to the resident..</div>";
}

function resident_reject_health_request() {
    return "<div>Resident rejected your request of sharing their health info..</div>";
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
  <div class="name">${user_firstName} ${user_lastName}</div>
  <p class="message">${user_message}</p>
  <span class="time-right">${hours}:${minutes}</span>
</div>`
}

function create_patient_history_form_content(user_firstName, user_lastName, json) {
    return `
    <h4>Patient History</h4>
    <table style="width:100%">
    <tr>
        <td>had ECT</td>
        <td>${json.had_ECT}</td>
    </tr>
    <tr>
        <td>Had psychotherapy</td>
        <td>${json.had_psychotherapy}</td>
    </tr>
    <tr>
        <td>Drug allergy</td>
        <td>${json.had_drug_allergy}</td>
    </tr>
    <tr>
        <td>to what</td>
        <td>${json.drug_allergy}</td>
    </tr>
    </table>
    `

}

function create_family_history_form_content(i, json) {
    return `
<h4>Family member ${i+1}</h4>
<table style="width:100%">
<tr>
<td>Relationship</td>
<td>${json.relationship}</td>
</tr>
<tr>
<td>Age</td>
<td>${json.age}</td>
</tr>
<tr>
<td>Health conditions</td>
<td>${json.health_condition}</td>
</tr>
<tr>
<td>Is Deceased</td>
<td>${json.is_deceased}</td>
</tr>
<tr>
<td>Age(s) at death</td>
<td>${json.death_age}</td>
</tr>
<tr>
<td>Cause</td>
<td>${json.death_cause}</td>
</tr>
</table>
    `;
}

function empty_container(user_firstName, user_lastName) {
    return `
<div class="container">
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
        if (message_json.message === Request_For_Health_Info) {
            chatList.append(create_health_request());
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
    }

    websocket.onmessage = function (event) {
        var dataStr = event.data;
        let dataJson = JSON.parse(dataStr);
        console.log(dataJson);
        if (dataJson.system) {

        } else {
            let health_info = $(empty_container()).appendTo(chatList);
            console.log(dataJson.message);
            if (dataJson.message.startsWith(Request_For_Health_Info)) {
                let suffix = dataJson.message.substring(Request_For_Health_Info.length);
                if (suffix === "accept") {
                    $.ajax({
                        type: "GET",
                        url: "/PatientHistoryFormDAOServlet?resident_id=" + resident_id,
                        dataType: "html",
                        success: function (resp) {
                            let json = JSON.parse(resp);
                            $(create_patient_history_form_content(dataJson.firstname, dataJson.lastname, json)).appendTo(health_info);
                        },
                        async:false
                    })

                    $.ajax({
                        type: "GET",
                        url: "/FamilyHistoryFormDAOServlet?item=getFamilyHistoryFormList&resident_id=" + resident_id,
                        dataType: "html",
                        success: function (resp) {
                            console.log(resp);
                            let json = JSON.parse(resp);
                            let cur_family_form_num = json.length;
                            for (let i = 0; i < cur_family_form_num; i++) {
                                $(create_family_history_form_content(i, json[i])).appendTo(health_info);
                            }
                        },
                        async:false
                    })
                } else {
                    $(resident_reject_health_request()).appendTo(chatList);
                }
            } else {
                let date = new Date();
                localStorage_setMessage(LOCALSTORAGE_FALSE, doctor_id, resident_id, dataJson.firstname,
                    dataJson.lastname, dataJson.message, date.getHours(), date.getMinutes());
                chatList.append(message_from_another_user(dataJson.firstname, dataJson.lastname,
                    dataJson.message, date.getHours(), date.getMinutes()));
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

    let messageText = $("#message_text");

    $("#send_message").on("click", function (e) {
        let message = messageText.val();
        chatList.append(message_from_current_user(message));
        messageText.val("");
        let json = {"toId": resident_id, "toResident": true, "message": message};

        let date = new Date();
        localStorage_setMessage(LOCALSTORAGE_TRUE, doctor_id, resident_id, null,
            null, message, date.getHours(), date.getMinutes());
        websocket.send(JSON.stringify(json));
    });

    $("#make_appointment").on("click", function (e) {
        window.open('/make_appointment?resident_id='+ resident_id +'&doctor_id=' + doctor_id, '_blank', 'height=80, width=500, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
    });

    $("#health_info").on("click", function (e) {
        $("<div>You sent a health info request to the resident..</div>").appendTo(chatList);
        let json = {"toId": resident_id, "toResident": true, "message": Request_For_Health_Info};

        let date = new Date();
        websocket.send(JSON.stringify(json));

        localStorage_setMessage(LOCALSTORAGE_TRUE, doctor_id, resident_id, null,
            null, Request_For_Health_Info, date.getHours(), date.getMinutes());
    })
})