function create_family_history_form_content(i, json) {
    return `
              <div class="family_member_form" style="border: 1px solid #EEEEEE">
                <h4>Family member ${i+1}</h4>
                <label for="family_relationship${i}">relationship: </label>
                <input type="text" name="family_relationship${i}" id="family_relationship${i}" value="${json.relationship}">
                
                <label for="family_member_age${i}">Age:
                <input type="text" name="family_member_age${i}" id="family_member_age${i}" value="${json.age}">
                
                <label for="family_member_health_condition${i}">Health conditions: </label>
                <input type="text" name="family_member_health_condition${i}" id="family_member_health_condition${i}" value="${json.health_condition}">
                
                <label for="is_deceased${i}">if deceased:
              <select id="is_deceased${i}" name="is_deceased${i}" style="width: 30%" >
                <option value="false" ${json.is_deceased === "false"? "selected": ""}>No</option>
                <option value="true" ${json.is_deceased === "true"? "selected": ""}>Yes</option>
              </select>
                <label for="death_age${i}">Age(s) at death: </label><input type="text" name="death_age${i}" id="death_age${i}" value="${json.death_age}">
                <label for="death_cause${i}">Cause: </label><input type="text" name="death_cause${i}" id="death_cause${i}" value="${json.death_cause}">
              </div>
    `;
}

function create_family_history_form(i) {
    return `
              <div class="family_member_form" style="border: 1px solid #EEEEEE">
                <h4>Family member ${i+1}</h4>
                <label for="family_relationship${i}">relationship: </label>
                <input type="text" name="family_relationship${i}" id="family_relationship${i}">
                
                <label for="family_member_age${i}">Age:
                <input type="text" name="family_member_age${i}" id="family_member_age${i}">
                
                <label for="family_member_health_condition${i}">Health conditions: </label>
                <input type="text" name="family_member_health_condition${i}" id="family_member_health_condition${i}">
                
                <label for="is_deceased${i}">if deceased:
              <select id="is_deceased${i}" name="is_deceased${i}" style="width: 30%">
                <option value="false">No</option>
                <option value="true">Yes</option>
              </select>
                
                <label for="death_age${i}">Age(s) at death: </label><input type="text" name="death_age${i}" id="death_age${i}">
                <label for="death_cause${i}">Cause: </label><input type="text" name="death_cause${i}" id="death_cause${i}">
              </div>
    `;
}

$(document).ready(function () {
    let resident_id = $("#resident_id").val();
    let familyMemberFormList = $("#family_member_form_list");

    let cur_family_form_num = 0;
    $.ajax({
        type: "GET",
        url: "/FamilyHistoryFormDAOServlet?item=getFamilyHistoryFormList&resident_id=" + resident_id,
        dataType: "html",
        success: function (resp) {
            console.log(resp);
            let json = JSON.parse(resp);
            cur_family_form_num = json.length;
            for (let i = 0; i < cur_family_form_num; i++) {
                $(create_family_history_form_content(i, json[i])).appendTo(familyMemberFormList);
            }
        },
        async: false
    })

    // family history form
    $("#add_family_history_form_button").on("click", function () {
        $(create_family_history_form(cur_family_form_num)).appendTo(familyMemberFormList);
        cur_family_form_num++;
    });

    $("#sub_family_history_form_button").on("click", function () {
        if (cur_family_form_num>0) {
            familyMemberFormList.children().last().remove();
            cur_family_form_num--;
        }
    })
})