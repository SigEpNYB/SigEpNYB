sendRequest('GET', 'AccountRequests', null, 'json', true, null, function(requests) {
  var requestBody = requests.map(function() {
    return '<tr>' +
      '<td>' + request.firstName + '</td>' +
      '<td>' + request.lastName + '</td>' +
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
    text: 'Click OK to go back to your dashboard'
    type: 'error',
    closeOnConfirm: true
  }, function() {
    window.location.href = '/Fratsite/dashboard.html';
  })
});

function approveRequest() {
  
}

/**
 * Creates a button td element for approving requests
 * @param {string} id
 * @returns {string}
 */
function approveString(id) {
  return '<button type="button" ' +
  'class="btn btn-success" ' + 
  'onclick="approveRequest(\'' + id + '\')">' +
  'Approve</button>';
}

/**
 * Creates a button td element for rejecting requests
 * @param {string} id
 * @returns {string}
 */
function rejectString(id) {
  return '<button type="button" ' +
  'class="btn btn-success" ' + 
  'onclick="rejectRequest(\'' + id + '\')">' +
  'Approve</button>';
}