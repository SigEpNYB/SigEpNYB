$(document).ready(function() {
  document.getElementById('tradeDuties').innerHTML = genDutyString();
});

function addDutyToList() {
  document.getElementById('tradeDuties').innerHTML += genDutyString();
}

function submitTrade() {
  var netid = document.getElementById('netid').value;
  var elements = [].slice.call(document.getElementsByClassName('tradeDuty'));
  elements = [].slice.call(elements.map(function(element) {
    return element.children;
  })).map(function(collection) {
    return [].slice.call(collection);
  });
  var tradeTypes = elements.map(function(element) {
    return element[0].value;
  });
  var eventId = elements.map(function(element) {
    return element[1].value;
  });
  var toFrom = elements.map(function(element) {
    return element[2].value;
  });
  var sendObjs = [];
  for (var i=0; i<tradeTypes.length; i++) {
    sendObjs.push({
      duty: tradeTypes[i],
      event: eventId[i],
      toFrom: toFrom[i],
      netid: netid
    });
  }
  sendTrade(data);
}

function sendTrade(data) {
  sendRequest('Duties', 'PUT', data, 'text', true, null, function() {
    swal({
      title: 'Duty Trade Submitted',
      type: 'success'
    }, function(isConfirm) {
      window.location.reload();
    })
  }, function(xhr) {
    swal({
      title: "Server Error",
      text: 'Error Code: ' + xhr.status,
      type: 'error'
    });
  });
}

function genDutyString() {
  return '<div class="tradeDuty">' +
    '<select class="form-control selectpicker tradeType">' +
      '<option>Risk Manager</option>' +
      '<option>Set/Clean</option>' +
      '<option>Sober</option>' +
      '<option>Driver</option>' +
    '</select>' +
    '<input class="form-control tradeEventId" type="number" min="0" placeholder="Event ID">' +
    '<select class="form-control selectpicker tradeToFrom">' +
      '<option>To</option>' +
      '<option>From</option>' +
    '</select>' +
    '<button type="submit" onclick="addDutyToList()" class="btn btn-primary addTrade">' +
      'Add Another' +
    '</button>' +
    '<br />' +
    '<br />' +
    '</div>';
}