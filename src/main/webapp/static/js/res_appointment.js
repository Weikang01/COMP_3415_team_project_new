function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] === variable){return pair[1];}
    }
    returnfalse;
}

$(window).ready(function () {
    for (let i = 0, j=12; i < 12; i++, j--) {
        $("<option value='" + (j - 1) + "'>" + j + " AM</option>").prependTo($("#hour"));
        $("<option value='" + (i + 12) + "'>" + (i+1) + " PM</option>").appendTo($("#hour"));
    }

    for (let i = 0; i <= 60; i+=15) {
        $("<option value='" + i + "'>" + i + "</option>").appendTo($("#min"));
    }

    $("#doctor_id").val(getQueryVariable("doctor_id"));
})