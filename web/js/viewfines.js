sendRequest('GET', 'Fines', null, 'json', true, null, function(fines) {
  var finesString = fines.map(function(fine) {
    return '<tr>' +
    '<td>' + fine.reason + '</td>' +
    '<td>$' + fine.amount + '</td>' +
    '<td><a href="https://venmo.com/" class="btn btn-primary">Pay it on Venmo</a></td>' +
    '</tr>';
  }).reduce(addStr, '');
  document.getElementById('fines').innerHTML = finesString;
}, onSendFail);