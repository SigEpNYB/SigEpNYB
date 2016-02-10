/*checkPermissions(['AssignFines'],
  "Sorry, you don't have permission to give fines",
  'Go to "View My Fines" to see your fines',
  true);
*/
setNetidMap();

sendRequest('GET', 'Fines', null, 'json', true, {showAll: true}, function(fines) {
  var fineString = fines.map(function(fine) {
  return '<tr>' +
    '<td>' + accountidMap[fine.idAccount] + '</td>' +
    '<td>' + fine.reason + '</td>' +
    '<td>' + fine.amount.toFixed(2) + '</td>' +
    '<td><button onclick="deleteFine(' + fine.idFine + ')" class="btn btn-danger">Delete/Close</button></td>' +
    '</tr>';
  }).reduce(function(s1, s2) {
    return s1 + s2;
  });
  document.getElementById('fines').innerHTML = fineString;
});

function submitFine() {
  var data = buildObj(['netid', 'reason', 'amount']);
  data.idAccount = netidMap[data.netid];
  delete data.netid;
  sendFine(data);
}

/**
 * Sends a fine to the server
 * @param {Object} data - Should contain netid, description and amount
 * @returns {undefined}
 */
function sendFine(data) {
  sendRequest('POST', 'Fines', data, 'text', true, null, function() {
    swal({
      title: 'Fine Submitted',
      type: 'success',
      confirmButtonText: 'Submit More Fines',
      showCancelButton: true,
      cancelButtonText: 'Go To Dashboard',
      closeOnConfirm: true,
      closeOnCancel: true
    }, function(isConfirm) {
      if (isConfirm) window.location.reload();
      else window.location.href = 'dashboard.html';
    });
  }, function(xhr) {
    swal({
      title: "Please make sure the Netid you entered is valid",
      text: 'Error Code: ' + xhr.status,
      type: 'error'
    });
  });
}

/**
 * Sends a delete fine request to the server
 * @param {number} fineId - ID of actual fine
 * @returns {undefined}
 */
function deleteFine(fineId) {
  var data = {idFine: fineId};
  sendRequest('DELETE', 'Fines', data, 'text', true, null, function() {
    swal({
      title: 'Fine Deleted',
      type: 'success'
    }, function(isConfirm) {
      window.location.reload();
    });
  }, function(xhr) {
    swal({
      title: "Server Error",
      text: 'Error Code: ' + xhr.status,
      type: 'error'
    });
  });
}