$(document).ready(function() {
	$('#createAccountButton').on('click', function() {
		if ($('#password').val() != $('#passwordConfirm').val()) {
			swal({title: "Oops", text: "Your passwords didn't match", type: "error"});
			document.getElementById('password').value = '';
			document.getElementById('passwordConfirm').value = '';
		} else if (($('#firstName').val() == '') || ($('#lastName').val() == '') || ($('#netid').val() == '') || ($('#password').val() == '')) {
			swal({title: "Oops", text: "One of the fields was empty", type: "error"})
		} else {
			addAccount();
		}
	})
	$(".loginInput").keyup(function(event){
	    if (event.keyCode == 13){
	        $("#createAccountButton").click();
	    }
	});
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