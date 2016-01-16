/**
 * Logs the user out and retuns them to the login page
 */
function logout() {
  sendRequest('DELETE', 'Login', null, true, null);
  Cookies.expire('token');
  window.location.href = '/Fratsite/index.html';
}

sendRequest('GET', 'Account', null, true, null, function(account) {
  document.getElementById('navbar-username').innerHTML = 
    '<a>' + account.firstName + ' ' + account.lastName + '</a>';
});