checkPermissions(['AssignFines'],
  "Sorry, you don't have permission to give fines",
  'Go to "View My Fines" to see your fines',
  true);

sendRequest('GET', 'FinesList', null, 'json', true, null, function(fines) {
  var fineString = fines.map(function(fine) {
  return '<tr>' +
    '<td><input type="text" class="form-control" value="' + fine.netid + '">' + '</td>' +
    '<td><input type="text" class="form-control" value="' + fine.description + '">' + '</td>' +
    '<td><input type="text" class="form-control" value="' + fine.amount + '">' + '</td>' +
    '<td><button onclick="closeFine(' + fine.id +')" class="btn btn-success">Close</button></td>' +
    '<td><button onclick="deleteFine(' + fine.id + ')" class="btn btn-error">Delete</button></td>' +
    '</tr>';
  });
});

function submitFine() {
  var data = buildObj(['netid', 'description', 'amount']);
  sendFine(data);
}

/**
 * Sends a fine to the server
 * @param {Object} data - Should contain netid, description and amount
 * @returns {undefined}
 */
function sendFine(data) {
  sendRequest('POST', 'Fines', null, 'text', true, data, function() {
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
      title: "Server Error",
      text: 'Error Code: ' + xhr.status,
      type: 'error'
    });
  });
}

/**
 * Sends a close fine request to the server
 * @param {number} fineId - ID of actual fine
 * @returns {undefined}
 */
function closeFine(fineId) {
  var data = {fineId: fineId};
  sendRequest('POST', 'FinesList', data, 'text', true, null, function() {
    swal({
      title: 'Fine Closed',
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

/**
 * Sends a delete fine request to the server
 * @param {number} fineId - ID of actual fine
 * @returns {undefined}
 */
function deleteFine(fineId) {
  var data = {fineId: fineId};
  sendRequest('DELETE', 'FinesList', data, 'text', true, null, function() {
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

function updateFines() {
  var data = document.getElementById('fines').childNodes.map(function(tr) {
    var td = tr.childNodes[0];
    return {
      netid: td.childNodes[0].value,
      description: td.childNodes[1].value,
      amount: td.childNodes[2].value
    };
  });
  return sendFinesUpdate(data);
}

/**
 * Updates the fines list
 * @param {Object[]} data - List of fine objects
 * @returns {undefined}
 */
function sendFinesUpdate(data) {
  sendRequest('PUT', 'FinesList', data, 'text', true, null, function() {
    swal({
      title: 'Fines Updated',
      type: 'success'
    });
  }, function(xhr) {
    swal({
      title: "Server Error",
      text: 'Error Code: ' + xhr.status,
      type: 'error'
    });
  });
}