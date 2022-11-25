if (window.localStorage == null) {
    alert("Sorry, your browser does not support session storage")
}

const LOCALSTORAGE_FALSE = 'no';
const LOCALSTORAGE_TRUE = 'yes';

function localStorage_getCountFromId(cur_id, obj_id) {

    let with_doctor_id_count_key = "" + cur_id + "_" + obj_id + "_count";
    let count = window.localStorage.getItem(with_doctor_id_count_key);
    if (count == null)
    {
        window.localStorage.setItem(with_doctor_id_count_key, '0');
        return 0;
    }
    return parseInt(count);
}

function localStorage_increaseCountFromId(cur_id, obj_id) {
    let cur_index = localStorage_getCountFromId(cur_id, obj_id);
    window.localStorage.setItem("" + cur_id + "_" + obj_id + "_count", "" + (cur_index + 1));
}

function localStorage_decreaseCountFromId(cur_id, obj_id) {
    let cur_index = localStorage_getCountFromId(cur_id, obj_id);
    window.localStorage.setItem("" + cur_id + "_" + obj_id + "_count", "" + (cur_index - 1));
}

function localStorage_getMessage(cur_id, obj_id, index) {
    let string = window.localStorage.getItem("" + cur_id + "_" + obj_id + "_" + index);
    return JSON.parse(string);
}

function localStorage_setMessage(fromSelf, cur_id, obj_id, firstname, lastname, message, hours, minutes) {
    let cur_index = localStorage_getCountFromId(cur_id, obj_id);

    let value = "";
    if (fromSelf) {
        value = JSON.stringify({"fromSelf": fromSelf, "message" : message,
            "firstname": firstname, "lastname": lastname, "hours": hours, "minutes":minutes});
    } else {
        value = JSON.stringify({"fromSelf": fromSelf, "message" : message,
            "hours": hours, "minutes":minutes});
    }
    localStorage_increaseCountFromId(cur_id, obj_id);

    window.localStorage.setItem("" + cur_id + "_" + obj_id + "_" + cur_index, value);
}

function localStorage_contactorCounter(cur_id) {
    let with_doctor_id_count_key = "" + cur_id + "_count";
    let count = window.localStorage.getItem(with_doctor_id_count_key);
    if (count == null)
    {
        window.localStorage.setItem(with_doctor_id_count_key, '0');
        return 0;
    }
    return parseInt(count);
}

function localStorage_increaseContactorCounter(cur_id) {
    let count = localStorage_contactorCounter(cur_id);
    window.localStorage.setItem("" + cur_id + "_count", (count + 1));
}

function localStorage_addNewContactor(cur_id, obj_id) {
    let cur_idx = localStorage_contactorCounter(cur_id);
    if (window.localStorage.getItem("" + cur_id + "_" + obj_id) === null) {
        window.localStorage.setItem("" + cur_id + "_" + cur_idx, obj_id);
        localStorage_increaseContactorCounter((cur_id))
    }
}

function localStorage_getContactor(cur_id, idx) {
    return parseInt(window.localStorage.getItem("" + cur_id + "_" + idx));
}

