
$(document).ready(function () {

    let appointmentStatusList;

    let $appointmentList = $("#appointment_list");
    let resident_id = $("#resident_id").val();
    let doctor_id = $("#doctor_id").val();
    let firstname = $("#firstname").val();
    let lastname = $("#lastname").val();
    let floatingWindowList = $("#floating_window_list");
    let is_resident = (resident_id !== "0");

    var websocket;
    if (typeof (WebSocket) == "undefined") {
        alert("Sorry, your browser does not support Web Socket");
    } else {
        websocket = new WebSocket("ws://" + document.location.host +
            `/make_appointment/q?resident_id=${resident_id}&doctor_id=${doctor_id}`);
    }
    websocket.onopen = function () {}
    websocket.onmessage = function (event) {
        var dataStr = event.data;
        let dataJson = JSON.parse(dataStr);
        if (dataJson.system) {
            if (dataJson.message === "make_appointment") {
                let msg;
                let $window = null;
                switch (dataJson.type) {
                    case "new":
                        msg = `You received a new appointment request for ${dataJson.reason} in ${dataJson.date} ${dataJson.hour}:${dataJson.min}`;
                        $window = $(create_appointment_floating_message_window(msg)).prependTo(floatingWindowList);
                        break;
                    case "confirm":
                        msg = `Your appointment with Dr. ${dataJson.firstname} ${dataJson.lastname} in ${dataJson.date} ${dataJson.hour}:${dataJson.min} has been confirmed!`;
                        $window = $(create_appointment_floating_message_window(msg)).prependTo(floatingWindowList);
                        break;
                    case "cancel":
                        msg = `Appointment with ${ is_resident? "Dr." : ""} ${dataJson.firstname} ${dataJson.lastname} for ${dataJson.reason} in ${dataJson.date} ${dataJson.hour}:${dataJson.min} has been cancelled by ${is_resident? "doctor":"patient"}!`;
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
        }
    }
    websocket.onclose = function () {}

    let cancel_status, confirmed_status;

    function change_appointment_status(appointment_id, old_status_id, new_status_id, element_id_prefix) {
        $.ajax({
            type: "GET",
            url: "/appointmentDAO?item=changeAppointmentStatus&id=" + appointment_id + "&to_status=" + new_status_id + "&from_status=" + old_status_id,
            success: function (resp) {
                let json = JSON.parse(resp);
                let new_status = parseInt(json.newStatusIndex);
                if (new_status !== old_status_id) {
                    let status_span = $("#status" + appointment_id);
                    status_span.text(appointmentStatusList[new_status].name);
                    $("#button_list" + appointment_id).find(".inactive").each(function (i, el) {
                        el.classList.remove("inactive");
                    });
                    $("#" + element_id_prefix + appointment_id).addClass("inactive");

                    status_span.each(function (i, el) {
                        var name = el.className.match(/status(\d*)$/)[0];
                        el.classList.remove(name);
                        el.classList.add("status" + new_status);
                    });
                }
            }
        });
    }

    function create_appointment_card(val, is_resident) {
        return `
  <div class="appointment_card">
    <div>
      <span>${is_resident? "Dr. ": ""} ${is_resident? val.doctor.firstname : val.resident.firstname}</span> <span>${is_resident? val.doctor.lastname : val.resident.lastname}</span>
    </div>
    <div class="date_div">
      <span class="date_span">${val.day}/${val.month}/${val.year} ${val.hour}:${val.min}</span>
    </div>
    <div class="reason_div">
    <b>Reason: </b>${val.reason}
</div>
    <div class="status_div">
      status: <span class="status_span status${val.status}" id="status${val.id}">${appointmentStatusList[val.status].name}</span>
    </div>
    <div id="button_list${val.id}">
      <span style="background: #f5d9d5" class="cancel_button ${cancel_status ===val.status? 'inactive': ''}" id="cancel${val.id}">cancel</span>
      ` +
        (is_resident? "" : `<span style="background: #f5d9d5" class="confirm_button ${confirmed_status ===val.status? 'inactive': ''}" id="confirm${val.id}">confirm</span>`)
        + `
    </div>
  </div>`
    }

    function getAppointmentStatusList() {
        $.ajax({
            type: "GET",
            url: "/appointmentDAO?item=getAppointmentStatusList",
            success: function (resp) {
                appointmentStatusList = JSON.parse(resp);
                for (let i = 0; i < appointmentStatusList.length; i++) {
                    if (appointmentStatusList[i].name === "cancelled") {
                        cancel_status = appointmentStatusList[i].value;
                    } else if (appointmentStatusList[i].name === "confirmed") {
                        confirmed_status = appointmentStatusList[i].value;
                    }
                }
            },
            async: false
        })
    }

    function getAppointmentList() {
        $.ajax({
            type: "GET",
            url: "/appointmentDAO?item=getAppointmentListByResidentAndDoctor",
            success: function (resp, textStatus, xhr) {
                let json = JSON.parse(resp);
                for (let i = 0; i < json.length; i++) {
                    let appointment_card = $(create_appointment_card(json[i], is_resident)).appendTo($appointmentList);
                    appointment_card.find(".cancel_button").on("click", function () {
                        if ($(this).hasClass("inactive")) {
                            return;
                        }

                        let appointment_id = event.target.id.substring(6);
                        $("#status" + appointment_id).each(function (i, el) {
                            var name = el.className.match(/status(\d*)$/)[1];

                            let date = new Date(json[i].year, json[i].month, json[i].day);
                            const yyyy = date.getFullYear();
                            let mm = date.getMonth() + 1; // Months start at 0!
                            let dd = date.getDate();

                            if (dd < 10) dd = '0' + dd;
                            if (mm < 10) mm = '0' + mm;
                            // yyyy-MM-dd
                            websocket.send(
                                `{
                                "type":"cancel", "date":"${yyyy}-${mm}-${dd}",
                                "hour":${json[i].hour},"min":${json[i].min},
                                "firstname": "${firstname}", "lastname":"${lastname}",
                                "from_resident":"${resident_id !== 0}",
                                "reason": "${json[i].reason}",
                                "toId": ${resident_id === "0"? json[i].resident.id : json[i].doctor.id}
                                }`);
                            change_appointment_status(appointment_id, parseInt(name), cancel_status, "cancel");
                        });
                    })

                    appointment_card.find(".confirm_button").on("click", function () {
                        if ($(this).hasClass("inactive")) {
                            return;
                        }

                        let appointment_id = event.target.id.substring(7);

                        $("#status" + appointment_id).each(function (i, el) {
                            var name = el.className.match(/status(\d*)$/)[1];

                            let date = new Date(json[i].year, json[i].month, json[i].day);
                            const yyyy = date.getFullYear();
                            let mm = date.getMonth() + 1; // Months start at 0!
                            let dd = date.getDate();

                            if (dd < 10) dd = '0' + dd;
                            if (mm < 10) mm = '0' + mm;
                            // yyyy-MM-dd
                            websocket.send(
                                `{
                                "type":"confirm", "date":"${yyyy}-${mm}-${dd}",
                                "hour":${json[i].hour},"min":${json[i].min},
                                "firstname": "${firstname}", "lastname":"${lastname}",
                                "from_resident": ${resident_id !== 0},
                                "reason": "${json[i].reason}",
                                "toId": ${resident_id === "0"? json[i].resident.id : json[i].doctor.id}
                                }`);

                            change_appointment_status(appointment_id, parseInt(name), confirmed_status, "confirm");
                        });
                    })
                }
                // $(".cancel_button").on("click", cancel_button_click);
                // $(".confirm_button").on("click",confirm_button_click);
            },
            complete: function(xhr, textStatus) {
                if (xhr.status === 302) {
                    window.open(xhr.responseText, "_self");
                }
            }
        });
    }

    getAppointmentStatusList();
    getAppointmentList();
    // console.log(appointmentStatusList);
})