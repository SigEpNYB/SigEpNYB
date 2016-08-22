$(document).ready(function() {
  $('.form-control').keyup(function(event) {
    if (event.keyCode === 13) changePassword();
  });
});

function changePassword() {
  var data = buildObj(['oldPassword', 'newPassword', 'passwordConfirm']);
  return sendPassword(data);
}

/**
 * Sends a password change request to the server
 * @param {Object} data - Needs to contains oldPassword, newPassword
 * and passwordConfirm
 * @returns {undefined}
 */
function sendPassword(data) {
  if (data.newPassword !== data.passwordConfirm) {
    swal({
      title: "Your passwords don't match :(",
      text: "Please try again",
      type: "warning",
      closeOnConfirm: true
    }, function() {
      clearIds(['newPassword', 'passwordConfirm']);
    });
  } else {
    delete data.passwordConfirm;
    sendRequest('PUT', 'Account', data, 'text', true, null, function() {
      swal({
        title: 'Password Changed',
        text: 'Make sure to remember your password!',
        type: 'success',
        closeOnConfirm: true
      }, function(isConfirm) {
        window.location.href = 'profile.html'
      });
    }, function() {
      swal({
        title: 'Password Change Failed',
        text: "Doesn't look like your old password was correct :(",
        type: 'error',
        closeOnConfirm: true
      });
    });
  }
}