/**
 * Sends an AJAX request
 * @param {string} method - HTTP method
 * @param {string} url
 * @param {Object} data - Data to be sent
 * @param {boolean} useToken - Whether to use token cookie in request
 * @param queryStringObj {Object} - Data to be included in query string
 * @param onSuccess {function} - Takes in data, textStatus, xhr
 * @param onFail {function} - Takes in xhr, textStatus, errorThrown
 * @returns {undefined}
 */
function sendRequest(method, url, data, useToken, queryStringObj, onSuccess, onFail) {
  if (typeof onSuccess !== 'function') onSuccess = $.noop;
  if (typeof onFail !== 'function') onFail = $.noop; //function() { logout(); };

  var queryString = '?';
  if ((queryStringObj !== null) && (typeof queryStringObj === 'object')) {
    Object.keys(queryStringObj).forEach(function(key) {
      queryString += key + '=' + queryStringObj[key] + '&';
    });
  }

  var dataString = JSON.stringify(data || {});

  queryString = queryString.slice(0, -1);

  var headers = {};
  if (useToken) {
    console.log()
    headers['Auth'] = Cookies.get('token');
  }
  $.ajax({
    method: method,
    url: url + queryString,
    data: dataString,
    headers: headers,
    success: onSuccess,
    error: onFail
  })
  return;
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
  var adjustedDate = date;
  adjustedDate.setYear(date.getFullYear() + year);
  adjustedDate.setMonth(date.getMonth() + month);
  adjustedDate.setDate(date.getDate() + day);
  return adjustedDate;
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
  return;
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