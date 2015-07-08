function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

function httpRequest(mthd, url, useToken, msg, urlParams, success, error, serverError) {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			var resp;
			try {
				resp = JSON.parse(xhr.responseText);
			} catch (e) {
				resp = null;
			}
			
			var status = xhr.status;
			if (status == 200) {
				if (success != null) success(resp);
			} else if (status == 401) {
				window.location.href = '/Fratsite/index.html';
			} else if (status == 400) {
				if (error != null) error();
				else if (success != null) success();
			} else if (status == 403) {
				if (error != null) error();
				else if (success != null) success();
			} else if (status == 500) {
				if (serverError == null) {
					if (error != null) error();
					else if (success != null) success();
				} else {
					serverError();
				}
			} else {
				alert("[DEBUG] unhandled response code: " + status);
			}
		}
	}
	
	var urlExt = '';
	if (urlParams != null) {
		urlExt = '?';
		for (var paramName in urlParams) {
			urlExt += paramName + '=' + urlParams[paramName] + '&';
		}
		urlExt = urlExt.substring(0, urlExt.length - 1);
	}
	
	xhr.open(mthd, url + urlExt, true);
	if (useToken) {
		var token = getCookie('token');
		xhr.setRequestHeader('Auth', token);
	}
	if (msg == null) {
		xhr.send(null);
	} else {
		xhr.send(JSON.stringify(msg));
	}
}

function buildObj() {
	var obj = {};
	for (var i = 0; i < arguments.length; i++) {
		var id = arguments[i];
		var element = document.getElementById(id);
		obj[id] = element.value;
		element.value = '';
	}
	
	return obj;
}

function login() {
	var msg = buildObj('netid', 'password');
	httpRequest('POST', 'Login', false, msg, null, function(resp) {
    	document.cookie = 'token=' + resp.token;
    	window.location.href = '/Fratsite/dashboard.html';
	}, function() {
		swal("Login Failed", "Please check your NetID and Password", "error")
	});
}

function logout() {
	console.log("FAILED")
	httpRequest('DELETE', 'Login', true, null, null, function() {
		window.location.href = '/Fratsite/index.html';
		document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
	});
}

function getAccount() {
	httpRequest('GET', 'Account', true, null, null, function(resp) {
		document.getElementById('name').innerHTML = resp.firstName + ' ' + resp.lastName;
		$("#navbar-username a").text(resp.firstName + " " + resp.lastName + " (" + resp.netid + ")");
	});
}

function addAccount() {
	var msg = buildObj('netid', 'password', 'firstName', 'lastName');
	httpRequest('POST', 'AccountRequests', true, msg, null, function(resp) {
		if ((resp != null ) && (resp.hasOwnProperty('typeText'))) {
			if (resp['typeText'] == 'ACCOUNT_ALREADY_EXISTS') {
				swal({title: "Account Already Exists", text: "An account with this NetID already exists, please try logging in", type: "error"}, function() {
					window.location.href = "/Fratsite";
				});
			} else if (resp['typeText'] == 'REQUEST_ALREADY_EXISTS') {
				swal({title: "Request Pending", text: "Somebody already requested an account under this NetID", type: "error"});
				document.getElementById('passwordConfirm').value = ''
			} else {
				swal("Code Not Handled, please try again")
			}
		} else {
			window.location.href = "/Fratsite/successresponse.html";
		}
	}, function() { 
		swal({title: "Server Error", text: "Please try again", type: "error"});
	});
}

function getAccounts() {
	httpRequest('GET', 'Accounts', true, null, null, function(resp) {
		var table = '<tr><td><b>First Name</b></td><td><b>Last Name</b></td><td><b>NetID</b></td></tr>';
		for (var i = 0; i < resp.length; i++) {
			var member = resp[i];
			table += '<tr><td>' + member.firstName + '</td><td>' + member.lastName + '</td><td>' + member.netid + '</td></tr>';
		}
		document.getElementById('members').innerHTML = table;
	});
}

function removeAccount() {
	var msg = buildObj('netid');
	httpRequest('DELETE', 'Accounts', true, msg, null, function() {
		document.getElementById("status").innerHTML = "Deleted " + msg.netid + " sucessfully";
	}, function() {
		document.getElementById("status").innerHTML = "Error - delete was unsecessful";
	});
}

function addEvent() {
	var msg = buildObj('title', 'startTime', 'endTime', 'description');
	httpRequest('POST', 'Events', true, msg, null, function() {
		document.getElementById("status").innerHTML = "Successfully created event";
	}, function() {
		document.getElementById("status").innerHTML = "Event creation failed";
	});
}

function dateToString(date) {
	var year = date.getFullYear();
	var month = date.getMonth();
	var day = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();
	return year + "-" + month + "-" + day + "T" + hour + ":" + minute;
}

function getEvents() {
	var now = new Date();
	var startStr = dateToString(now);
	now.setDate(now.getDate() + 7);
	var endStr = dateToString(now);
	
	var msg = {startTime:startStr, endTime:endStr};
	httpRequest('GET', 'Events', true, null, msg, function() {
		
	});
}

function getRoles() {
	httpRequest('GET', 'Roles', true, null, null, function(resp) {
		var rolesHTML = '';
		for (var i = 0; i < resp.length; i++) {
			var role = resp[i];
			rolesHTML += '<h3>' + role.role + '</h3>'
			for (var j = 0; j < role.links.length; j++) {
				var link = role.links[j];
				rolesHTML += '<a href=' + link.href + '>' + link.pageName + '</a><br>'
			}
		}
		document.getElementById('roles').innerHTML = rolesHTML;
	});
}