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

function httpRequest(mthd, url, useToken, msg, callback) {
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
			callback(status, resp);
		}
	}
	
	xhr.open(mthd, url, true);
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
		element.id = '';
	}
	
	return obj;
}

function login() {
	var msg = buildObj('netid', 'password');
	httpRequest('POST', 'Login', false, msg, function(status, resp) {
		if (status == 200) {
    		document.cookie = 'token=' + resp.token;
    		window.location.href = '/Fratsite/dashboard.html';
    	} else {
    		document.getElementById('errormsg').innerHTML = "Error loggin in";
    	}
	});
}

function logout() {
	httpRequest('DELETE', 'Login', true, null, function() {
		window.location.href = '/Fratsite/index.html';
		document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
	});
}

function getAccount() {
	httpRequest('GET', 'Account', true, null, function(status, resp) {
		if (resp != null) {
			document.getElementById('name').innerHTML = resp.firstName + ' ' + resp.lastName;
		}
	});
}

function addAccount() {
	var msg = buildObj('netid', 'firstName', 'lastName');
	httpRequest('POST', 'Accounts', true, msg, function(status) {
		var message;
		if (status == 200) {
			message = "Successfully created an account for " + msg.firstName;
		} else {
			message = "Account creation failed";
		}
		document.getElementById("status").innerHTML = message;
	});
}

function getAccounts() {
	httpRequest('GET', 'Accounts', true, null, function(status, resp) {
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
	httpRequest('DELETE', 'Accounts', true, msg, function(status) {
		var message;
		if (status == 200) {
			message = "Deleted " + msg.netid + " sucessfully";
		} else {
			message = "Error - delete was unsecessful";
		}
		document.getElementById('status').innerHTML = message;
	});
}

function addEvent() {
	var msg = buildObj('title', 'startTime', 'endTime', 'description');
	httpRequest('POST', 'Events', true, msg, function(status) {
		var message;
		if (status == 200) {
			message = "Successfully created event";
		} else {
			message = "Event creation failed";
		}
		document.getElementById("status").innerHTML = message;
	});
}

function getRoles() {
	httpRequest('GET', 'Roles', true, null, function(status, resp) {
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