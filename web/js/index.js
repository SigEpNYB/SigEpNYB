if (Cookies.get('token') !== undefined) {
  window.location.href = 'dashboard.html';
}

$(document).ready(function() {
  $('.loginInput').keyup(function(event) {
    if (event.keyCode === 13) login();
  });
});

function login() {
  var data = buildObj(['netid', 'password']);
  sendRequest('POST', 'Login', data, 'json', false, null, function(response) {
    Cookies.set('token', response.token);
    window.location.href = 'dashboard.html';
  }, onSendFail);
}