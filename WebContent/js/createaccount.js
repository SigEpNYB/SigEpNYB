$(document).ready(function() {
	$('#createAccountButton').on('click', function() {
		if ($('#password').val() != $('#passwordConfirm').val()) {
			swal({title: "Oops", text: "Your passwords didn't match", type: "error"});
			document.getElementById('password').value = '';
			document.getElementById('passwordConfirm').value = '';
		} else if (($('#firstName').val() == '') || ($('#lastName').val() == '') || ($('#netid').val() == '') || ($('#password').val() == '')) {
			swal({title: "Oops", text: "One of the fields was empty", type: "error"})
		} else {
			addAccount();
		}
	})
	$(".loginInput").keyup(function(event){
	    if (event.keyCode == 13){
	        $("#createAccountButton").click();
	    }
	});
})