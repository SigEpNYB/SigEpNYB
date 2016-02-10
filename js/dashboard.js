$(document).ready(function() {
  var date = new Date();
  var eventData = {
    'startTime': changeDateFormat(adjustDate(date, -1), 'UTC'),
    'endTime': changeDateFormat(adjustDate(date, 1), 'UTC')
  };
  sendRequest('GET', 'Events', null, 'json', true, eventData, function(events) {
    sendRequest('GET', 'Account', null, 'json', true, null, function(account) {
      events.map(function(event) {
        var eventObj = {
          name: event.title,
          id: event.id,
          description: event.description,
          start: new Date(event.startTime),
          end: new Date(event.endTime)
        };
        var eventId = {idEvent: parseInt(event.id, 10)}
        sendRequest('GET', 'Duties', null, 'json', true, eventId, function(duties) {
          var hasDuty = duties.hasMatch(function(duty) {
            return duty.idAccount === account.id;
          });
          var dutyObj = {
            riskManager: dutyMapReduce(duties, 'RISKMANAGER'),
            setClean: dutyMapReduce(duties, 'SETCLEAN'),
            sober: dutyMapReduce(duties, 'SOBER'),
            driver: dutyMapReduce(duties, 'DRIVER')
          };
          var eventColor = (hasDuty ? 'red' : 'blue');
          var dutyString = event.description + '\n\n' +
            '<b>Risk Managers:</b> ' + dutyObj.riskManager + '\n' +
            '<b>Set/Clean:</b> ' + dutyObj.setClean + '\n' +
            '<b>Sobers:</b> ' + dutyObj.sober + '\n' +
            '<b>Drivers:</b> ' + dutyObj.driver + '\n\n' +
            '<b>ID:</b> ' + event.id;       
          var overlayText = event.title + '\n\n' + event.description;
          var dutyColors = {
            backgroundColor: eventColor,
            borderColor: eventColor,
            hasDuty: hasDuty,
            title: overlayText,
            dutyString: dutyString
          };
          var calendarObj = $.extend({}, eventObj, dutyColors, dutyObj);
          $('#calendar').fullCalendar('renderEvent', calendarObj, true);
        });
      });
    });
  });

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
        content: {
          title: event.name,
          text: event.dutyString
        },
        style: {
          classes: 'qtip_fratsite'
        }
      });
    }
  });
});

sendRequest('GET', 'Account', null, 'json', true, null, function(account) {
  document.getElementById('name').innerHTML = 
    '<h1>' + account.firstName + ' ' + account.lastName + '</h1>';
});

sendRequest('GET', 'Roles', null, 'json', true, null, function(roles) {
  var roleString = roles.map(function(role) {
    return '<h3>' + role.role + '</h3>';
  }).reduce(function(s1, s2) {
    return s1 + s2;
  }, '');
  document.getElementById('roles').innerHTML = roleString;
});

/**
 * Filters a list of duties for the ones that match string
 * and converts them into a string of names
 * @param {Object[]} duties - List of duties
 * @param {string} type - Type of duty being looked for
 * @returns {string}
 */
function dutyMapReduce(duties, type) {
  return duties.filter(function(duty) {
    return duty.type === type;
  }).map(function(duty) {
    return duty.firstName + ' ' + duty.lastName + ', ';
  }).reduce(function(s1,s2) {
    return s1 + s2;
  }, '').trimLastChar().trimLastChar();
}