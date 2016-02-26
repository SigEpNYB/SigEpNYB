function sendProgrammingEmail() {
  sendRequest('POST', 'Email', {type: 'Programming'}, 'text', true, null, function() {
    swal({
      title: 'Programming Email Sent',
      text: 'The email was sent to the sigepnyb-discussion group',
      type: 'success'
    })
  }, function(xhr) {
    swal({
      title: 'Email Sending Failed',
      text: 'Error Code: ' + xhr.status,
      type: 'error'
    });
  });
}

function sendDutiesEmail() {
  sendRequest('POST', 'Email', {type: 'Duties'}, 'text', true, null, function() {
    swal({
      title: 'Duties Email Sent',
      text: 'The email was sent to the sigepnyb-discussion group',
      type: 'success'
    })
  }, function(xhr) {
    swal({
      title: 'Email Sending Failed',
      text: 'Error Code: ' + xhr.status,
      type: 'error'
    });
  });
}

function sendFinesEmail() {
  sendRequest('POST', 'Email', {type: 'Fines'}, 'text', true, null, function() {
    swal({
      title: 'Fines Email Sent',
      text: 'The email was sent to the sigepnyb-discussion group',
      type: 'success'
    })
  }, function(xhr) {
    swal({
      title: 'Email Sending Failed',
      text: 'Error Code: ' + xhr.status,
      type: 'error'
    });
  });
}

$(document).ready(function() {
  sendRequest('GET', 'Account', null, 'json', true, {showPermissions: true}, function(permissions) {
    if (!permissions.contains('events.post')) {
      $('#programmingEmail').hide();
    }
    if (!permissions.contains('duties.assign')) {
      $('#dutiesEmail').hide();
    }
    if (!permissions.contains('fines.create')) {
      $('#finesEmail').hide();
    }
  });
});