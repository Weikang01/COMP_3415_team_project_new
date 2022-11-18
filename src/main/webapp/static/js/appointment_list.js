
$(document).ready(function () {
    let $appointmentList = $("#appointment_list");
    let resident_id = $("#resident_id").val();
    let doctor_id = $("#doctor_id").val();
    let is_resident = resident_id !== 0;


    function cancel_button_click() {
        $.ajax({
        })
    }

    let appointmentStatusList;

    function create_appointment_card(val, is_resident) {
        return `
  <div class="appointment_card">
    <div>
      <span>${is_resident? "Dr. ": ""} ${is_resident? val.doctor.firstname : val.resident.firstname}</span> <span>${is_resident? val.doctor.lastname : val.resident.lastname}</span>
    </div>
    <div>
      <span>${val.day}/${val.month}/${val.year} ${val.hour}:${val.min}</span>
    </div>
    <div>
      status: <span>${appointmentStatusList[val.status].name}</span>
    </div>
    <div>
      <span style="background: #f5d9d5" class="cancel_button" id="cancel${val.id}">cancel</span>
      <span style="background: #f5d9d5" class="confirm_button" id="confirm${val.id}">confirm</span>
    </div>
  </div>`
    }
    function getAppointmentStatusList() {
        $.ajax({
            type: "GET",
            url: "/appointmentDAO?item=getAppointmentStatusList",
            success: function (resp) {
                appointmentStatusList = JSON.parse(resp);
                console.log(appointmentStatusList);
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
})