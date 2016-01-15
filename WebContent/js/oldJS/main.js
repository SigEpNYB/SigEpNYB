function httpRequest(mthd, url, useToken, msg, urlParams, success, error, serverError, passThrough) {
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
        if (success !== null) success(resp, passThrough);
      } else if (status == 401) {
        Cookies.set('lastPage', window.location.href, {expires: 600});
        window.location.href = '/Fratsite/index.html';
      } else if (status == 400) {
        if (error !== null) error();
        else if (success !== null) success();
      } else if (status == 403) {
        if (error !== null) error();
        else if (success !== null) success();
      } else if (status == 500) {
        if (serverError === null) {
          if (error !== null) error();
          else if (success !== null) success();
        } else {
          serverError();
        }
      } else {
        alert("[DEBUG] unhandled response code: " + status);
      }
    }
  };
  
  var urlExt = '';
  if (urlParams !== null) {
    urlExt = '?';
    for (var paramName in urlParams) {
      urlExt += paramName + '=' + urlParams[paramName] + '&';
    }
    urlExt = urlExt.substring(0, urlExt.length - 1);
  }
  
  xhr.open(mthd, url + urlExt, true);
  if (useToken) {
    var token = Cookies.get('token');
    xhr.setRequestHeader('Auth', token);
  }
  if (msg === null) {
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
  }
  
  return obj;
}

function login() {
  var msg = buildObj('netid', 'password');
  httpRequest('POST', 'Login', false, msg, null, function(resp) {
      Cookies.set('token', resp.token);
      if (Cookies.get("lastPage") !== undefined) {
        var lastPage = Cookies.get("lastPage");
        Cookies.expire("lastPage");
        window.location.href = lastPage;
      } else {
        window.location.href = '/Fratsite/dashboard.html';
      }
  }, function() {
    document.getElementById('netid').value = '';
    document.getElementById('password').value = '';
    swal("Login Failed", "Please check your NetID and Password", "error");
  });
}

function logout() {
  console.log("FAILED");
  httpRequest('DELETE', 'Login', true, null, null, function() {
    window.location.href = '/Fratsite/index.html';
    Cookies.expire("token");
  });
}

function getAccount() {
  httpRequest('GET', 'Account', true, null, null, function(resp) {
    document.getElementById('name').innerHTML = resp.firstName + ' ' + resp.lastName;
  });
}

function getAccountInfo(callback) {
  httpRequest('GET', 'Account', true, null, null, callback);
}

function addAccount() {
  var msg = buildObj('netid', 'password', 'firstName', 'lastName');
  httpRequest('POST', 'AccountRequests', true, msg, null, function(resp) {
    if ((resp !== null ) && (resp.hasOwnProperty('typeText'))) {
      if (resp['typeText'] == 'ACCOUNT_ALREADY_EXISTS') {
        swal({title: "Account Already Exists", text: "An account with this NetID already exists, please try logging in", type: "error"}, function() {
          window.location.href = "/Fratsite";
        });
      } else if (resp['typeText'] == 'REQUEST_ALREADY_EXISTS') {
        swal({title: "Request Pending", text: "Somebody already requested an account under this NetID", type: "error"});
        document.getElementById('netid').value = '';
        document.getElementById('firstName').value = '';
        document.getElementById('lastName').value = '';
        document.getElementById('password').value = '';
        document.getElementById('passwordConfirm').value = '';
      } else {
        swal("Code Not Handled, please try again");
      }
    } else {
      window.location.href = "/Fratsite/successresponse.html";
    }
  }, function() {
    swal({title: "Server Error", text: "Please try again", type: "error"});
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

function dateToPaddedString(date) {
  var year = date.getFullYear();
  var month = date.getMonth();
  var day = date.getDate();
  var hour = date.getHours();
  var minute = date.getMinutes();
    return (date.getFullYear() + '-' +
           ('0' + (1+date.getMonth())).slice(-2) + '-' +
           ('0' + date.getDate()).slice(-2) + 'T' +
           ('0' + date.getHours()).slice(-2) + ':' +
           ('0' + date.getMinutes()).slice(-2));
}

function getRequests() {
  httpRequest('GET', 'AccountRequest', true, null, null, function(resp) {
    console.log(resp);
  });
}

function monthToNumber(name) {
  if (name == "Jan") {
    return "01";
  } else if (name == "Feb") {
    return "02";
  } else if (name == "Mar") {
    return "03";
  } else if (name == "Apr") {
    return "04";
  } else if (name == "May") {
    return "05";
  } else if (name == "Jun") {
    return "06";
  } else if (name == "Jul") {
    return "07";
  } else if (name == "Aug") {
    return "08";
  } else if (name == "Sep") {
    return "09";
  } else if (name == "Oct") {
    return "10";
  } else if (name == "Nov") {
    return "11";
  } else {
    return "12";
  }
}

function getAdjustedDate(yearAdjustment, monthAdjustment, outputType) {
  return changeDateFormat(adjustDate(new Date(), yearAdjustment, monthAdjustment, 0), 'UTC');
}

function adjustDate(date, year, month, day) {
  var adjustedDate = date;
  adjustedDate.setYear(adjustedDate.getFullYear() + year);
  adjustedDate.setMonth(adjustedDate.getMonth() + month);
  adjustedDate.setDate(adjustedDate.getDate() + day);
  return adjustedDate;
}

function changeDateFormat(date, outputType) {
  if (outputType == 'UTC') {
    return date.getTime();
  } else if (outputType == 'ISOTZ') {
    return date.toISOString();
  } else if (outputType == 'ISO') {
    return date.toISOString().slice(0,-8);
  } else {
    return date;
  }
}

function dateToTZ(date) {
  var adjustedDate = date;
  adjustedDate.setHours(adjustedDate.getHours() - (new Date().getTimezoneOffset()/60));
  return changeDateFormat(adjustedDate, 'ISO');
}

String.prototype.toProperCase = function () {
    return this.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
};

function trimLastChar(string) {
  if (string.length === 0) {
    return string;
  } else {
    return string.substring(0, string.length - 1);
  }
}