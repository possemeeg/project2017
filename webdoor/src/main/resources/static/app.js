var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/app/general.users', function (userupdate) {
            showUsers(JSON.parse(userupdate.body));
        });
        stompClient.subscribe('/user/personal.directives', function (directiveupdate) {
            showDirectives(JSON.parse(directiveupdate.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    sendDirective($("#name").val(), $("#to").val())
}


function showDirectives(directives) {
    for (var i = 0; i < directives.length; i++) {
        showDirective(directives[i]);
    }
}

function showDirective(directive) {
    $("<tr>"
        + "<td>" + directive.id + "</td>"
        + "<td>" + directive.textContent + "</td>"
        + "<td>" + directive.sender + "</td>"
        + "<td>" + "" + "</td>"
        + "<td><button class=\"btn btn-default\" onclick=\"sendDirective('ack', '" + directive.sender + "')\">Respond</button></td>"
        + "</tr>").prependTo("#greetings");
}

function onlinetext(userchange) {
    if (userchange.online) {
        return "- online"
    } else {
        return "- offline"
    }
}

function showUsers(userchanges) {
    for (var i = 0; i < userchanges.length; i++) {
        $("<tr><td>"
            + userchanges[i].user
            + onlinetext(userchanges[i])
            + "</td></tr>").prependTo("#users");
    }
}

function sendDirective(textContent, to) {
    stompClient.send("/app/senddirective", {}, JSON.stringify({'textContent': textContent, 'recipient': to}));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
