$(document).ready(function() {
	$('#loginButton').click(function() {
		login();
	})
	$(".loginInput").keyup(function(event){
	    if (event.keyCode == 13){
	        $("#loginButton").click();
	    }
	});
})