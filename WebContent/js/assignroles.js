sendRequest('GET', 'Roles', null, 'json', true, null, function(roles) {
  var hasPermission = roles.hasMatch(function(role) {
    return role.role === 'President';
  });
  if (!hasPermission) {
    swal({
      title: "Sorry, you don't have permission to assign roles",
      text: 'Go to "View Members" to see the list of brothers',
      type: 'error',
      closeOnConfirm: true
    }, function(isConfirm) {
      window.location.href = 'dashboard.html';
    });
  }
});

function getRoles() {
  var netid = document.getElementById('netid');
  return sendGetRoles(netid);
}

/**
 * Gets the roles for a netid
 * @param {string} netid
 * @returns {undefined}
 */
function sendGetRoles(netid) {
  var data = {netid: netid};
  sendRequest('GET', 'Roles', null, 'json', true, data, function(roles) {
    var roleStrings = roles.map(function(role) {
      return '<tr>' +
      '<td>' + role.role + '</td>' +
      '<td>' + roleRemoveButton(netid, role.role) + '</td>' +
      '</tr>';
    }).fold(function(s1, s2) {
      return s1 + s2;
    }, '');
    document.getElementById('roles').innerHTML = roleStrings;
  }, function() {
    swal({
      title: "Looks like we couldn't find that user",
      text: "Please make sure you spelled the netid correctly",
      type: 'error',
      closeOnConfirm: true
    });
  });
}

/**
 * Sends a remove role request to the server
 * @param {ClickEvent} event
 * @param {string} netid
 * @param {string} role
 * @returns {undefined}
 */
function removeRole(event, netid, role) {
  var data = {
    netid: netid,
    role: role
  };
  sendRequest('DELETE', 'Roles', data, 'text', true, null, function() {
    swal({
      title: 'Role Removed',
      text: 'Refresh the page to see the chages'
      type: 'success',
      closeOnConfirm: true
    });
  }, function() {
    swal({
      title: 'Role Removal Failed',
      text: 'Please try again later',
      type: 'error',
      closeOnConfirm: true
    });
  });
}

function submitNewRole() {
  var data = buildObj(['netid', 'newRole']);
  return sendNewRole(data);
}

/**
 * Sends a new role to the server
 * @param {Object} data - contains netid and newRole
 * @returns {undefined}
 */
function sendNewRole(data) {
  sendRequest('POST', 'Roles', data, 'text', true, null, function() {
    swal({
      title: 'Role Added',
      text: 'Refresh the page to see the chages'
      type: 'success',
      closeOnConfirm: true
    });
  }, function() {
    swal({
      title: 'Role Removal Failed',
      text: 'Please make sure the netid and role are spelled correctly',
      type: 'error',
      closeOnConfirm: true
    });
  });
}

/**
 * Creates a button td element for removing a member's roles
 * @param {string} netid
 * @returns {string}
 */
function roleRemoveButton(netid, role) {
  return '<button type="button" ' +
  'class="btn btn-danger" ' + 
  'onclick="removeRole(\'' + netid + '\', \'' + role + '\')">' +
  'Remove</button>';
}