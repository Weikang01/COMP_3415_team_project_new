$(document).ready(function () {
    let submit_button = $("input#submit");

    var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#) (\$)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\+)(\=) (\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/) (\<)(\>)(\?)(\)]+/);

    let birthdate_checked = false;

    submit_button.attr("disabled","true");

    function setSubmitActive() {
        if (birthdate_checked) {
            submit_button.removeAttr("disabled");
        }
    }
})