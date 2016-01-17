/**
 * Logs the user in
 * @returns {undefined}
 */
function login() {
  var data = buildObj(['netid', 'password']);
  sendRequest('POST', 'Login', data, 'json', false, null, function(response) {
      Cookies.set('token', response.token);
      window.location.href = '/Fratsite/dashboard.html';
    }, function() {
      swal({
        title: 'Login Failed :(',
        text: 'Please make sure your NetID and Password are correct',
        type: 'error',
        closeOnConfirm: true
      });
    }
  );
}

$(document).ready(function() {
  $('.loginInput').keyup(function(event) {
    if (event.keyCode === 13) login();
  });
});

if (Cookies.get('token') !== undefined) {
  window.location.href = '/Fratsite/dashboard.html';
}