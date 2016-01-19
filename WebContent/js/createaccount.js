$(document).ready(function() {
  $('#phoneNumber').formance('format_phone_number');
  $('.loginInput').keyup(function(event) {
    if (event.keyCode === 13) submitAccount();
  });
});

function submitAccount() {
  var data = buildObj(['firstName', 'lastName', 'netid','password', 
    'phoneNumber', 'passwordConfirm']);
  return sendAccount(data);
}

/**
 * Sends an account to the server
 * @param {Object} data - must contain firstName, lastName, netid, password
 * and phoneNumber
 * @returns {undefined}
 */
function sendAccount(data) {
  var empty = Object.keys(data).hasMatch(function(key) {
    return data[key] === '';
  });
  if (data.password.length < 6) {
    swal({
      title: 'Your password is too short',
      text: 'It needs to be at least 6 characters',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (data.password.search(/\d/) === -1) {
    swal({
      title: 'Password missing number',
      text: 'Your password must contain at least 1 number',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (data.password.search(/[a-z]/) === -1) {
    swal({
      title: 'Password missing lowercase letter',
      text: 'Your password must contain at least 1 lowercase letter',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (data.password.search(/[A-Z]/) === -1) {
    swal({
      title: 'Password missing uppercase letter',
      text: 'Your password must contain at least 1 uppercase letter',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (data.netid.search(/[A-Z]!@#$%^&*/) !== -1) {
    swal({
      title: 'Invalid NetID',
      text: 'Your NetID can only contain lowercase letters and numbers',
      type: 'warning',
      closeOnConfirm: true
    })
  } else if (empty) {
    swal({
      title: 'Not all of the fields are filled out',
      text: 'All fields are required',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (data.phoneNumber.length < 16) {
    swal({
      title: 'Incomplete Phone Number',
      text: 'Please make sure to enter your area code too (+1 not necessary)',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (data.password !== data.passwordConfirm) {
      console.log(data);
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
        type: "success"
      }, function() {
        window.location.href = 'index.html'
      });
    }, function() {
      swal({
        title: "Account Request Failed",
        text: "Please make sure you haven't already sent an account request",
        type: "error"
      });
    });
  }
}