$(document).ready(function() {
	getAccount();
	getRoles();
	getEvents();

	var today = new Date();
	var startDay = today.getDate();
	startDay = (startDay < 10 ? '0' + startDay : startDay);
	var startMonth = today.getMonth();
	startMonth = (startMonth < 10 ? '0' + startMonth : startMonth);
	var startYear = today.getFullYear() - 1;
	var startTime = startYear + '-' + startMonth + '-' + startDay + "T01:01";

	var endDay = startDay;
	var endMonth = startMonth;
	var endYear = startYear + 2;
	var endTime = endYear + '-' + endMonth + '-' + endDay + "T01:01";

	var msg = {
		"startTime": startTime,
		"endTime": endTime
	}
	httpRequest('GET', 'Events', true, null, msg, function(resp) {
		console.log(resp)
		displayableEvents = []
		for (i=0; i < resp.length; i++) {
			var eventResp = resp[i]
			var eventObj = {
				"id": parseInt(eventResp["id"]),
				"title": eventResp["title"],
				"description": eventResp["description"],
				"start": eventResp["startTime"].substring(24,28) + "-" + monthToNumber(eventResp["startTime"].substring(4,7)) + "-" + eventResp["startTime"].substring(8,10) + "T" +
						 eventResp["startTime"].substring(11,13) + ":" + eventResp["startTime"].substring(14,16) + ":" +  eventResp["startTime"].substring(17,19),
				"end": eventResp["endTime"].substring(24,28) + "-" + monthToNumber(eventResp["endTime"].substring(4,7)) + "-" + eventResp["endTime"].substring(8,10) + "T" +
						 eventResp["endTime"].substring(11,13) + ":" + eventResp["endTime"].substring(14,16) + ":" +  eventResp["endTime"].substring(17,19)
			}
			displayableEvents.push(eventObj)
		}
	    $('#calendar').fullCalendar({
	    	header: {
	    		left: 'prev,next today',
	    		center: 'title',
	    		right: 'month,agendaWeek,agendaDay'
	    	},
	    	events: displayableEvents,
	    	editable: false
	    });
    });
});