sendRequest('GET', 'Account', null, 'json', true, null, function(account) {
  document.getElementById('navbar-username').innerHTML = 
    '<a href="profile.html">' + account.firstName + ' ' + account.lastName + '</a>';
  }, function() {
  Cookies.expire('token');
  window.location.href='index.html';
});

/**
 * Logs the user out and retuns them to the login page
 * @returns {undefined}
 */
function logout() {
  sendRequest('DELETE', 'Login', null, 'json', true, null);
  Cookies.expire('token');
  window.location.href = 'index.html';
}

sendRequest('GET', 'Account', null, 'json', true, {showPermissions: true}, function(permissions) {
  if (!permissions.contains('events.post')) $('#createevent').hide();
  if (!permissions.contains('duties.assign')) $('#assignduties').hide();
  if (!permissions.contains('accountRequests.view')) $('#accountrequests').hide();
  if (!permissions.contains('fines.create')) $('#assignfines').hide();
  if (!permissions.contains('email.send')) $('#sendemails').hide();
  if (!permissions.contains('announcements.create')) $('#createannouncement').hide();

  if (!permissions.contains('duties.assign') && !permissions.contains('events.post')) $('#eventDropdown').hide();
  if (!permissions.contains('email.send') && !permissions.contains('announcements.create')) $('#adminDropdown').hide();
});
