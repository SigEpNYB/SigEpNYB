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