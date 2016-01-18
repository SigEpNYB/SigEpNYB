sendRequest('GET', 'Roles', null, 'json', true, null, function(roles) {
  var hasPermission = roles.hasMatch(function(role) {
    return role.role === 'VP of Finance';
  });
  if (!hasPermission) {
    swal({
      title: "Sorry, you don't have permission to give fines",
      text: 'Go to "View My Fines" to see your fines',
      type: 'error',
      closeOnConfirm: true
    }, function(isConfirm) {
      window.location.href = 'dashboard.html';
    });
  }
});

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
  }, function() {
    swal({
      title: 'Fine Submission Failed',
      text: 'Please try again later',
      type: 'error',
      closeOnConfirm: true
    });
  });
}

function closeFine(fineId) {
  var data = {fineId: fineId};
  sendRequest('POST', 'FinesList', data, 'text', true, null, function() {
    swal({
      title: 'Fine Closed',
      text: 'Refresh the page to see the updated fines list',
      type: 'success',
      closeOnConfirm: true
    });
  }, function() {
    swal({
      title: 'Fine Failed To Close',
      text: 'Please try again later',
      type: 'error',
      closeOnConfirm: true
    });
  });
}

function deleteFine(fineId) {
  var data = {fineId: fineId};
  sendRequest('DELETE', 'FinesList', data, 'text', true, null, function() {
    swal({
      title: 'Fine Deleted',
      text: 'Refresh the page to see the updated fines list',
      type: 'success',
      closeOnConfirm: true
    });
  }, function() {
    swal({
      title: 'Fine Failed To Delete',
      text: 'Please try again later',
      type: 'error',
      closeOnConfirm: true
    });
  });
}

function updateFines() {
  var data = document.getElementById('fines').childNodes.map(function(row) {
    var td = row.childNodes[0];
    return {
      netid: td.childNodes[0].value,
      description: td.childNodes[1].value,
      amount: td.childNodes[2].value
    };
  });
  sendRequest('PUT', 'FinesList', data, 'text', true, null, function() {
    swal({
      title: 'Fines Updated',
      type: 'success',
      closeOnConfirm: true
    });
  }, function() {
    swal({
      fitle: 'Fine Updated Failed',
      text: 'Please try again later',
      type: 'error',
      closeOnConfirm: true
    })
  });
}