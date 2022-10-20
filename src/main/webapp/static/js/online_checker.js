function _online_checker (username, db) {
    $.ajax({
        type: "GET",
        url: "/online_checker",
        dataType: "html",
        data: "username=" + username + "&db=" + db,
    })
}
