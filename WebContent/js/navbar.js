$(document).ready(function() {
	$("#navbar-signout").on('click', logout);
	
    function navbarNameReplace() {
		httpRequest('GET', 'Account', true, null, null, function(resp) {
			$("#navbar-username a").text(resp.firstName + " " + resp.lastName + " (" + resp.netid + ")");
		});
	}
	navbarNameReplace();
});