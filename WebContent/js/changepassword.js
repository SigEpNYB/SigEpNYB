function changePassword() {
  var newPassword = document.getElementById('newPassword').value;
  var passwordConfirm = document.getElementById('passwordConfirm').value;
  if (newPassword !== passwordConfirm) {
    swal({
      title: "Your passwords don't match :(",
      text: "Please try again",
      type: "warning",
      closeOnConfirm: true
    }, function() {
      clearIds(['newPassword', 'passwordConfirm']);
    });
  } else {
    var data = buildObj(['oldPassword', 'newPassword']);
    sendRequest('PUT', 'Account', data, 'text', true, null, function() {
      swal({
        title: 'Password Changed',
        text: 'Make sure to remember your password!',
        type: 'success',
        closeOnConfirm: true
      }, function() {
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

$(document).ready(function() {
  $('.form-control').keyup(function(event) {
    if (event.keyCode === 13) changePassword();
  });
});