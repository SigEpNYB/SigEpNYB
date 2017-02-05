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
  var duties = buildObj(['RISKMANAGER', 'SETCLEAN', 'SOBER', 'DRIVER']);

  startTime.setHours(startTime.getHours() + offset);
  endTime.setHours(endTime.getHours() + offset);
  data.startTime = startTime.getTime();
  data.endTime = endTime.getTime();
  data.duties = duties;

  sendRequest('POST', 'Events', data, 'json', true, null, function() {
    swal({
      title: 'Event Created!',
      text: 'Check your dashboard to see it on the calendar',
      type: 'success'
    }, function() {
      clearIds(['title', 'description', 'startTime', 'endTime', 'endTime',
        'RISKMANAGER', 'SETCLEAN', 'SOBER', 'DRIVER']);
    });
  }, function() {
    swal({
      title: 'Event Creation Failed',
      text: 'Please make sure all of the fields are filled out',
      type: 'error'
    });
  });
}