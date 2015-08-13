$(document).ready(function() {
  getAccount();
  getRoles();

  var eventMsg = {
    "startTime": getAdjustedDate(-1, 0, 'UTC'),
    "endTime": getAdjustedDate(1, 0, 'UTC')
  };
  httpRequest('GET', 'Events', true, null, eventMsg, function(eventResp) {
    httpRequest('GET', 'Account', true, null, null, function(accountResp) {
      accountId = accountResp["id"];
      var displayableEvents = [];
      for (i=0; i < eventResp.length; i++) {
        var currEvent = eventResp[i];
        var eventId = parseInt(currEvent["id"], 10);
        var dutyMsg = {'idEvent': eventId};
        var eventObj = {
            "id": eventId,
            "title": currEvent["title"],
            "description": currEvent["description"],
            "start": dateToTZ(new Date(currEvent["startTime"])),
            "end": dateToTZ(new Date(currEvent["endTime"]))
        };
        httpRequest('GET', 'Duties', true, null, dutyMsg, function(dutyResp, passThrough) {
          if (dutyResp.length === 0) {
            hasDuty = false;
          }

          var hasDuty = false;
          var k = 0;
          while ((!hasDuty) && (k < dutyResp.length)) {
            if (dutyResp[k]["idAccount"] == accountId) {
              hasDuty = true;
            }
          }
          var eventColor = (hasDuty ? 'red' : 'blue');
          passThrough["backgroundColor"] = eventColor;
          passThrough["borderColor"] = eventColor;
          passThrough["hasDuty"] = hasDuty;
          displayableEvents.push(eventObj);
          $('#calendar').fullCalendar('renderEvent', passThrough, true);
        }, null, null, eventObj);
      }

        $('#calendar').fullCalendar({
          header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
          },
          defaultView: 'agendaWeek',
          events: [],
          editable: false,
          eventRender: function(event, element) {
            element.qtip({
              content: {text: event.description, title: event.title},
              style: {classes: ((event.hasDuty) ? 'qtip-red' : 'qtip-blue')}
            });
          }
        });
    });
    });
});