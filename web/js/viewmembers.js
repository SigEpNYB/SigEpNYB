sendRequest('GET', 'Account', null, 'json', true, {showPermissions: true}, function(permissions) {
  sendRequest('GET', 'Accounts', null, 'json', true, null, function(accounts) {
    var removePermission = permissions.indexOf('accounts.delete') >= 0;

    document.getElementById('members').innerHTML = accounts.sort(function(account1, account2) {
      return account1.firstName.toUpperCase().localeCompare(account2.firstName.toUpperCase());
    }).map(function(account) {
      var buttonString = removePermission ? btnStr(account.netid) : '';
      return '<tr>' +
        '<td>' + account.firstName + '</td>' +
        '<td>' + account.lastName + '</td>' +
        '<td>' + account.netid + '</td>' +
        '<td>' + account.phone + '</td>' +
        buttonString +
        '</tr>';
    }).reduce(function(s1, s2) {
      return s1 + s2;
    }, '');
  });
});

/**
 * Sends a request to the server to remove a brother from the site
 * @param {string} netid - netid of member
 * @returns {undefined}
 */
function removeMember(netid) {
  var data = {netid: netid};
  sendRequest('DELETE', 'Accounts', data, 'text', true, null, function() {
    swal({
      title: 'User Deleted!',
      type: 'success'
    }, function() {
      window.location.reload();
    });
  }, function() {
    swal({
      title: 'User Deletion Failed :(',
      text: 'Please make sure you can delete this person',
      type: 'error'
    }, function(isConfirm) {
      window.location.reload();
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