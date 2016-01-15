$(document).ready(function() {
	$('#eventSubmit').on('click', function() {
		addEvent();
	})
	$(".eventInput").keyup(function(event){
	    if (event.keyCode == 13){
	        $("#eventSubmit").click();
	    }
	});
	$('#startTimeButton').datepicker({orientation: "top"}).on('changeDate', function(clickEvent) {
		$('#startTime').val(dateToPaddedString(clickEvent.date))
		$(this).datepicker('hide')
	})
	$('#endTimeButton').datepicker({orientation: "top"}).on('changeDate', function(clickEvent) {
		$('#endTime').val(dateToPaddedString(clickEvent.date))
		$(this).datepicker('hide')
	})
})

function addEvent() {
  var msg = buildObj('title', 'startTime', 'endTime', 'description');

  var startTime = new Date(msg['startTime']);
  startTime.setHours(startTime.getHours() + (new Date().getTimezoneOffset()/60));
  msg['startTime'] = startTime.getTime();
  var endTime = new Date(msg['endTime']);
  endTime.setHours(endTime.getHours() + (new Date().getTimezoneOffset()/60));
  msg['endTime'] = endTime.getTime();

  httpRequest('POST', 'Events', true, msg, null, function() {
    swal({title: "Event Created!", text: "Check your dashboard to see it on the calendar", type: "success", closeOnConfirm: true},
      function() {
        document.getElementById("title").value = "";
        document.getElementById("description").value = "";
        document.getElementById("startTime").value = "";
        document.getElementById("endTime").value = "";
      });
  }, function() {
    document.getElementById("status").innerHTML = "Event creation failed";
  });
}