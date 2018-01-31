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
        stompClient.subscribe('/app/topic/global-messages', function (greeting) {
            messages = JSON.parse(greeting.body);
            for (var i = 0; i < messages.length; i++) {
                showGreeting(messages[i]);
            }
        });
        stompClient.subscribe('/user/topic/personal-messages', function (greeting) {
            message = JSON.parse(greeting.body);
            showGreeting(message);
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

function showGreeting(message) {
    $("<tr><td>"
        + message.message + "</td><td>From: "
        + message.user
        + "</td><td><button class=\"btn btn-default\" onclick=\"sendMessage('ack', '"
        + message.user + "')\">Respond</button></td></tr>").prependTo("#greetings");
}


function sendMessage(message, to) {
    stompClient.send("/app/send-message", {}, JSON.stringify({'message': message, 'recipient': to}));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
