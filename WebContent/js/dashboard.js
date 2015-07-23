$(document).ready(function() {
	getAccount();
	getRoles();

	var today = new Date();
	var startCalendar = new Date();
	startCalendar.setYear(today.getFullYear() - 1)
	startCalendar = startCalendar.getTime()
	var endCalendar = new Date();
	endCalendar.setYear(today.getFullYear() + 1)
	endCalendar = endCalendar.getTime()

	var msg = {
		"startTime": startCalendar,
		"endTime": endCalendar
	}
	httpRequest('GET', 'Events', true, null, msg, function(resp) {
		displayableEvents = []
		for (i=0; i < resp.length; i++) {
			var eventResp = resp[i]
			var startTime = new Date(eventResp["startTime"])
			startTime.setHours(startTime.getHours() - (new Date().getTimezoneOffset()/60))

			var endTime = new Date(eventResp["endTime"])
			endTime.setHours(endTime.getHours() - (new Date().getTimezoneOffset()/60))

			var eventObj = {
				"id": parseInt(eventResp["id"]),
				"title": eventResp["title"],
				"description": eventResp["description"],
				"start": startTime.toISOString().slice(0,-8),
				"end": endTime.toISOString().slice(0,-8)		
			}
			displayableEvents.push(eventObj)
		}
	    $('#calendar').fullCalendar({
	    	header: {
	    		left: 'prev,next today',
	    		center: 'title',
	    		right: 'month,agendaWeek,agendaDay'
	    	},
	    	defaultView: 'agendaWeek',
	    	events: displayableEvents,
	    	editable: false
	    });
    });
});