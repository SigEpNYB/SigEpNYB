$(document).ready(function() {
	$("#navbar-signout").click(function() {
    	logout();
    });
    function navbarNameReplace() {
		httpRequest('GET', 'Account', true, null, null, function(resp) {
			$("#navbar-username a").text(resp.firstName + " " + resp.lastName + " (" + resp.netid + ")");
		});
	}
	navbarNameReplace();
});