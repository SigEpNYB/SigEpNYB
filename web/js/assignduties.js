checkPermissions(['duties.create','duties.assign', 'duties.delete'],
  "Sorry, you don't have permission to assign duties",
  "Check the calendar to see your upcoming duties",
   true);

$(document).ready(function() {
  getUnassignedDuties();
});

setNetidMap();

var eventMap = {};
/**
 * Gets the event info for an event
 * @param {number} eventId - The ID associated with the requested event
 * @returns {undefined}
 */
function getUnassignedDuties() {
  var date = new Date();
  var eventData = {
    'startTime': changeDateFormat(adjustDate(date, -1), 'UTC'),
    'endTime': changeDateFormat(adjustDate(date, 1), 'UTC')
  };
  sendRequest('GET', 'Events', null, 'json', true, eventData, function(events) {
    events.map(function(event) {
      var start = new Date(event.startTime);
      var dateString = (start.getMonth() + 1) + '/' + start.getDate() + '/' + ('' + start.getFullYear()).slice(2);
      eventMap[event.id] = {date: dateString, title: event.title};
    });
    sendRequest('GET', 'Duties', null, 'json', true, null, function(duties) {
      duties.sort(function(duty1, duty2) {
        var duty1Date = new Date(eventMap[duty1.idEvent].date);
        var duty2Date = new Date(eventMap[duty2.idEvent].date);
        return duty1Date - duty2Date;
      }).map(function(duty) {
        var dutyEvent = eventMap[duty.idEvent];
        document.getElementById('duties').innerHTML += dutyString(duty.type, dutyEvent.title, dutyEvent.date, duty.id);
      });
      $('.typeahead').typeahead({
        hint: false,
        minLength: 2,
        highlight: true
      }, {
        name: 'Names',
        source: substringMatcher(nameList)
      });
    });
  });
}

function submitDuties() {
  var failed = false;
  Array.prototype.slice.call(document.getElementById('duties').childNodes).forEach(function(tr) {
    var input = tr.getElementsByClassName('form-control')[0];
    if (input.value !== '') {
      var account = nameMap[input.value];
      if (account !== undefined) {
        var data = {idDuty: input.id.slice(4), idAccount: account.id};
        sendRequest('PUT', 'Duties', data, 'text', true, null, null, function() {
          failed = true;
        });
      } else {
        swal({
          title: 'Invalid Name',
          closeOnConfirm: true,
          confirmButtonText: 'Try Again',
          text: 'Invalid Name ' + input.value,
          type: 'error'
        });
      }
    }
  });
  $(document).ajaxStop(function() {
    if (!failed) {
      swal({
          title: 'Duties Assigned',
          type: 'success',
          confirmButtonText: 'Assign More Duties',
          showCancelButton: true,
          cancelButtonText: 'Go To Dashboard',
          closeOnConfirm: true,
          closeOnCancel: true
      }, function(isConfirm) {
        if (isConfirm) window.location.reload();
        else window.location.href = 'dashboard.html';
      });
    } else {
      onSendFail(null, reloadPage);
    }
  });
}

function dutyString(type, title, date, dutyId) {
  return '<tr>' + 
  '<td>' + type.toProperCase() + '</td>' +
  '<td>' + title + '</td>' +
  '<td>' + date + '</td>' +
  '<td><input type="text" id="duty' + dutyId + '" class="form-control typeahead" placeholder="Name"></td>' +
  '</tr>'
}