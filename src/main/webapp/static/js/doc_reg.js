$(document).ready(function () {
    let submit_button = $("input#submit");

    var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#) (\$)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\+)(\=) (\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/) (\<)(\>)(\?)(\)]+/);

    let username_checked = false;
    let password01_checked = false;
    let password02_checked = false;

    submit_button.attr("disabled","true");

    function setSubmitActive() {
        if (username_checked && password01_checked && password02_checked) {
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
                    data: "username=" + input_username.val() + "&db=doc",
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
            confirm_password_input_display();
            setSubmitActive();
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

    // specialties
    let specialties_span = $("#specialties_span");
    let add_spec_button = $("#add_specialty");
    let del_spec_button = $("#del_specialty");
    let max_specialty = 5;
    let min_specialty = 1;
    let spec_size = 1;

    del_spec_button.css("display", "none");
    for (let i = min_specialty + 1; i <= max_specialty; i++) {
        $("#specialty" + i + "_id_label").css("display", "none");
    }

    function addSpecialty() {
        if (spec_size < max_specialty) {
            if (spec_size === min_specialty) {
                del_spec_button.css("display", "inline");
            }
            spec_size ++;
            $("#specialty" + spec_size + "_id_label").css("display", "inline");
            if (spec_size === max_specialty) {
                add_spec_button.css("display", "none");
            }
        }
    }

    function delSpecialty() {
        console.log(spec_size);
        if (spec_size > min_specialty) {
            if (spec_size === max_specialty) {
                add_spec_button.css("display", "inline");
            }
            $("#specialty" + spec_size + "_id_label").css("display", "none");
            spec_size --;
            if (spec_size === min_specialty) {
                del_spec_button.css("display", "none");
            }
        }
    }

    add_spec_button.on("click", addSpecialty);
    del_spec_button.on("click", delSpecialty);
})
