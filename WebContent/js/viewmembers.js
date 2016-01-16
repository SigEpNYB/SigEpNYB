sendRequest('GET', 'Accounts', null, true, null, function(accounts) {
  var table = accounts.map(function(account) {
    return '<tr>' +
      '<td>' + account.firstName + '</td>' +
      '<td>' + account.lastName + '</td>' +
      '<td>' + account.netid + '</td>' +
      '</tr>';
  }).reduce(function(s1, s2) {
    return s1 + s2;
  }, '');
  document.getElementById('members').innerHTML = table;
})