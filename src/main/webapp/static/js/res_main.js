function generate_doctor_card(doctor, distance = 0.0, online=false) {
    return `<div class="doctor_card" id=${doctor.id}>
        <h3><span>${doctor.firstname}</span> <span>${doctor.lastname}</span></h3>
        <div>Specialty:
            <span class="spec_border" style="display: ${(doctor.specialty1_id === 0?'inline':'none')}" >${doctor.specialty1}</span>
            <span class="spec_border" style="display: ${(doctor.specialty2_id === 0?'inline':'none')}" >${doctor.specialty2}</span>
            <span class="spec_border" style="display: ${(doctor.specialty3_id === 0?'inline':'none')}" >${doctor.specialty3}</span>
            <span class="spec_border" style="display: ${(doctor.specialty4_id === 0?'inline':'none')}" >${doctor.specialty4}</span>
            <span class="spec_border" style="display: ${(doctor.specialty5_id === 0?'inline':'none')}" >${doctor.specialty5}</span>
        </div>
        <div>Organization: <span>${doctor.hospital}</span></div>
        <div>Distance: <span></span>${distance} km</div>
        <div>Status: <span>${(online ? 'online' : 'offline')}</span> </div>
        <div class="consult_button" style="background: aquamarine">
            <input type="hidden" name="doctor_id" value="${doctor.id}">
            Consult Online!</div>
        <div style="background: darkturquoise" class="appointment_button">
            <input type="hidden" name="doctor_id" value="${doctor.id}">
            Make an Appointment</div>
    </div>`;
}

$(document).ready(function () {
    let res_username = $("#resident_username_holder").val();
    let res_id = $("#resident_id_holder").val();
    let current_doctors_on_list;

    $.get({
        url: "/docdao",
        dataType: "html",
        data: "item=getDoctorsNearCoord",
        success: function (resp) {
            let dataJson = JSON.parse(resp);
            console.log(dataJson.length);

        },
        async: false
    })

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
        if (dataJson.system) {
            if (dataJson.message === "new") {
                console.log("hello world");
            } else if (typeof (dataJson.message) === typeof([])) {

                console.log("length: " + dataJson.message.length);
                for (let i = 0; i < dataJson.message.length; i++) {
                    console.log(dataJson.message["_" + i]);
                }
            }
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

            let doctor_id = $(this).find("input[name='doctor_id']").val();
            window.open('/make_appointment?resident_id='+ res_id +'&doctor_id=' + doctor_id, 'newwindow', 'height=80, width=500, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
        })
    })
})