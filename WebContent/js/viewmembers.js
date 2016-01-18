sendRequest('GET', 'Roles', null, 'json', true, null, function(roles) {
  sendRequest('GET', 'Accounts', null, 'json', true, null, function(accounts) {
    var removePermission = roles.hasMatch(function(role) {
      return role.role === 'President';
    });
    var table = accounts.map(function(account) {
      var buttonString = '';
      if (removePermission) {
        buttonString = btnStr(account.netid);
      };
      return '<tr>' +
        '<td>' + account.firstName + '</td>' +
        '<td>' + account.lastName + '</td>' +
        '<td>' + account.netid + '</td>' +
        '<td>' + account.phoneNumber + '</td>' +
        buttonString + 
        '</tr>';
    }).reduce(function(s1, s2) {
      return s1 + s2;
    }, '');
    document.getElementById('members').innerHTML = table;
  });
});

/**
 * Removes a site member
 * @param {string} netid - netid of member
 * @returns {undefined}
 */
function removeMember(netid) {
  var data = {netid: netid};
  sendRequest('DELETE', 'Accounts', data, 'text', true, null, function() {
    swal({
      title: 'User Deleted!',
      type: 'success',
      closeOnConfirm: true
    });
  }, function() {
    swal({
      title: 'User Deletion Failed :(',
      text: 'Please make sure you can delete this person',
      type: 'error',
      closeOnConfirm: true
    });
  });
}

/**
 * Creates a button td element for removing members
 * @param {string} netid
 * @returns {string}
 */
function btnStr(netid) {
  return '<td><button type="button" ' +
  'class="btn btn-danger" ' + 
  'onclick="removeMember(\'' + netid + '\')">' +
  'Remove</button>' + 
  '</td>';
}