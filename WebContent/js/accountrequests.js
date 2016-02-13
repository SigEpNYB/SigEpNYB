checkPermissions(['accountRequests.view', 'accountRequests.accept', 'accountRequests.reject'],
  "Sorry, you don't have permission to approve account requests",
  'Go to "View Members" to see the list of brothers',
   true);

sendRequest('GET', 'AccountRequests', null, 'json', true, null, function(requests) {
  var requestBody = requests.map(function(request) {
    return '<tr>' +
      '<td>' + request.firstName + ' ' + request.lastName + '</td>' +
      '<td>' + request.netid + '</td>' +
      '<td>' + approveString(request.id) + '</td>' +
      '<td>' + rejectString(request.id) + '</td>' +
      '</tr>';
  }).reduce(function(s1, s2) {
    return s1 + s2;
  }, '');
  document.getElementById('requests').innerHTML = requestBody;
  }, function(xhr) {
  swal({
    title: "Server Error",
    text: 'Error Code: ' + xhr.status,
    type: 'error'
  });
});

/**
 * Sends an account approval request to the server
 * @param {number} idRequest - The request's ID number
 * @returns {undefined}
 */
function approveRequest(idRequest) {
  console.log(idRequest);
  var data = {idRequest: idRequest};
  sendRequest('PUT', 'AccountRequests', data, 'text', true, null, function() {
    swal({
      title: 'Account Approved',
      type: 'success'
    }, function() {
      window.location.reload();
    });
   }, function() {
    swal({
      title: 'Failed To Approve Account',
      text: 'Please Try again later',
      type: 'error'
    });
  });
}

/**
 * Sends an account reject request to the server
 * @param {number} idRequest - The request's ID number
 * @returns {undefined}
 */
function rejectRequest(idRequest) {
  var data = {idRequest: idRequest};
  sendRequest('DELETE', 'AccountRequests', data, 'text', true, null, function() {
    swal({
      title: 'Account Rejected',
      type: 'success'
    }, function() {
      window.location.reload();
    });
  }, function() {
    swal({
      title: 'Failed To Reject Account',
      text: 'Please try again later',
      type: 'error'
    });
  });
}

/**
 * Creates a button td element for approving requests
 * @param {string} idRequest
 * @returns {string}
 */
function approveString(idRequest) {
  return '<button type="button" ' +
  'class="btn btn-success" ' +
  'onclick="approveRequest(\'' + idRequest + '\')">' +
  'Approve</button>';
}

/**
 * Creates a button td element for rejecting requests
 * @param {string} idRequest
 * @returns {string}
 */
function rejectString(idRequest) {
  return '<button type="button" ' +
  'class="btn btn-danger" ' +
  'onclick="rejectRequest(\'' + idRequest + '\')">' +
  'Reject</button>';
}