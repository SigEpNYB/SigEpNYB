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
}, function() {
  swal({
    title: "Sorry, you don't have permission to approve account requests",
    text: 'Click OK to go back to your dashboard',
    type: 'error',
    closeOnConfirm: true
  }, function() {
    window.location.href = '/Fratsite/dashboard.html';
  })
});

function approveRequest(event, idRequest) {
  var data = {idRequest: idRequest};
  sendRequest('PUT', 'AccountRequests', data, 'text', true, null, function() {
    swal({
      title: 'Account Approved',
      type: 'success',
      closeOnConfirm: true
    }, function() {
      event.target.parentNode.parentNode.parentNode.removeChild(event.target.parentNode.parentNode);
    });
   }, function() {
    swal({
      title: 'Failed To Approve Account :(',
      text: 'Please Try again later',
      type: 'error',
      closeOnConfirm: true
    });
  });
}

function rejectRequest(event, idRequest) {
  var data = {idRequest: idRequest};
  sendRequest('DELETE', 'AccountRequests', data, 'text', true, null, function() {
    swal({
      title: 'Account Rejected',
      type: 'success',
      closeOnConfirm: true
    }, function() {
      event.target.parentNode.parentNode.parentNode.removeChild(event.target.parentNode.parentNode);
    });
  }, function() {
    swal({
      title: 'Failed To Reject Account :(',
      text: 'Please try again later',
      type: 'error',
      closeOnConfirm: true
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
  'onclick="approveRequest(event, \'' + idRequest + '\')">' +
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
  'onclick="rejectRequest(event, \'' + idRequest + '\')">' +
  'Reject</button>';
}