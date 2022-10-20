$(document).ready(function () {
    let res_username = $("#resident_username_holder").val();

    setInterval(function () {
        _online_checker(res_username, "res");
    }, 20000);

    $(".consult_button").each(function (index) {
        $(this).on("click", function () {
            let doctor_id = $(this).find("input[name='doctor_id']").val();
            window.open('/chat?doctor_id=' + doctor_id, 'newwindow', 'height=600, width=600, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
        })
    })

})