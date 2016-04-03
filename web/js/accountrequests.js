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
}, onSendFail);

/**
 * Sends an account approval request to the server
 * @param {number} idRequest - The request's ID number
 * @returns {undefined}
 */
function approveRequest(idRequest) {
  var data = {idRequest: idRequest};
  sendRequest('PUT', 'AccountRequests', data, 'text', true, null, function() {
    swal({
      title: 'Account Approved',
      type: 'success'
    }, function() {
      window.location.reload();
    });
   }, onSendFail);
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
  }, onSendFail);
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