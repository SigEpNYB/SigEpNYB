/**
 * Logs the user out and retuns them to the login page
 */
function logout() {
  sendRequest('DELETE', 'Login', null, 'json', true, null);
  Cookies.expire('token');
  window.location.href = '/Fratsite';
}

sendRequest('GET', 'Account', null, 'json', true, null, function(account) {
  document.getElementById('navbar-username').innerHTML = 
    '<a href="profile.html">' + account.firstName + ' ' + account.lastName + '</a>';
  }, function() {
  Cookies.expire('token');
  window.location.href='/Fratsite';
});