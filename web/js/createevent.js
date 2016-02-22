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

checkPermissions(['events.post'],
  "Sorry, you don't have permission to create events",
  "Talk to the VP of Programming to create an event",
  true);

function addEvent() {
  var data = buildObj(['title', 'description', 'startTime', 'endTime']);
  return sendEvent(data);
}

/**
 * Sends a new event to the server
 * @param {Object} data - Must contain title, description, startTime,
 * endTime, riskManagers, setClean, sobers and drivers
 * @returns {undefined}
 */
function sendEvent(data) {
  var startTime = new Date(data.startTime);
  var endTime = new Date(data.endTime);
  var offset = (new Date()).getTimezoneOffset() / 60;
  var counts = buildObj(['riskManagers', 'setClean', 'sobers', 'drivers']);

  startTime.setHours(startTime.getHours() + offset);
  endTime.setHours(endTime.getHours() + offset);
  data.startTime = startTime.getTime();
  data.endTime = endTime.getTime();

  sendRequest('POST', 'Events', data, 'json', true, null, function(data) {
    sendDuties('RISKMANAGER', data.idEvent, counts.riskManagers);
    sendDuties('SETCLEAN', data.idEvent, counts.setClean);
    sendDuties('SOBER', data.idEvent, counts.sobers);
    sendDuties('DRIVER', data.idEvent, counts.drivers);
    swal({
      title: 'Event Created!',
      text: 'Check your dashboard to see it on the calendar',
      type: 'success'
    }, function() {
      clearIds(['title', 'description', 'startTime', 'endTime', 'endTime',
        'riskManagers', 'setClean', 'sobers', 'drivers']);
    });
  }, function() {
    swal({
      title: 'Event Creation Failed',
      text: 'Please make sure all of the fields are filled out',
      type: 'error'
    });
  });
}

function sendDuties(dutyName, idEvent, n) {
  for (var i = 0; i<n; i++) {
    var data = {type: dutyName, idEvent: idEvent};
    console.log(data);
    sendRequest('POST', 'Duties', data, 'text', true, null);
  }
}