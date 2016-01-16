/**
 * Logs the user in
 * @returns {undefined}
 */
function login() {
  var data = buildObj(['netid', 'password']);
  sendRequest('POST', 'Login', data, false, null, 
    function(data) {
      Cookies.set('token', data.token);
      window.location.href = '/Fratsite/dashboard.html'
    },
    function(xhr) {
      console.log('Login Failed: ' + xhr.status)
    }
  );
}

$(document).ready(function() {
  $('.loginInput').keyup(function(event) {
    if (event.keyCode === 13) login();
  });
});