/*
checkPermissions(['EditEvent'],
  "Sorry, you don't have permission to edit events",
  "Talk to the VP of Programming to edit an event",
  true);
*/

var start = window.location.href.indexOf('?eventId=') + '?eventId='.length;
if (start !== -1) {
  var idEvent = parseInt(window.location.href.substring(start));
  var data = {idEvent: idEvent};

  sendRequest('GET', 'Events', null, 'json', data, function(event) {
    setIds(['title', 'startTime', 'endTime', 'description', 'riskManagers',
      'setClean', 'sobers', 'drivers'], event)
  }, function() {
    swal({
      title: 'Invalid Event ID',
      text: 'Please check to make sure the event ID you entered is correct',
      type: 'success'
    });
  });
}

function updateEvent() {
  var data = buildObj(['title', 'startTime', 'endTime', 'description', 
    'riskManagers', 'setClean', 'sobers', 'drivers']);
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