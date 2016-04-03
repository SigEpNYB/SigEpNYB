function submitAppeal() {
  var data = buildObj(['fines', 'reason']);
  return sendAppeal(data);
}

/**
 * Sends an appeal to the server
 * @param {Object} data - must contain fines and reason
 * @returns {undefined}
 */
function sendAppeal(data) {
  sendRequest('POST', 'Appeals', data, 'text', true, null, function() {
    swal({
      title: 'Appeal Submitted',
      text: 'Please go to standards board on sunday to formally appeal',
      type: 'success'
    }, function(isConfirm) {
      clearIds(['fines', 'reason']);
    });
  }, onSendFail);
}