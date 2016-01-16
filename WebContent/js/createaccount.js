function submitAccount() {
  var password = document.getElementById('password').value;
  var passwordConfirm = document.getElementById('passwordConfirm').value;
  if (password !== passwordConfirm) {
    swal({
      title: "Your passwords don't match :(",
      text: "Please try again",
      type: "warning",
      closeOnConfirm: true
    }, function() {
      clearIds(['password', 'passwordConfirm']);
    });
  } else {
    var data = buildObj(['firstName', 'lastName', 'netid','password']);
    sendRequest('POST', 'AccountRequests', data, 'text', false, null, function() {
      swal({
        title: "Account Request Sent",
        text: "You'll receive an email when you're account is received",
        type: "success",
        closeOnConfirm: true
      }, function() {
        window.location.href = '/Fratsite'
      });
    }, function(xhr, textStatus, error) {
      console.log(error);
      console.log(xhr.responseText)
      swal({
        title: "Account Request Failed",
        text: "Sorry, we couldn't process your request",
        type: "error",
        closeOnConfirm: true
      })
    });
  }
}