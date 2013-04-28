var rt = window.external.mxGetRuntime();
var browser = rt.create('mx.browser');
var storage = rt.storage;
var locale = rt.create('mx.app.locale');
var EXTURL = 'http://ruyi.etao.com/extension/maxthon.js';
var STORAGE_KEY_VERSION = 'version';
var STORAGE_KEY_UID = 'uid';
var STORAGE_KEY_INSERT_DATE = 'insert_date';

function generateDateStamp() {
  var now = new Date();
  var date = now.getDate();
  var month = now.getMonth() + 1;
  if (date < 10)
    date = '0' + date;
  if (month < 10)
    month = '0' + month;
  return '' + now.getFullYear() + month + date;
}

function loadScript(url, uid, onload) {
  var script = document.createElement('script');
  script.src = url + '?t=' + generateDateStamp() + '&u=' + encodeURIComponent(uid);
  script.charset = 'utf-8';
  var head = document.getElementsByTagName('head')[0];
  if (head) {
    head.appendChild(script);
  }

  script.onload = function() {
    onload && onload();
//    head.removeChild(script);
  }
}

function getRandomString() {
  return Math.floor(Math.random() * 2147483648).toString(36) +
    (Math.floor(Math.random() * 2147483648) ^
    (new Date).getTime()).toString(36);
}

var version = storage.getConfig(STORAGE_KEY_VERSION);
var INSTALL_URL = 'http://ruyi.etao.com/tutorial?utm_medium=ext&utm_source=install';
var UPDATE_URL = 'http://ruyi.etao.com/tutorial?utm_medium=ext&utm_source=update';
var currentVersion = "3.4.2.8";
var isNeedToUpdate = function(currentVersion, version) {
  currentVersion = currentVersion.split('.');
  version = version.split('.');
  if (currentVersion[0] > version[0] ||
      (currentVersion[0] == version[0] && currentVersion[1] > version[1])) {
    return true;
  }
  return false;
};

// First installed.
if (!version) {
  storage.setConfig(STORAGE_KEY_VERSION, currentVersion);
  storage.setConfig(STORAGE_KEY_UID, getRandomString());
} else if (isNeedToUpdate(currentVersion, version)) {
  storage.setConfig(STORAGE_KEY_VERSION, currentVersion);
}

// Set uid.
var uid = storage.getConfig(STORAGE_KEY_UID);
document.body.setAttribute('data-ruyitao-uid', uid);

// Used to count active users.
var generateTimeStamp = function() { return +new Date; }
var analysisImgURL = 'http://ruyi.etao.com/s.gif?a=up&p=rm001&u=' + uid + '&v=3.1.5&t=' + generateTimeStamp();
var today = generateDateStamp();
var insertedToday = storage.getConfig(STORAGE_KEY_INSERT_DATE) == today;
if (!insertedToday) {
  storage.setConfig(STORAGE_KEY_INSERT_DATE, today);
  var img = document.createElement('img');
  img.id = "ruyitao-user-active-count";
  img.setAttribute('src', analysisImgURL);
  img.setAttribute('style', 'display:none !important;');
  document.body.appendChild(img);
}

var rmsie = /msie\s+([\w.]+)/;
var matches = rmsie.exec(window.navigator.userAgent.toLowerCase());
if ( matches && (document.compatMode == 'BackCompat' || matches[1].substr(0,1) <= 6) ) {
  // IE 6 或者 IE 兼容模式不插入 content script
} else if (window.location.protocol == 'http:') {
  // 只在 http: 协议下插入 content script
  loadScript(EXTURL, uid);
}
