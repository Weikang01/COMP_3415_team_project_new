$(document).ready(function () {
    // specialties
    let add_spec_button = $("#add_specialty");
    let del_spec_button = $("#del_specialty");
    let max_specialty = 5;
    let min_specialty = 1;
    let spec_size = parseInt($("#doc_specialties_count").val());

    if (spec_size <= 1) {
        del_spec_button.css("display", "hide");
    } else if (spec_size >= max_specialty) {
        add_spec_button.css("display", "hide");
    }

    for (let i = spec_size + 1; i <= max_specialty; i++) {
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
            $("#specialty" + spec_size + "_id").prop('selectedIndex',0);
            spec_size --;
            if (spec_size === min_specialty) {
                del_spec_button.css("display", "none");
            }
        }
    }

    add_spec_button.on("click", addSpecialty);
    del_spec_button.on("click", delSpecialty);

    let latitude_input = $("#latitude");
    let latitude_display = $("#_latitude");
    let longitude_input = $("#longitude");
    let longitude_display = $("#_longitude");

    var map = L.map("map").setView([latitude_display.val(), longitude_display.val()], 16);
    var tiles = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);
    var marker = L.marker([latitude_display.val(), longitude_display.val()]).addTo(map);

    map.on("click", function (e) {
        latitude_input.val(e.latlng.lat);
        latitude_display.val(e.latlng.lat);
        longitude_input.val(e.latlng.lng);
        longitude_display.val(e.latlng.lng);
        marker.setLatLng(e.latlng);
    })
})