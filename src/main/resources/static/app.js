const stompClient = new StompJs.Client();

let name;
let userSubscription;
let messageSubscription;
let caseSubscription;
let casesSubscription;
let caseDataSubscription;

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $('#connect').prop('disabled', connected);
    $('#disconnect').prop('disabled', !connected);
    if (connected) {
        $('#conversation').show();
    }
    else {
        $('#conversation').hide();
    }
    $('#greetings').html('');
}

function setUserSubscribed(isSubscribed) {
    $('#subscribe_users').prop('disabled', isSubscribed);
    $('#unsubscribe_users').prop('disabled', !isSubscribed);
}

function setMessageSubscribed(isSubscribed) {
    $('#subscribe_messages').prop('disabled', isSubscribed);
    $('#unsubscribe_messages').prop('disabled', !isSubscribed);
}

function setCaseSubscribed(isSubscribed) {
    $('#subscribe_case').prop('disabled', isSubscribed);
    $('#unsubscribe_case').prop('disabled', !isSubscribed);
}

function setCasesSubscribed(isSubscribed) {
    $('#subscribe_cases').prop('disabled', isSubscribed);
    $('#unsubscribe_cases').prop('disabled', !isSubscribed);
}

function setCaseDataSubscribed(isSubscribed) {
    $('#subscribe_case_get').prop('disabled', isSubscribed);
    $('#unsubscribe_case_get').prop('disabled', !isSubscribed);
}

function connect() {
    stompClient.brokerURL = 'ws://' + $('#url').val() + '/gs-guide-websocket';
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log('Disconnected');
}

function getCases() {
    $('#cases').html('');

    caseDataSubscription = stompClient.subscribe('/app/all-cases', (cases) => {
        JSON.parse(cases.body).forEach(function(forensicCase) {
            $('#cases').append('<tr><td>' + forensicCase.forensicCaseNumber + '</td></tr>');
        });
    });
}

function sendCase() {
    fetch('/api/v1/forensic-case', {
        method: 'POST',
        headers: {
            Accept: 'application/json',
            'Content-type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify({
            'forensicCaseNumber': $('#caseNumber_rest').val(),
            'sin': $('#sin_rest').val(),
            'status': $('#status_rest').val(),
            'username': $('#user_rest').val()
        })
    })
        .catch(err => console.log(err));
}

function updateCase() {
    fetch('/api/v1/forensic-case/' + $('#caseNumber_rest').val(), {
        method: 'PUT',
        headers: {
            Accept: 'application/json',
            'Content-type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify({
            'forensicCaseNumber': $('#caseNumber_rest').val(),
            'sin': $('#sin_rest').val(),
            'status': $('#status_rest').val(),
            'username': $('#user_rest').val()
        })
    })
        .catch(err => console.log(err));
}

function sendRemove() {
    fetch('/api/v1/forensic-case/' + $('#caseNumber_rest').val(), {
        method: 'DELETE',
        headers: {
            Accept: 'application/json',
            'Content-type': 'application/json; charset=UTF-8'
        }
    })
        .catch(err => console.log(err));
}

function subscribeUser() {
    userSubscription = stompClient.subscribe('/topic/greetings', (greeting) => {
        showMessage(JSON.parse(greeting.body).message);
    });
    setUserSubscribed(true);
}

function unsubscribeUser() {
    userSubscription.unsubscribe();
    setUserSubscribed(false);
}

function subscribeMessage() {
    messageSubscription = stompClient.subscribe('/topic/message', (message) => {
        showMessage(JSON.parse(message.body).message);
    });
    setMessageSubscribed(true);
}

function unsubscribeMessage() {
    messageSubscription.unsubscribe();
    setMessageSubscribed(false);
}

function subscribeCase() {
    caseSubscription = stompClient.subscribe('/topic/case/' + $('#caseNumber').val(), (message) => {
        showMessage(JSON.parse(message.body).message);
    });
    setCaseSubscribed(true);
}

function unsubscribeCase() {
    caseSubscription.unsubscribe();
    setCaseSubscribed(false);
}

function subscribeCases() {
    casesSubscription = stompClient.subscribe('/topic/all_cases', (forensicCase) => {
        let body = JSON.parse(forensicCase.body);
        if (body.deleted === true) {
            $('#cases tr:contains(' + body.forensicCaseNumber + ')').remove();
        }
        else {
            $('#cases').append('<tr><td>' + body.forensicCaseNumber + '</td></tr>');
        }
    });
    setCasesSubscribed(true);
}

function unsubscribeCases() {
    casesSubscription.unsubscribe();
    setCasesSubscribed(false);
}

function subscribeCaseData() {
    caseDataSubscription = stompClient.subscribe('/app/forensic_case/' + $('#caseNumber_get').val(), (message) => {
        showMessage(JSON.parse(message.body).message);
    });
}

function unsubscribeCaseData() {
    caseDataSubscription.unsubscribe();
    setCaseDataSubscribed(false);
}

function sendName() {
    name = $('#name').val();
    stompClient.publish({
        destination: '/app/hello', body: JSON.stringify({'name': name})
    });
}

function sendMessage() {
    let $message = $('#message');
    stompClient.publish({
        destination: '/app/message', body: JSON.stringify({'name': name, 'message': $message.val()})
    });
    $message.val('');
}

function sendMessageCase() {
    let $messageCase = $('#message_case');
    stompClient.publish({
        destination: '/app/forensic_case/' + $('#case_number').val(), body: JSON.stringify({'message': $messageCase.val()})
    });
    $messageCase.val('');
}

function showMessage(message) {
    $('#greetings').append('<tr><td>' + message + '</td></tr>');
}

$(function() {
    $('form').on('submit', (e) => e.preventDefault());
    $('#connect').click(() => connect());
    $('#disconnect').click(() => disconnect());
    $('#get_cases').click(() => getCases());
    $('#send_post').click(() => sendCase());
    $('#send_update').click(() => updateCase());
    $('#send_remove').click(() => sendRemove());
    $('#subscribe_users').click(() => subscribeUser());
    $('#unsubscribe_users').click(() => unsubscribeUser());
    $('#subscribe_messages').click(() => subscribeMessage());
    $('#unsubscribe_messages').click(() => unsubscribeMessage());
    $('#subscribe_case').click(() => subscribeCase());
    $('#unsubscribe_case').click(() => unsubscribeCase());
    $('#subscribe_cases').click(() => subscribeCases());
    $('#unsubscribe_cases').click(() => unsubscribeCases());
    $('#subscribe_case_get').click(() => subscribeCaseData());
    $('#unsubscribe_case_get').click(() => unsubscribeCaseData());
    $('#save_username').click(() => sendName());
    $('#send_message').click(() => sendMessage());
    $('#send_message_case').click(() => sendMessageCase());
});
