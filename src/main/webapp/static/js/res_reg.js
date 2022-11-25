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
    let submit_button = $("input#submit");

    var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#) (\$)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\+)(\=) (\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/) (\<)(\>)(\?)(\)]+/);

    let username_checked = false;
    let password01_checked = false;
    let password02_checked = false;
    let birthdate_checked = false;

    submit_button.attr("disabled","true");

    function setSubmitActive() {
        if (username_checked && password01_checked && password02_checked && birthdate_checked) {
            submit_button.removeAttr("disabled");
        }
    }

    let min_username_length = 5;
    let max_username_length = 30;
    // username checking
    {
        let input_username = $("#username");
        let text_username = $("#username_text");
        text_username.hide();

        function invalid_username_input_display(text) {
            text_username .show();
            text_username .text(text);
            input_username.removeClass("input_waiting");
            input_username.removeClass("input_valid");
            input_username.addClass("input_invalid");
            username_checked = false;
            submit_button.attr("disabled","true");
        }

        function valid_username_input_display(text) {
            text_username .show();
            text_username .text(text);
            input_username.removeClass("input_waiting");
            input_username.removeClass("input_invalid");
            input_username.addClass("input_valid");
            username_checked = true;
        }

        input_username.on("blur", function () {
            if (input_username.val().length < min_username_length)
            {
                invalid_username_input_display("Username must has at least " + min_username_length + " characters!");
            }
            else if (input_username.val().length > max_username_length) {
                invalid_username_input_display("Username must has at most " + max_username_length + " characters!")
            }
            else if (containSpecial.test(input_username.val())) {
                invalid_username_input_display("Username cannot contain special characters! (~！@#^&*(),.:;/\\'\"-_=+[]{}");
            }
            else {
                $.ajax({
                    type: "GET",
                    url: "/username_checking",
                    dataType: "html",
                    data: "username=" + input_username.val() + "&db=res",
                    beforeSend: function () {
                        text_username.show();
                        text_username.text("please wait...")
                        input_username.removeClass("input_invalid");
                        input_username.removeClass("input_valid");
                        input_username.addClass("input_waiting");
                        submit_button.attr("disabled","true");
                        username_checked = false;
                    },
                    success: function (resp) {
                        if (resp === "0") {
                            valid_username_input_display("This is a valid username!");

                        }
                        else {
                            invalid_username_input_display("This username is already taken!");
                        }
                    }
                })
            }
        })
    }

    let min_password_length = 5;
    let max_password_length = 30;
    // password checking
    {
        let password_input = $("#password");
        let password_text = $("#password_text");
        let confirm_password_input = $("#confirm_password");
        let confirm_password_text = $("#confirm_password_text");

        password_text.hide();
        confirm_password_text.hide();

        function invalid_confirm_password_input_display(text) {
            confirm_password_text .show();
            confirm_password_text .text(text);
            confirm_password_input.removeClass("input_waiting");
            confirm_password_input.removeClass("input_valid");
            confirm_password_input.addClass("input_invalid");
            password02_checked = false;
            submit_button.attr("disabled","true");
        }

        function valid_confirm_password_input_display(text) {
            confirm_password_text .show();
            confirm_password_text .text(text);
            confirm_password_input.removeClass("input_waiting");
            confirm_password_input.removeClass("input_invalid");
            confirm_password_input.addClass("input_valid");
            password02_checked = true;
            setSubmitActive();
        }

        function confirm_password_input_display() {
            if (confirm_password_input.val() === "") {
                invalid_confirm_password_input_display("Password cannot be empty!");
            }
            else if (password_input.val() !== confirm_password_input.val()) {
                invalid_confirm_password_input_display("Confirm password and password are not the same!");
            } else {
                valid_confirm_password_input_display("Passwords are the same!");
            }
        }

        function invalid_password_input_display(text) {
            password_text .show();
            password_text .text(text);
            password_input.removeClass("input_waiting");
            password_input.removeClass("input_valid");
            password_input.addClass("input_invalid");
            password01_checked = false;
            submit_button.attr("disabled","true");
            confirm_password_input_display();
        }

        function valid_password_input_display(text) {
            password_text .show();
            password_text .text(text);
            password_input.removeClass("input_waiting");
            password_input.removeClass("input_invalid");
            password_input.addClass("input_valid");
            password01_checked = true;
            setSubmitActive();
            confirm_password_input_display();
        }

        password_input.on("blur", function (){
            if (password_input.val().length < min_password_length) {
                invalid_password_input_display("Password must has at least " + min_password_length + " characters!");
            }
            else if (password_input.val().length > max_password_length) {
                invalid_password_input_display("Password must has at most " + max_password_length + " characters!");
            } else {
                valid_password_input_display("This is a valid password!");
            }
        });

        confirm_password_input.on("blur", confirm_password_input_display);
    }

    // birthdate checking
    {
        let birthdate_input = $("#birthdate");
        let birthdate_text = $("#birthdate_text");
        birthdate_text.hide();

        function invalid_birthdate_input_display(text) {
            birthdate_text .show();
            birthdate_text .text(text);
            birthdate_input.removeClass("input_waiting");
            birthdate_input.removeClass("input_valid");
            birthdate_input.addClass("input_invalid");
            birthdate_checked = false;
            submit_button.attr("disabled","true");
        }

        birthdate_input.on("blur", function () {
            if (birthdate_input.val() === "") {
                invalid_birthdate_input_display("Birthdate cannot be void!")
            } else if (
                new Date(birthdate_input.val()) > new Date(new Date().toLocaleDateString())
            ) {
                invalid_birthdate_input_display("Birthdate must be before today!");
            } else {
                birthdate_text .show();
                birthdate_text .text("Birthdate is valid!");
                birthdate_input.removeClass("input_waiting");
                birthdate_input.removeClass("input_invalid");
                birthdate_input.addClass("input_valid");
                birthdate_checked = true;
                setSubmitActive();
            }
        })
    }

    // Patient History Form
    let drugAllergies = $("#drug_allergies");
    drugAllergies.hide();
    $("#had_drug_allergies").on("change", function () {
        switch (this.value) {
            case "true":
                drugAllergies.show();
                break;
            case "false":
                drugAllergies.hide();
                break;
        }
    })

    // family history form
    let familyMemberFormList = $("#family_member_form_list");
    let cur_family_form_num = 0;

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

    // address checking
    var map = L.map("map").setView([48.4210658, -89.2622423], 16);
    var tiles = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    var marker = L.marker([48.4210658, -89.2622423]).addTo(map);
    {
        let latitude_input = $("#latitude");
        let latitude_display = $("#_latitude");
        let longitude_input = $("#longitude");
        let longitude_display = $("#_longitude");

        map.on("click", function (e) {
            latitude_input.val(e.latlng.lat);
            latitude_display.val(e.latlng.lat);
            longitude_input.val(e.latlng.lng);
            longitude_display.val(e.latlng.lng);
            marker.setLatLng(e.latlng);
        })
    }
})
