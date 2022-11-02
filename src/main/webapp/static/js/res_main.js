function generate_doctor_card(doctor, distance = 0.0, online=false) {
    return `<div class="doctor_card" id=${doctor.id}>
        <h3><span>${doctor.firstname}</span> <span>${doctor.lastname}</span></h3>
        <div>Specialty:
            <span class="spec_border" style="display: ${(doctor.specialty1_id === 0?'none':'inline')}" >${doctor.specialty1}</span>
            <span class="spec_border" style="display: ${(doctor.specialty2_id === 0?'none':'inline')}" >${doctor.specialty2}</span>
            <span class="spec_border" style="display: ${(doctor.specialty3_id === 0?'none':'inline')}" >${doctor.specialty3}</span>
            <span class="spec_border" style="display: ${(doctor.specialty4_id === 0?'none':'inline')}" >${doctor.specialty4}</span>
            <span class="spec_border" style="display: ${(doctor.specialty5_id === 0?'none':'inline')}" >${doctor.specialty5}</span>
        </div>
        <div>Organization: <span>${doctor.hospital}</span></div>
        <div>Distance: <span></span>${distance}</div>
        <div class="status">Status: <span>${(online ? 'online' : 'offline')}</span> </div>
        <div class="consult_button" style="background: aquamarine">
            <input type="hidden" name="doctor_id" value="${doctor.id}">
            Consult Online!</div>
        <div style="background: darkturquoise" class="appointment_button">
            <input type="hidden" name="doctor_id" value="${doctor.id}">
            Make an Appointment</div>
    </div>`;
}

function doctor_set_online_status(doctor_id, online_status=true) {
    $(".doctor_card#"+doctor_id).find(".status>span").text(online_status ? 'online' : 'offline');
}

/**
 * @param d
 * @returns {number}
 */
function getRad(d){
    var PI = Math.PI;
    return d*PI/180.0;
}

/**
 * @param lng1
 * @param lat1
 * @param lng2
 * @param lat2
 * @returns {number|*}
 * @constructor
 */
function getDistance(lng1,lat1,lng2,lat2){
    var f = getRad((lat1 + lat2)/2);
    var g = getRad((lat1 - lat2)/2);
    var l = getRad((lng1 - lng2)/2);
    var sg = Math.sin(g);
    var sl = Math.sin(l);
    var sf = Math.sin(f);
    var s,c,w,r,d,h1,h2;
    var a = 6378137.0;//The Radius of eath in meter.
    var fl = 1/298.257;
    sg = sg*sg;
    sl = sl*sl;
    sf = sf*sf;
    s = sg*(1-sl) + (1-sf)*sl;
    c = (1-sg)*(1-sl) + sf*sl;
    w = Math.atan(Math.sqrt(s/c));
    r = Math.sqrt(s*c)/w;
    d = 2*w*a;
    h1 = (3*r -1)/2/c;
    h2 = (3*r +1)/2/s;
    s = d*(1 + fl*(h1*sf*(1-sg) - h2*(1-sf)*sg));
    if(s >= 1000 && s <= 99000){
        var kilometer = s/1000;
        s = kilometer.toFixed(1) + ' km';
    }else if(s > 99000){
        s = '>99km';
    }else{
        s = Math.round(s) + ' m';
    }
    return s;
}

$(document).ready(function () {
    let res_username = $("#resident_username_holder").val();
    let res_id = $("#resident_id_holder").val();
    let current_doctors_on_list;
    let doctorCardList = $("#doctor_card_list");
    let residentLatitude = parseFloat($("#resident_latitude_holder").val());
    let residentLongitude = parseFloat($("#resident_longitude_holder").val());

    $.get({
        url: "/docdao",
        dataType: "html",
        data: "item=getDoctorsNearCoord",
        success: function (resp) {
            let dataJson = JSON.parse(resp);
            for (let i = 0; i < dataJson.length; i++) {
                doctorCardList.append(generate_doctor_card(dataJson[i],
                    getDistance(residentLongitude, residentLatitude, dataJson[i].longitude, dataJson[i].latitude),
                    false));
            }
        },
        async: false
    })

    var websocket;
    if (typeof (WebSocket) == "undefined") {
        alert("Sorry, your browser does not support Web Socket");
    } else {
        websocket = new WebSocket("ws://" + document.location.host + "/main");
    }
    websocket.onerror = function () {
    }
    websocket.onopen = function () {
    }
    websocket.onmessage = function (event) {
        var dataStr = event.data;
        let dataJson = JSON.parse(dataStr);
        if (dataJson.system) {
            if (dataJson.message === "new") {
                console.log(dataJson.fromId);
                doctor_set_online_status(dataJson.fromId, true);
            } else if (typeof (dataJson.message) === typeof([])) {
                for (let i = 0; i < dataJson.message.length; i++) {
                    console.log(dataJson.message[i]);
                    doctor_set_online_status(dataJson.message[i], true);
                }
            }
        } else {
        }
    }
    websocket.onclose = function () {
    }

    window.onbeforeunload = function () {
        closeWebSocket();
    }

    function closeWebSocket() {
        websocket.close();
    }

    $(".consult_button").each(function (index) {
        $(this).on("click", function () {
            let doctor_id = $(this).find("input[name='doctor_id']").val();
            window.open('/chat/from_res?resident_id=' + res_id + '&doctor_id=' + doctor_id, 'newwindow', 'height=600, width=600, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
        })
    });

    $(".appointment_button").each(function (index) {
        $(this).on("click", function () {

            let doctor_id = $(this).find("input[name='doctor_id']").val();
            window.open('/make_appointment?resident_id='+ res_id +'&doctor_id=' + doctor_id, 'newwindow', 'height=80, width=500, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
        })
    });

    let symptomList = [];

    function get_sub_symptom_list(search) {
        var final_output = "";

        var recom_score = [];
        var option_htmls = [];

        var cur_index = 0;
        var last_bolded_index = 0;
        var is_last_bolded =false;
        var included = false;
        for (let i = 0; i < symptomList.length; i++) {
            cur_index = 0;
            last_bolded_index = 0;
            included = false;
            is_last_bolded = false;
            var final_option_output = "";
            for (let j = 0; j < symptomList[i].name.length; j++) {
                if (cur_index < search.length && symptomList[i].name.at(j) === search.at(cur_index)) {
                    cur_index ++;
                    if (!included) {
                        last_bolded_index = j;
                        included = true;
                        final_option_output = final_option_output.concat(`<option value="${symptomList[i].id}">${symptomList[i].name.substring(0, j)}<b>`);
                        // final_option_output += `<option value="${symptomList[i].name}">${symptomList[i].name.substring(0, j)}<b>`;
                        is_last_bolded = true
                        recom_score.push(100 - j * 10);
                    } else {
                        if (!is_last_bolded) {
                            // final_option_output = final_option_output.concat(`<b>`);
                            final_option_output += `<b>`;
                            last_bolded_index = j;
                            recom_score[recom_score.length - 1] += (100 - j * 10);
                        }
                        recom_score[recom_score.length - 1] += (100 - j * 10);
                    }

                } else {  // symptomList[i].name.at(j) !== search.at(cur_index)
                    if (included && is_last_bolded) {
                        is_last_bolded = false;
                        // final_option_output = final_option_output.concat(`${symptomList[i].name.substring(last_bolded_index, j)}</b>`);
                        final_option_output += `${symptomList[i].name.substring(last_bolded_index, j)}</b>`;
                        last_bolded_index = j;
                    }
                }

                if ((j === symptomList[i].name.length - 1) && included) {
                    if (is_last_bolded) {
                        final_option_output += `${symptomList[i].name.substring(last_bolded_index, j)}</b></option>\n`;
                    } else {
                        final_option_output += `${symptomList[i].name.substring(last_bolded_index, j)}</option>\n`;
                    }
                    option_htmls.push(final_option_output);
                }
            }
        }

        function swap_option(left_index, right_index) {
            var temp_score = recom_score[left_index];
            var temp_html = option_htmls[left_index];
            recom_score[left_index] = recom_score[right_index];
            option_htmls[left_index] = option_htmls[right_index];
            recom_score[right_index] = temp_score;
            option_htmls[right_index] = temp_html;
        }
        
        function partition(left_index, right_index) {
            var pivot = recom_score[Math.floor((left_index + right_index) * .5)];
            var i = left_index;
            var j = right_index;

            while (i <= j) {
                while (recom_score[i] > pivot) {
                    i ++;
                }

                while (recom_score[j] < pivot) {
                    j--;
                }

                if (i <= j) {
                    swap_option(i, j);
                    i++;
                    j--;
                }
            }
            return i;
        }

        function quick_sort(left_index, right_index) {
            var index;

            if (recom_score.length > 1) {
                index = partition(left_index, right_index);

                if (left_index < index - 1) {
                    quick_sort(left_index, index - 1);
                }

                if (index < right_index) {
                    quick_sort(index, right_index);
                }
            }
        }

        quick_sort(0, recom_score.length - 1);

        for (let i = 0; i < recom_score.length; i++) {
            final_output += `${option_htmls[i]}\n`;
        }
        return final_output;
    }  // end of get_sub_symptom_list(search)

    function get_sub_symptom_list_simplified(search) {
        var final_output = "";

        var recom_score = [];
        var option_htmls = [];

        var cur_index = 0;
        var included = false;

        for (let i = 0; i < symptomList.length; i++) {
            included = false;
            cur_index = 0;
            for (let j = 0; j < symptomList[i].name.length; j++) {
                if (cur_index < search.length && symptomList[i].name.at(j) === search.at(cur_index)) {
                    cur_index ++;
                    if (!included) {
                        included = true;
                        option_htmls.push(`<option value="${symptomList[i].name}"></option>`);
                        recom_score.push(100 - j * 10);
                    } else {
                        recom_score[recom_score.length - 1] += 100 - j * 10;
                    }
                }
            }
        }

        function swap_option(left_index, right_index) {
            var temp_score = recom_score[left_index];
            var temp_html = option_htmls[left_index];
            recom_score[left_index] = recom_score[right_index];
            option_htmls[left_index] = option_htmls[right_index];
            recom_score[right_index] = temp_score;
            option_htmls[right_index] = temp_html;
        }

        function partition(left_index, right_index) {
            var pivot = recom_score[Math.floor((left_index + right_index) * .5)];
            var i = left_index;
            var j = right_index;

            while (i <= j) {
                while (recom_score[i] > pivot) {
                    i ++;
                }

                while (recom_score[j] < pivot) {
                    j--;
                }

                if (i <= j) {
                    swap_option(i, j);
                    i++;
                    j--;
                }
            }
            return i;
        }

        function quick_sort(left_index, right_index) {
            var index;

            if (recom_score.length > 1) {
                index = partition(left_index, right_index);

                if (left_index < index - 1) {
                    quick_sort(left_index, index - 1);
                }

                if (index < right_index) {
                    quick_sort(index, right_index);
                }
            }
        }

        quick_sort(0, recom_score.length - 1);

        for (let i = 0; i < recom_score.length; i++) {
            final_output += `${option_htmls[i]}\n`;
        }

        console.log(final_output);
        return final_output;
    }

    let symptom_list_element = $("#symptom_list");
    $("#symptoms").on("input", function (e) {

        let val = $("#symptoms").val().trim();
        if (val != null && val !== "") {
            symptom_list_element.empty();
            if (symptomList.length === 0) {
                $.ajax({
                    type: "GET",
                    url: "/symptomSearch",
                    dataType: "html",
                    data: "value=" + val,
                    success: function (resp) {
                        let json = JSON.parse(resp);
                        console.log(json);
                        _idx = 0;
                        while (_idx <= 8) {
                            if (json[_idx] === undefined) {
                                break;
                            }
                            symptomList.push(json[_idx++]);
                        }

                        symptom_list_element.append(get_sub_symptom_list_simplified(val));
                    }
                });
            }
            symptom_list_element.append(get_sub_symptom_list_simplified(val));
        }
    })
})