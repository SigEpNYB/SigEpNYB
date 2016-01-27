checkPermissions(['DeleteEvent'], 
  "Sorry, you don't have permission to delete events",
  "Talk to the VP of Programming to delete an event",
  true);

function deleteEvent() {
  var eventId = document.getElementById('eventId').value;
  return sendDelete(eventId);
}

/**
 * Sends a delete event request to the server
 * @param {number} eventId - ID Number of event
 * @returns {undefined}
 */
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
      type: 'error'
    });
  });
}