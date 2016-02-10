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
  return sendLogin(data);
}

/**
 * Logs the user in
 * @param {Object} data - Must contain netid and password
 * @returns {undefined}
 */
function sendLogin(data) {
  sendRequest('POST', 'Login', data, 'json', false, null, function(response) {
      Cookies.set('token', response.token);
      window.location.href = 'dashboard.html';
    }, function() {
      swal({
        title: 'Login Failed',
        text: 'Please make sure your NetID and Password are correct',
        type: 'error'
      });
    }
  );
}