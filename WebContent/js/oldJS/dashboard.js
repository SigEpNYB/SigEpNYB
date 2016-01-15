/*jshint -W083 */
$(document).ready(function() {
  getAccount();
  getRoles();
  var eventMsg = {
    "startTime": getAdjustedDate(-1, 0, 'UTC'),
    "endTime": getAdjustedDate(1, 0, 'UTC')
  };
  httpRequest('GET', 'Events', true, null, eventMsg, function(eventResp) {
    httpRequest('GET', 'Account', true, null, null, function(accountResp) {
      var accountId = accountResp["id"];
      for (var i=0; i < eventResp.length; i++) {
        var currEvent = eventResp[i];
        var eventObj = {
            "title": currEvent["title"],
            "description": currEvent["description"],
            "start": dateToTZ(new Date(currEvent["startTime"])),
            "end": dateToTZ(new Date(currEvent["endTime"]))
        };
        httpRequest('GET', 'Duties', true, null, {'idEvent': parseInt(currEvent["id"], 10)}, function(dutyResp, passThrough) {
          var hasDuty = false;
          var riskManager = "",
              setClean = "",
              sober = "",
              driver = "";
          for (var j=0; j<dutyResp.length; j++) {
            if (dutyResp[j]["idAccount"] == accountId) {
              hasDuty = true;
            }

            if (dutyResp[j]["type"] == 'RISKMANAGER') {
              riskManager += (dutyResp[j]["firstName"] + " " + dutyResp[j]["lastName"] + ",");
            } else if (dutyResp[j]["type"] == 'SETCLEAN') {
              setClean += (dutyResp[j]["firstName"] + " " + dutyResp[j]["lastName"] + ",");
            } else if (dutyResp[j]["type"] == 'SOBER') {
              sober += (dutyResp[j]["firstName"] + " " + dutyResp[j]["lastName"] + ",");
            } else if (dutyResp[j]["type"] == 'DRIVER') {
              driver += (dutyResp[j]["firstName"] + " " + dutyResp[j]["lastName"] + ",");
            }
            riskManager = trimLastChar(riskManager);
            setClean = trimLastChar(setClean);
            sober = trimLastChar(sober);
            driver = trimLastChar(driver);
          }
          var eventColor = (hasDuty ? 'red' : 'blue');
          passThrough["backgroundColor"] = eventColor;
          passThrough["borderColor"] = eventColor;
          passThrough["hasDuty"] = hasDuty;
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

function getRoles() {
  httpRequest('GET', 'Roles', true, null, null, function(resp) {
    var rolesHTML = '';
    for (var i = 0; i < resp.length; i++) {
      var role = resp[i];
      rolesHTML += '<h3>' + role.role + '</h3>';
      for (var j = 0; j < role.links.length; j++) {
        var link = role.links[j];
        rolesHTML += '<a href=' + link.href + '>' + link.pageName + '</a><br>';
      }
    }
    document.getElementById('roles').innerHTML = rolesHTML;
  });
}