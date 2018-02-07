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
        stompClient.subscribe('/user/personal.greetings', function (greeting) {
            showGreetings(JSON.parse(greeting.body));
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
    sendMessage($("#name").val(), $("#to").val())
}


function showGreetings(messages) {
    for (var i = 0; i < messages.length; i++) {
        showGreeting(messages[i]);
    }
}

function showGreeting(message) {
    $("<tr><td>"
        + message.message + "</td><td>From: "
        + message.user
        + "</td><td><button class=\"btn btn-default\" onclick=\"sendMessage('ack', '"
        + message.user + "')\">Respond</button></td></tr>").prependTo("#greetings");
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

function sendMessage(message, to) {
    stompClient.send("/app/hello", {}, JSON.stringify({'message': message, 'recipient': to}));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
