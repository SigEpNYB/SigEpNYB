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

function httpRequest(mthd, url, useToken, msg, success, error, serverError) {
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
			} else if (status == 302) {
				alert(xhr.responseText);
			} else if (status == 400) {
				if (error != null) error();
				else if (success != null) success();
			} else if (status == 401) {
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
		element.value = '';
	}
	
	return obj;
}

function login() {
	var msg = buildObj('netid', 'password');
	httpRequest('POST', 'Login', false, msg, function(resp) {
    	document.cookie = 'token=' + resp.token;
    	window.location.href = '/Fratsite/dashboard.html';
	}, function() {
		document.getElementById('errormsg').innerHTML = "Error loggin in";
	});
}

function logout() {
	httpRequest('DELETE', 'Login', true, null, function() {
		window.location.href = '/Fratsite/index.html';
		document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
	});
}

function getAccount() {
	httpRequest('GET', 'Account', true, null, function(resp) {
		document.getElementById('name').innerHTML = resp.firstName + ' ' + resp.lastName;
	});
}

function addAccount() {
	var msg = buildObj('netid', 'firstName', 'lastName');
	httpRequest('POST', 'Accounts', true, msg, function() {
		document.getElementById("status").innerHTML = "Successfully created an account for " + msg.firstName;
	}, function() { 
		document.getElementById("status").innerHTML = "Account creation failed";
	});
}

function getAccounts() {
	httpRequest('GET', 'Accounts', true, null, function(resp) {
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
	httpRequest('DELETE', 'Accounts', true, msg, function() {
		document.getElementById("status").innerHTML = "Deleted " + msg.netid + " sucessfully";
	}, function() {
		document.getElementById("status").innerHTML = "Error - delete was unsecessful";
	});
}

function addEvent() {
	var msg = buildObj('title', 'startTime', 'endTime', 'description');
	httpRequest('POST', 'Events', true, msg, function() {
		document.getElementById("status").innerHTML = "Successfully created event";
	}, function() {
		document.getElementById("status").innerHTML = "Event creation failed";
	});
}

function getRoles() {
	httpRequest('GET', 'Roles', true, null, function(resp) {
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