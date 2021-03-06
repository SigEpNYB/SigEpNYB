$(document).ready(function() {
  $('#phone').formance('format_phone_number');
  $('.loginInput').keyup(function(event) {
    if (event.keyCode === 13) submitAccount();
  });
});

/**
 * Sends an account to the server
 * @returns {undefined}
 */
function submitAccount() {
  var data = buildObj(['firstName', 'lastName', 'netid','password', 
    'phone', 'passwordConfirm'])
  var empty = Object.keys(data).hasMatch(function(key) {
    return data[key] === '';
  });
  if (data.netid.search(/[A-Z]!@#$%^&*/) !== -1) {
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
  } else if (data.phone.length < 16) {
    swal({
      title: 'Incomplete Phone Number',
      text: 'Please make sure to enter your area code too (+1 not necessary)',
      type: 'warning',
      closeOnConfirm: true
    });
  } else if (data.password !== data.passwordConfirm) {
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