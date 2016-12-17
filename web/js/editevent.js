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

  sendRequest('GET', 'Events', null, 'json', true, data, function(event) {
    setIds(['title', 'description', 'riskManagers', 'setClean', 'sobers', 'drivers'], event[0]);
    setDates(['startTime', 'endTime'], event[0]);
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
  sendRequest('PUT', 'Events', data, 'text', true, null, function() {
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

$(document).ready(function() {
  $(".eventInput").keyup(function(event){
    if (event.keyCode === 13) addEvent();
  });

  $('#startTimeButton').datepicker({orientation: "top"})
    .on('changeDate', function(clickEvent) {
      $('#startTime').val(dateToPaddedString(clickEvent.date));
      $(this).datepicker('hide');
    });
  $('#endTimeButton').datepicker({orientation: "top"})
    .on('changeDate', function(clickEvent) {
      $('#endTime').val(dateToPaddedString(clickEvent.date));
      $(this).datepicker('hide');
    });
});
