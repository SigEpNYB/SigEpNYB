$(document).ready(function() {
	$('#createAccountButton').click(function() {
		if ($('#password').val() != $('#passwordConfirm').val()) {
			swal(title: "Oops", text: "Your passwords didn't match", type:"error"});
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