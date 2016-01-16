$(document).ready(function() {
  var date = new Date();
  var eventData = {
    'startTime': changeDateFormat(adjustDate(date, -1), 'UTC'),
    'endTime': changeDateFormat(adjustDate(date, 1), 'UTC')
  };
  sendRequest('GET', 'Events', null, true, eventData, function(events) {
    sendRequest('GET', 'Account', null, true, null, function(account) {
      events.map(function(event) {
        var eventObj = {
          eventName: event.title,
          description: event.description,
          start: dateToTZ(new Date(event.startTime)),
          end: dateToTZ(new Date(event.endTime))
        };
        var eventId = {idEvent: parseInt(event.id, 10)}
        sendRequest('GET', 'Duties', null, true, eventId, function(duties) {
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
          var eventTitle = eventObj.eventName + '\n\n' +
            'Risk Managers: ' + dutyObj.riskManager + '\n' +
            'Set/Clean: ' + dutyObj.setClean + '\n' +
            'Sobers: ' + dutyObj.sober + '\n' +
            'Drivers: ' + dutyObj.driver;
          var dutyColors = {
            backgroundColor: eventColor,
            borderColor: eventColor,
            hasDuty: hasDuty,
            title: eventTitle
          };
          var calendarObj = $.extend({}, eventObj, dutyColors)
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
    editable: false
  });

  sendRequest('GET', 'Account', null, true, null, function(account) {
    document.getElementById('name').innerHTML = 
      account.firstName + ' ' + account.lastName;
  });

  sendRequest('GET', 'Roles', null, true, null, function(roles) {
    var roleString = roles.map(function(role) {
      return '<h3>' + role.role + '</h3>';
    }).reduce(function(s1, s2) {
      return s1 + s2;
    }, '');

    document.getElementById('roles').innerHTML = roleString;
  });
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
    return duty.firstName + ' ' + duty.lastName + ',';
  }).reduce(function(s1,s2) {
    return s1 + s2;
  }, '').trimLastChar();
}