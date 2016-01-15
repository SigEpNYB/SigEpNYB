function getAccounts() {
  httpRequest('GET', 'Accounts', true, null, null, function(resp) {
    var table = '<tr><td><b>First Name</b></td><td><b>Last Name</b></td><td><b>NetID</b></td></tr>';
    for (var i = 0; i < resp.length; i++) {
      var member = resp[i];
      table += '<tr><td>' + member.firstName + '</td><td>' + member.lastName + '</td><td>' + member.netid + '</td></tr>';
    }
    document.getElementById('members').innerHTML = table;
  });
}