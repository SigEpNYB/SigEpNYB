checkPermissions(['fines.create', 'fines.viewall', 'fines.delete'],
  "Sorry, you don't have permission to assign fines",
  'Go to "View My Fines" to see your fines',
  true);

setNetidMap();

$(document).ready(function() {
  getFines();
});

function getFines() {
  sendRequest('GET', 'Fines', null, 'json', true, {showAll: true}, function(fines) {
    var fineString = fines.map(function(fine) {
    return '<tr>' +
      '<td>' + accountidMap[fine.idAccount].firstName + ' ' + accountidMap[fine.idAccount].lastName + '</td>' +
      '<td>' + fine.reason + '</td>' +
      '<td>' + '$' + fine.amount.toFixed(2) + '</td>' +
      '<td><button onclick="deleteFine(' + fine.idFine + ')" class="btn btn-danger">Delete/Close</button></td>' +
      '</tr>';
    }).reduce(addStr, '');
    document.getElementById('fines').innerHTML = fineString;

  });
}

$('.typeahead').typeahead({
  hint: false,
  minLength: 2,
  highlight: true
}, {
  name: 'Names',
  source: substringMatcher(nameList)
});

function submitFine() {
  var data = buildObj(['name', 'reason', 'amount']);
  data.idAccount = nameMap[data.name];
  if (data.idAccount !== undefined) {
    delete data.name;
    data.idAccount = data.idAccount.id;
    sendFine(data);
  } else {
    swal({
      title: 'Invalid Name',
      closeOnConfirm: true,
      confirmButtonText: 'Try Again',
      text: 'Invalid Name ' + data.name,
      type: 'error'
    });
  }
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