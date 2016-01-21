function addDutyToList() {
  document.getElementById('tradeDuties').innerHTML += '<div class="tradeDuty">' +
    '\n<select class="form-control selectpicker tradeType">' +
      '<option>Risk Manager</option>' +
      '<option>Set/Clean</option>' +
      '<option>Sober</option>' +
      '<option>Driver</option>' +
    '</select>' +
    '<input class="form-control tradeEventId2" type="text" placeholder="Event ID">' +
    '<select class="form-control selectpicker tradeToFrom2">' +
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