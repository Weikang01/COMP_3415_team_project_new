
$(document).ready(function () {
    let appointmentStatusList;

    let $appointmentList = $("#appointment_list");
    let resident_id = $("#resident_id").val();
    let is_resident = resident_id !== 0;
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

    function cancel_button_click(event) {
        if ($(this).hasClass("inactive")) {
            return;
        }

        let appointment_id = event.target.id.substring(6);
        $("#status" + appointment_id).each(function (i, el) {
            var name = el.className.match(/status(\d*)$/)[1];
            change_appointment_status(appointment_id, parseInt(name), cancel_status, "cancel");
        });
    }

    function confirm_button_click(event) {
        if ($(this).hasClass("inactive")) {
            return;
        }

        let appointment_id = event.target.id.substring(7);

        $("#status" + appointment_id).each(function (i, el) {
            var name = el.className.match(/status(\d*)$/)[1];
            change_appointment_status(appointment_id, parseInt(name), confirmed_status, "confirm");
        });
    }

    function create_appointment_card(val, is_resident) {
        return `
  <div class="appointment_card">
    <div>
      <span>${is_resident? "Dr. ": ""} ${is_resident? val.doctor.firstname : val.resident.firstname}</span> <span>${is_resident? val.doctor.lastname : val.resident.lastname}</span>
    </div>
    <div>
      <span>${val.day}/${val.month}/${val.year} ${val.hour}:${val.min}</span>
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
                    $appointmentList.append(create_appointment_card(json[i], is_resident));
                }
                $(".cancel_button").on("click", cancel_button_click);
                $(".confirm_button").on("click",confirm_button_click);
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