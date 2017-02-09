setNetidMap();

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
            '<b>Drivers:</b> ' + dutyObj.driver + '\n';
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
    scrollTime: '12:00:00',
    events: [],
    editable: false,
    eventRender: function(event, element) {
      element[0].className = element[0].className + ' contextmenu';
      element[0].id = event.id;
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

  sendRequest('GET', 'Account', null, 'json', true, {showPermissions: true}, function(permissions) {
    if (permissions.indexOf('events.post') === -1) return;
    $.contextMenu({
      selector: '.contextmenu', 
      items: {
          "edit": {
            name: "Edit", 
            icon: "edit",
            callback: function(key, options) {
              var eventId = options.$trigger[0].id;
              window.location.href = 'editevent.html?eventId=' + eventId;
            }
          },
          "delete": {
            name: "Delete",
            icon: "delete",
            callback: function(key, options) {
              var eventId = options.$trigger[0].id;
              var data = {idEvent: eventId};
              sendRequest('DELETE', 'Events', data, 'text', true, null, function() {
                swal({
                  title: 'Event Deleted',
                  type: 'success',
                  confirmButtonText: 'Go To Dashboard',
                  closeOnConfirm: true,
                }, function() {
                    window.location.href = 'dashboard.html';
                });
              }, onSendFail);
            }
          }
        }
      });
  });
});

sendRequest('GET', 'Account', null, 'json', true, null, function(account) {
  document.getElementById('name').innerHTML = 
    '<h1>' + account.firstName + ' ' + account.lastName + '</h1>';
});

sendRequest('GET', 'Roles', null, 'json', true, null, function(roles) {
  document.getElementById('roles').innerHTML = roles.map(function(role) {
    return '<h3>' + role.role + '</h3>';
  }).reduce(function(s1, s2) {
    return s1 + s2;
  }, '');
});

var start = changeDateFormat(adjustDate(new Date(), 0, -1), 'UTC');
var end = changeDateFormat(new Date(), 'UTC');
var announcementData = {startTime: start, endTime: end};
sendRequest('GET', 'Announcements', null, 'json', true, announcementData, function(announcements) {
  document.getElementById('announcements').innerHTML = announcements.map(function(announcement) {
    return '<h4 class="announcementText">' + announcement.body + '</h4>' + 
           '<p class="announcementTime">' + (new Date(announcement.postTime)).toLocaleString() + '</p>';
  }).reduce(function(s1, s2) {
    return s1 + s2;
  }, '');
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
    if (duty.idAccount == 0) {
      return '';
    } else {
      var dutyPerson = accountidMap[duty.idAccount];
      return dutyPerson.firstName + ' ' + dutyPerson.lastName + ', '
    }
  }).reduce(addStr, '').trimLastChar().trimLastChar();
}
