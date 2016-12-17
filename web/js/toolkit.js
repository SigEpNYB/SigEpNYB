/**
 * Sends an AJAX request
 * @param {string} method - HTTP method
 * @param {string} url
 * @param {Object} data - Data to be sent
 * @param {string} dataType - Datatype upon receival, default = json
 * @param {boolean} useToken - Whether to use token cookie in request
 * @param queryStringObj {Object} - Data to be included in query string
 * @param onSuccess {function} - Takes in data, textStatus, xhr
 * @param onFail {function} - Takes in xhr, textStatus, errorThrown
 * @returns {undefined}
 */
function sendRequest(method, url, data, dataType, useToken, queryStringObj, onSuccess, onFail) {
  if (typeof onSuccess !== 'function') onSuccess = $.noop;
  if (typeof onFail !== 'function') onFail = $.noop;
  if (typeof dataType !== 'string') dataType = 'json';

  var queryString = '?';
  if ((queryStringObj !== null) && (typeof queryStringObj === 'object')) {
    Object.keys(queryStringObj).forEach(function(key) {
      queryString += key + '=' + queryStringObj[key] + '&';
    });
  }
  queryString = queryString.slice(0, -1);

  var dataString = ($.isEmptyObject(data) ? undefined : JSON.stringify(data));

  var headers = {};
  if (useToken) {
    headers['Auth'] = Cookies.get('token');
  }
  $.ajax({
    method: method,
    url: url + queryString,
    data: dataString,
    dataType: dataType,
    headers: headers,
    success: onSuccess,
    error: onFail
  });
  return;
}

/**
 * Checks that the user has the required permissions
 * @param {string[]} necessary - list of necessary permission strings
 * @param {string} title - Sweetalert Title
 * @param {string} text - Sweetalert Text
 * @param {boolean} redirect - whether to redirect to dashboard
 */
function checkPermissions(necessary, title, text, redirect) {
  sendRequest('GET', 'Account', null, 'json', true, {showPermissions: true}, function(permissions) {
    var hasPermission = necessary.reduce(function(b, p) {
      return b && (permissions.indexOf(p) >= 0);
    }, true);
    var redir;
    if (redirect) {
      redir = function(isConfirm) { window.location.href = 'dashboard.html'; };
    } else {
      redir = function(isConfirm) {};
    }
    if (!hasPermission) {
      swal({
        title: title,
        text: text,
        type: 'error'
      }, redir);
    }
  });
}

var netidMap = {};
var accountidMap = {};
var nameMap = {};
var nameList = [];
function setNetidMap() {
  sendRequest('GET', 'Accounts', null, 'json', true, null, function(accounts) {
    accounts.forEach(function(account) {
      netidMap[account.netid] = account;
      accountidMap[account.id] = account;
      var name = account.firstName + ' ' + account.lastName;
      nameMap[name] = account;
      nameList.push(name);
    });
  });
}

/**
 * Builds an array of ids and values to be sent
 * @param {string[]} ids - array of IDs
 * @returns {Object}
 */
function buildObj(ids) {
  var obj = {};
  ids.forEach(function(id) {
    obj[id] = document.getElementById(id).value;
  });
  return obj;
}

/**
 * Clears the values of a list of ids
 * @param {string[]} ids - array of IDs
 * @returns {undefined}
 */
function clearIds(ids) {
  ids.forEach(function(id) {
    document.getElementById(id).value = '';
  });
}

/**
 * Sets the values of a list of ids
 * @param {string[]} ids - array of IDs
 * @param {Object} data - object containing value for each id
 * @returns {undefined}
 */
function setIds(ids, data) {
  ids.forEach(function(id) {
    document.getElementById(id).value = data[id];
  });
}

/**
 * Sets the values of a list of times
 * @param {string[]} ids - array of IDs
 * @param {Object} data - object containing value for each id
 * @returns {undefined}
 */
function setDates(ids, data) {
  ids.forEach(function(id) {
    var dateObj = new Date(data[id]);
    var stringObj = dateObj.toISOString().slice(0, -1);
    document.getElementById(id).value = stringObj;
  });
}

/**
 * Adjusts the given date by the given amounts
 * @param {Date} date
 * @param {number} year
 * @param {number} month
 * @param {number} day
 * @returns {Date}
 */
function adjustDate(date, year, month, day) {
  year = typeof year !== 'undefined' ? year : 0;
  month = typeof month !== 'undefined' ? month : 0;
  day = typeof day !== 'undefined' ? day : 0;
  var adjustedDate = new Date(date);
  adjustedDate.setYear(date.getFullYear() + year);
  adjustedDate.setMonth(date.getMonth() + month);
  adjustedDate.setDate(date.getDate() + day);
  return adjustedDate;
}

/**
 * Changes a date to UTC or ISO format
 * @param {Date} date
 * @param {string} outputType - UTC or ISO
 * @reutnrs {string}
 */
function changeDateFormat(date, outputType) {
  if (outputType == 'UTC') {
    return date.getTime().toString();
  } else if (outputType == 'ISO') {
    return date.toISOString().slice(0,-8);
  } else {
    return date.getTime().toString();
  }
}

/**
 * Adjusts a date to the timezone it is being viewed from
 * @param {Date} date
 * @returns {Date}
 */
function dateToTZ(date) {
  var adjustedDate = date;
  adjustedDate.setHours(adjustedDate.getHours() - 
    (new Date().getTimezoneOffset()/60));
  return adjustedDate;
}

/**
 * Converts a date object to an common US Time string
 * @param {Date} date
 * @returns {string}
 */
function dateToPaddedString(date) {
  var year = date.getFullYear();
  var month = date.getMonth();
  var day = date.getDate();
  var hour = date.getHours();
  var minute = date.getMinutes();
    return (date.getFullYear() + '-' +
           ('0' + (1 + date.getMonth())).slice(-2) + '-' +
           ('0' + date.getDate()).slice(-2) + 'T' +
           ('0' + date.getHours()).slice(-2) + ':' +
           ('0' + date.getMinutes()).slice(-2));
}

function onSendFail(xhr, onCompletion) {
  if (typeof xhr !== 'object') xhr = {status: 600}
  if (typeof onCompletion !== 'function') onCompletion = $.noop;
  switch (xhr.status) {
    case 400:
      swal({
        title: 'Front-End Error',
        text: 'Please tell one of the developers and try again later',
        type: 'error'
      });
      break;
    case 401:
      swal({
        title: 'Invalid Token',
        text: 'Please try logging in again',
        type: 'error'
      });
      break;
    case 403:
      swal({
        title: 'Login Failed',
        text: 'Please make sure your NetID and Password are correct',
        type: 'error'
      });
      break;
    case 404:
      swal({
        title: 'Invalid Endpoint',
        text: 'Please tell one of the developers and try again later',
        type: 'error'
      });
      break;
    case 405:
      swal({
        title: 'Invalid HTTP Method',
        text: 'Please tell one of the developers and try again later',
        type: 'error'
      });
      break;
    case 500:
      swal({
        title: 'Server Error',
        text: 'Please tell one of the developers and try again later',
        type: 'error'
      });
      break;
    case 600:
      swal({
        title: 'One of the Requests failed',
        text: "We'll refresh so you can see which ones failed",
        type: 'error'
      });
    default:
      swal({
        title: 'Unhandled Exception',
        text: 'Error Code: ' + xhr.status,
        type: 'error'
      })
  }
  onCompletion();
}

function addStr(s1, s2) { return s1 + s2; }

function substringMatcher(strs) {
  return function findMatches(q, cb) {
    var matches, substringRegex;

    // an array that will be populated with substring matches
    matches = [];

    // regex used to determine if a string contains the substring `q`
    substrRegex = new RegExp(q, 'i');

    // iterate through the pool of strings and for any string that
    // contains the substring `q`, add it to the `matches` array
    $.each(strs, function(i, str) {
      if (substrRegex.test(str)) {
        matches.push(str);
      }
    });

    cb(matches);
  };
}

function reloadPage() {
  window.location.reload();
}

/**
 * Converts a string to proper english case
 * @returns {string}
 */
String.prototype.toProperCase = function () {
  return this.replace(/\w\S*/g, function(txt){
    return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
  });
};

/**
 * Trims the last character off a string if it's not empty
 * @returns {string}
 */
String.prototype.trimLastChar = function() {
  return (this.length === 0) ? this : this.slice(0, -1);
};

/**
 * Checks if at least 1 element in the list returns true on the predicate
 * function
 * @param {function} predicate - Must return True or False for each element
 * of the array
 * @returns {boolean}
 */
Array.prototype.hasMatch = function(predicate) {
  return (this.filter(predicate)).length > 0;
}

Array.prototype.contains = function(val) {
  return this.indexOf(val) > -1;
}