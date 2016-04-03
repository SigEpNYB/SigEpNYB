/*checkPermissions(['AssignRoles'],
  "Sorry, you don't have permission to assign roles",
  'Go to "View Members" to see the list of brothers',
  true);
*/
setNetidMap();

/**
 * Gets the roles for a netid
 * @param {string} accountId
 * @returns {undefined}
 */
function getRoles(accountId) {
  var netid = document.getElementById('netid');
  var accountId = sendGetRoles(netidMap[netid].id);
  var data = {accountId: accountId};
  sendRequest('GET', 'Roles', null, 'json', true, data, function(roles) {
    var roleStrings = roles.map(function(role) {
      return '<tr>' +
      '<td>' + role.role + '</td>' +
      '<td>' + roleRemoveButton(accountId, role.role) + '</td>' +
      '</tr>';
    }).fold(function(s1, s2) {
      return s1 + s2;
    }, '');
    document.getElementById('roles').innerHTML = roleStrings;
  }, function() {
    swal({
      title: "Looks like we couldn't find that user",
      text: "Please make sure you spelled the netid correctly",
      type: 'error'
    });
  });
}

/**
 * Sends a remove role request to the server
 * @param {string} accountId
 * @param {string} role
 * @returns {undefined}
 */
function removeRole(accountId, role) {
  var data = {
    accountId: accountId,
    role: role
  };
  sendRequest('DELETE', 'Roles', data, 'text', true, null, function() {
    swal({
      title: 'Role Removed',
      text: 'Refresh the page to see the chages',
      type: 'success'
    });
  }, onSendFail);
}

/**
 * Sends a new role to the server
 * @param {Object} data - contains netid and newRole
 * @returns {undefined}
 */
function submitNewRole() {
  var data = buildObj(['netid', 'newRole']);
  sendRequest('POST', 'Roles', data, 'text', true, null, function() {
    swal({
      title: 'Role Added',
      text: 'Refresh the page to see the chages',
      type: 'success'
    });
  }, onSendFail);
}

/**
 * Creates a button td element for removing a member's roles
 * @param {string} accountId
 * @returns {string}
 */
function roleRemoveButton(accountId, role) {
  return '<button type="button" ' +
  'class="btn btn-danger" ' + 
  'onclick="removeRole(\'' + accountId + '\', \'' + role + '\')">' +
  'Remove</button>';
}