sendRequest('GET', 'Account', null, 'json', true, null, function(account) {
  document.getElementById('name').innerHTML = 
    '<h1>' + account.firstName + ' ' + account.lastName + '</h1>';
  document.getElementById('netid').innerHTML =
    '<h1>' + account.netid + '</h1>';
  document.getElementById('phoneNumber').innerHTML =
    '<h2>' + account.phone + '</h2>';
});

sendRequest('GET', 'Roles', null, 'json', true, null, function(roles) {
  var roleString = roles.map(function(role) {
    return '<h3>' + role.role + '</h3>';
  }).reduce(function(s1, s2) {
    return s1 + s2;
  }, '');
  document.getElementById('roles').innerHTML = roleString;
});