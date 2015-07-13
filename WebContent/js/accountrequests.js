$(document).ready(function() {
	function displayRequests() {
		httpRequest('GET', 'AccountRequests', true, null, null, function(resp){
			console.log(resp)
			for (i=0; i<resp.length; i++) {
				$('#requests-body').prepend('<tr id="' + resp[i].id + '"><td class="text-center">' + resp[i]["firstName"] + ' ' + resp[i]["lastName"] + '</td>' +
											'<td class="text-center">' + resp[i]["netid"] + '</td>' + 
											'<td class="buttons"><button class="btn btn-success requestbutton approveRequest">Approve</button> ' + 
											'<button class="btn btn-danger requestbutton rejectRequest">Reject</button></td></tr>')
			}
		})
	}
	displayRequests();

	function approveRequest(id) {
		msg = {idRequest: id}
		httpRequest('POST', 'AccountRequests', true, msg, null, function(resp) {
			$('#request' + id).hide('slow')
		})
	}
	function rejectRequest(id) {
		msg = {idRequest: id}
		httpRequest('DELETE', 'AccountRequests', true, msg, null, function(resp) {
			$('#request' + id).hide('slow')
		})
	}
	function noResults() {
		if ($('#requests-body > tr').length === 0) {
			$("#requests-body").append('<tr><td colspan="3" id="no-requests"><strong>No requests pending, Yay!</strong></td></tr>');
		}
	}

	$(document).on('click', '.approveRequest', function() {
		approveRequest($(this).closest('tr').attr('id'));
		noResults();
	})
	$(document).on('click', '.rejectRequest', function() {
		rejectRequest($(this).closest('tr').attr('id'))
		noResults();
	})
})