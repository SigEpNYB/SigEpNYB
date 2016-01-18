sendRequest('GET', 'Fines', null, true, null, function(fines) {
  var finesString = fines.map(function(fine) {
    return '<tr>' +
    '<td>' + fine.description + '</td>' +
    '<td>$' + fine.amount + '</td>' +
    '<td><a href="https://venmo.com/" class="btn btn-primary">Pay it on Venmo</a></td>' +
    '</tr>';
  });
  document.getElementById('fines').innerHTML = finesString;
}, function() {
  swal({
    title: "Sorry, we couldn't get your fines :|",
    text: 'Please try again later',
    type: 'error',
    closeOnConfirm: true
  });
});