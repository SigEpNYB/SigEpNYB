function deleteEvent() {
  var eventId = document.getElementById('eventId').value;
  return sendDelete(eventId);
}

function sendDelete(eventId) {
  var data = {eventId: eventId};
  sendRequest('DELETE', 'Event', data, 'text', true, null, function() {
    swal({
      title: 'Event Deleted',
      type: 'success',
      confirmButtonText: 'Delete More Events',
      showCancelButton: true,
      cancelButtonText: 'Go To Dashboard',
      closeOnConfirm: true,
      closeOnCancel: true
    }, function(isConfirm) {
      if (isConfirm) clearIds(['eventId']);
      else window.location.href = 'dashboard.html';
    });
  }, function() {
    swal({
      title: 'Event Deletion Failed',
      text: 'Please make sure the event ID is correct',
      type: 'error',
      closeOnConfirm: true
    });
  });
}