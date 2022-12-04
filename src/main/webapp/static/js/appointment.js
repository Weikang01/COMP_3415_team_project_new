function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] === variable){return pair[1];}
    }
    return false;
}

$(window).ready(function () {
    var websocket;
    if (typeof (WebSocket) == "undefined") {
        alert("Sorry, your browser does not support Web Socket");
    } else {
        websocket = new WebSocket("ws://" + document.location.host + window.location.pathname + '/q' + window.location.search);
    }

    websocket.onopen = function () {}

    websocket.onmessage = function (event) {}

    websocket.onclose = function () {}

    window.onbeforeunload = function () {
        closeWebSocket();
    }

    function closeWebSocket() {
        websocket.close();
    }

    for (let i = 0, j=12; i < 12; i++, j--) {
        $("<option value='" + (j - 1) + "'>" + j + " AM</option>").prependTo($("#hour"));
        $("<option value='" + (i + 12) + "'>" + (i+1) + " PM</option>").appendTo($("#hour"));
    }

    for (let i = 0; i <= 60; i+=15) {
        $("<option value='" + i + "'>" + i + "</option>").appendTo($("#min"));
    }

    $("#doctor_id").val(getQueryVariable("doctor_id"));
    $("#resident_id").val(getQueryVariable("resident_id"));
    let submit_button = $("#submit");
    submit_button.attr("disabled","true");
    let $date = $("#date");
    let $dateText = $("#date_text");
    $dateText.hide();
    submit_button.attr("disabled","true");

    function invalid_date(text) {
        $dateText .show();
        $dateText .text(text);
        $date.removeClass("input_waiting");
        $date.removeClass("input_valid");
        $date.addClass("input_invalid");
        submit_button.attr("disabled","true");
    }

    function valid_date(text) {
        $dateText .show();
        $dateText .text(text);
        $date.removeClass("input_waiting");
        $date.removeClass("input_invalid");
        $date.addClass("input_valid");
        submit_button.removeAttr("disabled");
    }

    $date.on("blur", function () {
        if ($date.val() === "") {
            invalid_date("date cannot be void!");
        } else if (
            new Date($date.val()) < new Date(new Date().toLocaleDateString())
        ) {
            invalid_date("date must be after today!");
        } else {
            valid_date("Date is valid!");
        }
    });

    submit_button.on("click", function () {
        websocket.send(`{"type":"new", "date":"${$date.val()}", "hour":${$("#hour").val()}, 
        "min":${$("#min").val()}, "reason": "${$("#reason").val()}", 
        "doctor_id": ${$("#doctor_id").val()}, "resident_id": ${$("#resident_id").val()}}`);
        window.close();
    });
})