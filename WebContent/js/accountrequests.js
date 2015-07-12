$(document).ready(function() {
	function displayRequests() {
		httpRequest('GET', 'AccountRequests', true, null, null, function(resp){
			for (i=0; i<resp.length; i++) {
				console.log(resp)
			}
		})
	}
	displayRequests();
})