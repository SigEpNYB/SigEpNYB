$(document).ready(function() {
  $('.eventInput').hide();
});

checkPermissions(['EditEvent'],
  "Sorry, you don't have permission to edit events",
  "Talk to the VP of Programming to edit an event",
  true);

function getEvent() {
  var eventId = document.getElementById('eventId').value;
  return sendGetEvent(eventId);
}

/**
 * Get event info for eventId
 * @param {number} eventId
 * @returns {undefined}
 */
function sendGetEvent(eventId) {
  var data = {eventId: eventId};
  sendRequest('GET', 'Events', null, 'json', data, function(event) {
    setIds(['title', 'startTime', 'endTime', 'description', 'riskManagers',
      'setClean', 'sobers', 'drivers'], event);
  }, function() {
    swal({
      title: 'Invalid Event ID',
      text: 'Please check to make sure the event ID you entered is correct',
      type: 'success'
    });
  });
  $('.eventInput').show();
}

function updateEvent() {
  var data = buildObj(['title', 'startTime', 'endTime', 'description', 'riskManagers',
      'setClean', 'sobers', 'drivers']);
  return sendUpdateEvent(data);
}

/**
 * Sends and update event request to the server
 * @param {Object} data - Must contain title, startTime, endTime, description,
 * riskmanagers, setClean, sobers, and drivers
 */
function sendUpdateEvent(data) {
  sendRequest('PUT', 'Events', data, 'text', null, function() {
    swal({
      title: 'Updated Event',
      type: 'success'
    }, function() {
      window.location.reload();
    });
  }, function() {
    swal({
      title: 'Event failed to update',
      text: 'Please make sure all of the fields are filled out',
      type: 'error'
    });
  });
}