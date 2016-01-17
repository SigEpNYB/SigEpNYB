$(document).ready(function() {
  $('#phoneNumber').formance('format_phone_number');
});

function submitAccount() {
  var password = document.getElementById('password').value;
  var passwordConfirm = document.getElementById('passwordConfirm').value;
  var data = buildObj(['firstName', 'lastName', 'netid','password', 'phoneNumber']);
  var empty = Object.keys(data).hasMatch(function(key) {
    return data[key] === '';
  });
  if (password.length < 6) {
    swal({
      title: 'Your password is too short'
      text: 'It needs to be at least 6 characters',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (password.search(/\d/) === -1) {
    swal({
      title: 'Your password must contain at least 1 number',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (password.search(/[a-z]/) === -1) {
    swal({
      title: 'Your password must contain at least 1 lowercase letter',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (password.search(/[A-Z]/) === -1) {
    swal({
      title: 'Your password must contain at least 1 uppercase letter',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (empty) {
    swal({
      title: 'Not all of the fields are filled out',
      text: 'All fields are required',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (password !== passwordConfirm) {
    swal({
      title: "Your passwords don't match :(",
      text: "Please try again",
      type: "warning",
      closeOnConfirm: true
    }, function() {
      clearIds(['password', 'passwordConfirm']);
    });
  } else {
    sendRequest('POST', 'AccountRequests', data, 'text', false, null, function() {
      swal({
        title: "Account Request Sent",
        text: "You'll receive an email when you're account is received",
        type: "success",
        closeOnConfirm: true
      }, function() {
        window.location.href = '/Fratsite'
      });
    }, function() {
      swal({
        title: "Account Request Failed",
        text: "Sorry, we couldn't process your request :(",
        type: "error",
        closeOnConfirm: true
      });
    });
  }
}