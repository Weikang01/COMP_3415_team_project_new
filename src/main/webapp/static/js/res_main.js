function generate_doctor_card(doctor, distance = 0.0, online=false) {
    let r = `<div class="doctor_card" id=${doctor.id}>
        <h3><span>${doctor.firstname}</span> <span>${doctor.lastname}</span></h3>
        <div>Specialty:
        `;

    for (let i = 0; i < doctor.specialties.length; i++) {
        r += `<span class="spec_border" style="display: ${(doctor.specialties[i] === ''?'none':'inline')}" >${doctor.specialties[i]}</span>`;
    }

    r += `
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
    
    return r;
}

function doctor_set_online_status(doctor_id, online_status=true) {
    $(".doctor_card#"+doctor_id).find(".status>span").text(online_status ? 'online' : 'offline');
}

// function generate_symptom_card(symptom) {
//     return `<div class="symptom_card"><div>${symptom.name}</div>`;
// }

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

function moveEnd(obj){
    obj = obj[0];
    obj.focus();
    let len = obj.innerText.length;
    if (document.selection) {
        let sel = document.selection.createRange();
        sel.moveStart('character',len);
        sel.collapse();
        sel.select();
    }
    else{
        let sel = window.getSelection();
        let range = document.createRange();
        range.selectNodeContents(obj);
        range.collapse(false);
        sel.removeAllRanges();
        sel.addRange(range);
    }
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

    function consult_button_onclick(index) {
        $(this).on("click", function () {
            let doctor_id = $(this).find("input[name='doctor_id']").val();
            window.open('/chat/from_res?resident_id=' + res_id + '&doctor_id=' + doctor_id, 'newwindow', 'height=600, width=600, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
        })
    }
    $(".consult_button").each(consult_button_onclick);

    function appointment_button_onclick(index) {
        $(this).on("click", function () {

            let doctor_id = $(this).find("input[name='doctor_id']").val();
            window.open('/make_appointment?resident_id='+ res_id +'&doctor_id=' + doctor_id, 'newwindow', 'height=80, width=500, top=300, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
        })
    }
    $(".appointment_button").each(appointment_button_onclick);

    let symptomList = [];
    let cur_index = -1;

    let option_list = $("#option_list");
    let symptom_input_div = $("#symptoms");

    function get_sub_symptom_list(search) {
        var final_output = "";

        var recom_score = [];
        var option_htmls = [];

        var cur_index = 0;
        var last_bolded_index = 0;
        var is_last_bolded =false;
        var included = false;

        var last_countable_index = 0;
        var continuous = 0;
        for (let i = 0; i < symptomList.length; i++) {
            cur_index = 0;
            last_bolded_index = 0;
            included = false;
            is_last_bolded = false;
            last_countable_index = 0;
            continuous = 0;

            var final_option_output = "";
            for (let j = 0; j < symptomList[i].name.length; j++) {
                if (cur_index < search.length && symptomList[i].name.at(j) === search.at(cur_index)) {
                    cur_index ++;
                    if (last_countable_index = j - 1) {
                        continuous ++;
                    } else {
                        continuous = 0;
                    }
                    last_countable_index = j;
                    if (!included) {
                        last_bolded_index = j;
                        included = true;
                        final_option_output = final_option_output.concat(
                            `<span name="${symptomList[i].id}">${symptomList[i].name.substring(0, j)}<b>`);
                        is_last_bolded = true
                        recom_score.push(100 - j * 10 + continuous * 50);
                    } else {
                        if (!is_last_bolded) {
                            is_last_bolded = true;
                            final_option_output += `${symptomList[i].name.substring(last_bolded_index, j)}<b>`;
                            last_bolded_index = j;
                            recom_score[recom_score.length - 1] += (100 - j * 10 + continuous * 50);
                        } else {
                            recom_score[recom_score.length - 1] += (100 - j * 10 + continuous * 50);
                        }
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
                        final_option_output += `${symptomList[i].name.substring(last_bolded_index, j + 1)}</b></span>\n`;
                    } else {
                        final_option_output += `${symptomList[i].name.substring(last_bolded_index, j + 1)}</span>\n`;
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

    let changed = false;
    symptom_input_div.on("DOMSubtreeModified", function (e) {
        if (!changed){
            changed = true;
            option_list.empty();
            let symptom_options = symptom_input_div.text().split(",");
            let val = symptom_options[symptom_options.length - 1].trim();
            if (val != null && val !== "") {
                // console.log(`val != null && val !== ""`);
                if (symptomList.length === 0) {
                    $.ajax({
                        type: "GET",
                        url: "/symptomSearch",
                        success: function (resp) {
                            let json = JSON.parse(resp);
                            console.log(json);
                            _idx = 0;
                            if (symptomList.length !== 0)
                                return;
                            while (_idx <= 8) {
                                if (json[_idx] === undefined) {
                                    break;
                                }
                                symptomList.push(json[_idx++]);
                            }
                            option_list.append(get_sub_symptom_list(val));
                            option_list.find("span").on("click", function (e) {
                                symptom_input_div.text($(this).text());
                            })
                            cur_index = -1;
                            changed = false;
                        }
                    });
                }
                option_list.append(get_sub_symptom_list(val));
                option_list.find("span").on("click", function (e) {
                    symptom_input_div.text($(this).text());
                })
                changed = false;
            }
            changed = false;
        }
    });

    symptom_input_div.on("click", function (e) {
        option_list.show();
    })

    symptom_input_div.keydown(function (event) {
        let span_arr_length = option_list.find("span").toArray().length;
        switch (event.which) {
            case 13:  // enter
                if (cur_index !== -1) {
                    window.getSelection().removeAllRanges();

                    let new_text = "<a class=\"symptom_string\">" + option_list.find(`span:nth-of-type(${cur_index})`).text() + "</a>,";

                    let original_text_list = symptom_input_div.html().split(",");
                    let length = original_text_list.length;
                    original_text_list.pop();

                    let lastComma = symptom_input_div.text().lastIndexOf(",");
                    if (lastComma === -1) lastComma = 0;

                    symptom_input_div.html(original_text_list.join() + (length > 1? ",": "") + new_text);
                    moveEnd(symptom_input_div);
                    cur_index = -1;
                }
                return false;
            case 38: // arrow up
                if (cur_index === -1) {
                    cur_index = span_arr_length;
                }
                else {
                    option_list.find(`span:nth-of-type(${cur_index})`).removeClass("hovered_span");
                    cur_index -= 1;
                    if (cur_index <= 0) {
                        cur_index += span_arr_length;
                    }
                }
                option_list.find(`span:nth-of-type(${cur_index})`).addClass("hovered_span");
                break;
            case 40: // arrow down
                if (cur_index === -1) {
                    cur_index = 1;
                } else {
                    option_list.find(`span:nth-of-type(${cur_index})`).removeClass("hovered_span");
                    cur_index += 1;
                    if (cur_index > span_arr_length) {
                        cur_index -= span_arr_length;
                    }
                }
                option_list.find(`span:nth-of-type(${cur_index})`).addClass("hovered_span");
                break;
            case 188:  // comma
                return false;
        }
    })

    let $symptomDoctorList = $("#symptom_doctor_list");
    $("#symptoms_search").on("click", function (e) {
        $.ajax({
            url: "/symptomSearch",
            type: "POST",
            data: {symptom_str: symptom_input_div.text()},
            success: function (data, textStatus, jqXHR) {
                let json = JSON.parse(data);
                let html = "";
                for (let i = 0; i < json.length; i++) {
                    html += `<div class="symptom_card"><div>${json[i].name}</div>`;
                    for (let j = 0; j < json[i].diseases.length; j++) {
                        html += `<div class="disease_card"><div>${json[i].diseases[j].name}</div><div>Correlativity: ${json[i].diseases[j].correlativity}</div>`;
                        for (let k = 0; k < json[i].diseases[j].specialties.length; k++) {
                            html += `<div class="specialty_card"><div>${json[i].diseases[j].specialties[k].name}</div>`;
                            for (let l = 0; l < json[i].diseases[j].specialties[k].doctors.length; l++) {
                                html += `<div class="doctor_card">`;
                                html += `<div>${json[i].diseases[j].specialties[k].doctors[l].firstname} ${json[i].diseases[j].specialties[k].doctors[l].lastname}</div>`;
                                html += `<div>Organization: ${json[i].diseases[j].specialties[k].doctors[l].organization}</div>`;
                                html += `<div>Distance: ${getDistance(residentLongitude, residentLatitude, parseFloat(json[i].diseases[j].specialties[k].doctors[l].longitude), parseFloat(json[i].diseases[j].specialties[k].doctors[l].latitude))}</div>`;
                                html += `<div class="consult_button" style="background: aquamarine">`;
                                html += `<input type="hidden" name="doctor_id" value="${json[i].diseases[j].specialties[k].doctors[l].id}">`;
                                html += `Consult Online!</div>`;
                                html += `<div style="background: darkturquoise" class="appointment_button">`;
                                html += `<input type="hidden" name="doctor_id" value="${json[i].diseases[j].specialties[k].doctors[l].id}"">`;
                                html += `Make an Appointment</div>`;
                                html += `</div>`;
                            }
                            html += `</div>`;
                        }
                        html += `</div>`;
                    }
                    html += `</div>`;
                }
                $symptomDoctorList.empty();
                $symptomDoctorList.html(html);

                $(".consult_button").each(consult_button_onclick);
                $(".appointment_button").each(appointment_button_onclick);
                // console.log(json);
            },
            error: function (jqXHR, textStatus, errorThrown) {
            }
        })
    })

    $(document).mouseup(function (e) {
        if (!option_list.is(e.target) && option_list.has(e.target).length === 0) {
            option_list.hide();
        }
    })
})