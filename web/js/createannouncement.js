checkPermissions(['announcements.post'],
  "Sorry, you don't have permission to create announcements",
  "Talk to the President to create an announcement",
  true);

function addAnnouncement() {
  var data = buildObj(['body']);
  return sendAnnouncement(data);
}

/**
 * Sends a new announcement to the server
 * @param {Object} data - Must contain body
 * @returns {undefined}
 */
function sendAnnouncement(data) {
  sendRequest('POST', 'Announcements', data, 'json', true, null, function(data) {
    swal({
      title: 'Announcement Posted!',
      text: 'Check your dashboard to see all announcements',
      type: 'success'
    }, function() {
      clearIds(['body']);
    });
  }, function() {
    swal({
      title: 'Announcement Creation Failed',
      text: 'Please make sure the body is filled out',
      type: 'error'
    });
  });
}
