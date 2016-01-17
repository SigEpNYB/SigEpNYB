sendRequest('GET', 'Roles', null, 'json', true, null, function(roles) {
  var hasPermission = roles.hasMatch(function(role) {
    return role.role === 'VP of Communication';
  });
  if (!hasPermission) {
    swal({
      title: "Sorry, you don't haver permission to assign duties",
      text: "Check the calendar to see your upcoming duties",
      type: 'error',
      closeOnConfirm: true
    }, function() {
      window.location.href = '/Fratsite/dashboard.html';
    });
  }
});

function getEvent() {
  var eventId = document.getElementById('eventId').value;
  var data = {eventId: eventId};
  sendRequest('GET', 'Event', null, 'json', true, data, function(event) {
    sendRequest('GET', 'Duties', null, 'json', true, data, function(duties) {
      var dutyObj = {
        riskManager: dutyFilter(duties, 'RISKMANAGER'),
        setClean: dutyFilter(duties, 'SETCLEAN'),
        sober: dutyFilter(duties, 'SOBER'),
        driver: dutyFilter(duties, 'DRIVER')
      };
      var riskManagerString = '<h5>Risk Managers</h5>';
      for (i=0; i<event.riskManagerCount; i++) {
        if (dutyObj.riskManager[i] !== undefined) {
          riskManagerString += "<input type='text' class='form-control' value='" + 
            dutyObj.riskManager[i].netid + "'>";
        } else {
          riskManagerString += "<input type='text' class='form-control'>";
        }
      }
      document.getElementById('riskManagers').innerHTML = riskManagerString;

      var setCleanString = '<h5>Set Clean</h5>';
      for (i=0; i<event.setCleanCount; i++) {
        if (dutyObj.setClean[i] !== undefined) {
          setCleanString += "<input type='text' class='form-control' value='" + 
            dutyObj.setClean[i].netid + "'>";
        } else {
          setCleanString += "<input type='text' class='form-control'>";
        }
      }
      document.getElementById('setClean').innerHTML = setCleanString;
      
      var soberString = '<h5>Sobers</h5>';
      for (i=0; i<event.soberCount; i++) {
        if (dutyObj.sober[i] !== undefined) {
          soberString += "<input type='text' class='form-control' value='" + 
            dutyObj.sober[i].netid + "'>";
        } else {
          soberString += "<input type='text' class='form-control'>";
        }
      }
      document.getElementById('sobers').innerHTML = soberString;
      
      var driverString = '<h5>Drivers</h5>';
      for (i=0; i<event.driverCount; i++) {
        if (dutyObj.driver[i] !== undefined) {
          driverString += "<input type='text' class='form-control' value='" + 
            dutyObj.driver[i].netid + "'>";
        } else {
          driverString += "<input type='text' class='form-control'>";
        }
      }
      document.getElementById('drivers').innerHTML = driverString;
    });
  });
}

function dutyFilter(duties, type) {
  return duties.filter(function(duty) { return duty.type === type; });
}