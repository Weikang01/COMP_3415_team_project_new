$(document).ready(function () {

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