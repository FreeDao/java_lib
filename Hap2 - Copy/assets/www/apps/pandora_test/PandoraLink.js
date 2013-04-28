
   // ----------------------------------------------------------------------------
   // PandoraLink JS library.
   // Version: 168650
   // To verify a frame, visit https://developer.pandora.com/pandoralink/logparser.vm
   // For more information about PandoraLink, visit https://developer.pandora.com/pandoralink/
   // ----------------------------------------------------------------------------

           /*jslint adsafe: false, bitwise: true, browser: true, cap: false, css: false,
  debug: false, devel: true, eqeqeq: true, es5: false, evil: false,
  forin: false, fragment: false, immed: true, laxbreak: false, newcap: true,
  nomen: false, on: false, onevar: true, passfail: false, plusplus: true,
  regexp: false, rhino: true, safe: false, strict: false, sub: false,
  undef: true, white: false, widget: false, windows: false */
/*global jQuery: false, window: false */
"use strict";

/*
 * Original code (c) 2010 Nick Galbreath
 * http://code.google.com/p/stringencoders/source/browse/#svn/trunk/javascript
 *
 * jQuery port (c) 2010 Carlo Zottmann
 * http://github.com/carlo/jquery-base64
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
*/

/* base64 encode/decode compatible with window.btoa/atob
 *
 * window.atob/btoa is a Firefox extension to convert binary data (the "b")
 * to base64 (ascii, the "a").
 *
 * It is also found in Safari and Chrome.  It is not available in IE.
 *
 * if (!window.btoa) window.btoa = $.base64.encode
 * if (!window.atob) window.atob = $.base64.decode
 *
 * The original spec's for atob/btoa are a bit lacking
 * https://developer.mozilla.org/en/DOM/window.atob
 * https://developer.mozilla.org/en/DOM/window.btoa
 *
 * window.btoa and $.base64.encode takes a string where charCodeAt is [0,255]
 * If any character is not [0,255], then an exception is thrown.
 *
 * window.atob and $.base64.decode take a base64-encoded string
 * If the input length is not a multiple of 4, or contains invalid characters
 *   then an exception is thrown.
 */

jQuery.base64 = ( function( $ ) {

  var _PADCHAR = "=",
    _ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/",
    _VERSION = "1.0";


  function _getbyte64( s, i ) {
    // This is oddly fast, except on Chrome/V8.
    // Minimal or no improvement in performance by using a
    // object with properties mapping chars to value (eg. 'A': 0)

    var idx = _ALPHA.indexOf( s.charAt( i ) );

    if ( idx === -1 ) {
      throw "Cannot decode base64";
    }

    return idx;
  }


  function _decode( s ) {
    var pads = 0,
      i,
      b10,
      imax = s.length,
      x = [];

    s = String( s );

    if ( imax === 0 ) {
      return s;
    }

    if ( imax % 4 !== 0 ) {
      throw "Cannot decode base64";
    }

    if ( s.charAt( imax - 1 ) === _PADCHAR ) {
      pads = 1;

      if ( s.charAt( imax - 2 ) === _PADCHAR ) {
        pads = 2;
      }

      // either way, we want to ignore this last block
      imax -= 4;
    }

    for ( i = 0; i < imax; i += 4 ) {
      b10 = ( _getbyte64( s, i ) << 18 ) | ( _getbyte64( s, i + 1 ) << 12 ) | ( _getbyte64( s, i + 2 ) << 6 ) | _getbyte64( s, i + 3 );
      x.push( String.fromCharCode( b10 >> 16, ( b10 >> 8 ) & 0xff, b10 & 0xff ) );
    }

    switch ( pads ) {
      case 1:
        b10 = ( _getbyte64( s, i ) << 18 ) | ( _getbyte64( s, i + 1 ) << 12 ) | ( _getbyte64( s, i + 2 ) << 6 );
        x.push( String.fromCharCode( b10 >> 16, ( b10 >> 8 ) & 0xff ) );
        break;

      case 2:
        b10 = ( _getbyte64( s, i ) << 18) | ( _getbyte64( s, i + 1 ) << 12 );
        x.push( String.fromCharCode( b10 >> 16 ) );
        break;
    }

    return x.join( "" );
  }


  function _getbyte( s, i ) {
    var x = s.charCodeAt( i );

    if ( x > 255 ) {
      throw "INVALID_CHARACTER_ERR: DOM Exception 5";
    }

    return x;
  }


  function _encode( s ) {
    if ( arguments.length !== 1 ) {
      throw "SyntaxError: exactly one argument required";
    }

    if ($.isArray(s)) {
        var str = "";
        for (var j = 0; j < s.length; j++) {
            str = str + String.fromCharCode(s[j]);
        }
        s = str
    } else {
        s = String( s );
    }

    var i,
      b10,
      x = [],
      imax = s.length - s.length % 3;

    if ( s.length === 0 ) {
      return s;
    }

    for ( i = 0; i < imax; i += 3 ) {
      b10 = ( _getbyte( s, i ) << 16 ) | ( _getbyte( s, i + 1 ) << 8 ) | _getbyte( s, i + 2 );
      x.push( _ALPHA.charAt( b10 >> 18 ) );
      x.push( _ALPHA.charAt( ( b10 >> 12 ) & 0x3F ) );
      x.push( _ALPHA.charAt( ( b10 >> 6 ) & 0x3f ) );
      x.push( _ALPHA.charAt( b10 & 0x3f ) );
    }

    switch ( s.length - imax ) {
      case 1:
        b10 = _getbyte( s, i ) << 16;
        x.push( _ALPHA.charAt( b10 >> 18 ) + _ALPHA.charAt( ( b10 >> 12 ) & 0x3F ) + _PADCHAR + _PADCHAR );
        break;

      case 2:
        b10 = ( _getbyte( s, i ) << 16 ) | ( _getbyte( s, i + 1 ) << 8 );
        x.push( _ALPHA.charAt( b10 >> 18 ) + _ALPHA.charAt( ( b10 >> 12 ) & 0x3F ) + _ALPHA.charAt( ( b10 >> 6 ) & 0x3f ) + _PADCHAR );
        break;
    }

    return x.join( "" );
  }


  return {
    decode: _decode,
    encode: _encode,
    VERSION: _VERSION
  };

}( jQuery ) );
//     Underscore.js 1.4.2
//     http://underscorejs.org
//     (c) 2009-2012 Jeremy Ashkenas, DocumentCloud Inc.
//     Underscore may be freely distributed under the MIT license.
(function(){var e=this,t=e._,n={},r=Array.prototype,i=Object.prototype,s=Function.prototype,o=r.push,u=r.slice,a=r.concat,f=r.unshift,l=i.toString,c=i.hasOwnProperty,h=r.forEach,p=r.map,d=r.reduce,v=r.reduceRight,m=r.filter,g=r.every,y=r.some,b=r.indexOf,w=r.lastIndexOf,E=Array.isArray,S=Object.keys,x=s.bind,T=function(e){if(e instanceof T)return e;if(!(this instanceof T))return new T(e);this._wrapped=e};typeof exports!="undefined"?(typeof module!="undefined"&&module.exports&&(exports=module.exports=T),exports._=T):e._=T,T.VERSION="1.4.2";var N=T.each=T.forEach=function(e,t,r){if(e==null)return;if(h&&e.forEach===h)e.forEach(t,r);else if(e.length===+e.length){for(var i=0,s=e.length;i<s;i++)if(t.call(r,e[i],i,e)===n)return}else for(var o in e)if(T.has(e,o)&&t.call(r,e[o],o,e)===n)return};T.map=T.collect=function(e,t,n){var r=[];return e==null?r:p&&e.map===p?e.map(t,n):(N(e,function(e,i,s){r[r.length]=t.call(n,e,i,s)}),r)},T.reduce=T.foldl=T.inject=function(e,t,n,r){var i=arguments.length>2;e==null&&(e=[]);if(d&&e.reduce===d)return r&&(t=T.bind(t,r)),i?e.reduce(t,n):e.reduce(t);N(e,function(e,s,o){i?n=t.call(r,n,e,s,o):(n=e,i=!0)});if(!i)throw new TypeError("Reduce of empty array with no initial value");return n},T.reduceRight=T.foldr=function(e,t,n,r){var i=arguments.length>2;e==null&&(e=[]);if(v&&e.reduceRight===v)return r&&(t=T.bind(t,r)),arguments.length>2?e.reduceRight(t,n):e.reduceRight(t);var s=e.length;if(s!==+s){var o=T.keys(e);s=o.length}N(e,function(u,a,f){a=o?o[--s]:--s,i?n=t.call(r,n,e[a],a,f):(n=e[a],i=!0)});if(!i)throw new TypeError("Reduce of empty array with no initial value");return n},T.find=T.detect=function(e,t,n){var r;return C(e,function(e,i,s){if(t.call(n,e,i,s))return r=e,!0}),r},T.filter=T.select=function(e,t,n){var r=[];return e==null?r:m&&e.filter===m?e.filter(t,n):(N(e,function(e,i,s){t.call(n,e,i,s)&&(r[r.length]=e)}),r)},T.reject=function(e,t,n){var r=[];return e==null?r:(N(e,function(e,i,s){t.call(n,e,i,s)||(r[r.length]=e)}),r)},T.every=T.all=function(e,t,r){t||(t=T.identity);var i=!0;return e==null?i:g&&e.every===g?e.every(t,r):(N(e,function(e,s,o){if(!(i=i&&t.call(r,e,s,o)))return n}),!!i)};var C=T.some=T.any=function(e,t,r){t||(t=T.identity);var i=!1;return e==null?i:y&&e.some===y?e.some(t,r):(N(e,function(e,s,o){if(i||(i=t.call(r,e,s,o)))return n}),!!i)};T.contains=T.include=function(e,t){var n=!1;return e==null?n:b&&e.indexOf===b?e.indexOf(t)!=-1:(n=C(e,function(e){return e===t}),n)},T.invoke=function(e,t){var n=u.call(arguments,2);return T.map(e,function(e){return(T.isFunction(t)?t:e[t]).apply(e,n)})},T.pluck=function(e,t){return T.map(e,function(e){return e[t]})},T.where=function(e,t){return T.isEmpty(t)?[]:T.filter(e,function(e){for(var n in t)if(t[n]!==e[n])return!1;return!0})},T.max=function(e,t,n){if(!t&&T.isArray(e)&&e[0]===+e[0]&&e.length<65535)return Math.max.apply(Math,e);if(!t&&T.isEmpty(e))return-Infinity;var r={computed:-Infinity};return N(e,function(e,i,s){var o=t?t.call(n,e,i,s):e;o>=r.computed&&(r={value:e,computed:o})}),r.value},T.min=function(e,t,n){if(!t&&T.isArray(e)&&e[0]===+e[0]&&e.length<65535)return Math.min.apply(Math,e);if(!t&&T.isEmpty(e))return Infinity;var r={computed:Infinity};return N(e,function(e,i,s){var o=t?t.call(n,e,i,s):e;o<r.computed&&(r={value:e,computed:o})}),r.value},T.shuffle=function(e){var t,n=0,r=[];return N(e,function(e){t=T.random(n++),r[n-1]=r[t],r[t]=e}),r};var k=function(e){return T.isFunction(e)?e:function(t){return t[e]}};T.sortBy=function(e,t,n){var r=k(t);return T.pluck(T.map(e,function(e,t,i){return{value:e,index:t,criteria:r.call(n,e,t,i)}}).sort(function(e,t){var n=e.criteria,r=t.criteria;if(n!==r){if(n>r||n===void 0)return 1;if(n<r||r===void 0)return-1}return e.index<t.index?-1:1}),"value")};var L=function(e,t,n,r){var i={},s=k(t);return N(e,function(t,o){var u=s.call(n,t,o,e);r(i,u,t)}),i};T.groupBy=function(e,t,n){return L(e,t,n,function(e,t,n){(T.has(e,t)?e[t]:e[t]=[]).push(n)})},T.countBy=function(e,t,n){return L(e,t,n,function(e,t,n){T.has(e,t)||(e[t]=0),e[t]++})},T.sortedIndex=function(e,t,n,r){n=n==null?T.identity:k(n);var i=n.call(r,t),s=0,o=e.length;while(s<o){var u=s+o>>>1;n.call(r,e[u])<i?s=u+1:o=u}return s},T.toArray=function(e){return e?e.length===+e.length?u.call(e):T.values(e):[]},T.size=function(e){return e.length===+e.length?e.length:T.keys(e).length},T.first=T.head=T.take=function(e,t,n){return t!=null&&!n?u.call(e,0,t):e[0]},T.initial=function(e,t,n){return u.call(e,0,e.length-(t==null||n?1:t))},T.last=function(e,t,n){return t!=null&&!n?u.call(e,Math.max(e.length-t,0)):e[e.length-1]},T.rest=T.tail=T.drop=function(e,t,n){return u.call(e,t==null||n?1:t)},T.compact=function(e){return T.filter(e,function(e){return!!e})};var A=function(e,t,n){return N(e,function(e){T.isArray(e)?t?o.apply(n,e):A(e,t,n):n.push(e)}),n};T.flatten=function(e,t){return A(e,t,[])},T.without=function(e){return T.difference(e,u.call(arguments,1))},T.uniq=T.unique=function(e,t,n,r){var i=n?T.map(e,n,r):e,s=[],o=[];return N(i,function(n,r){if(t?!r||o[o.length-1]!==n:!T.contains(o,n))o.push(n),s.push(e[r])}),s},T.union=function(){return T.uniq(a.apply(r,arguments))},T.intersection=function(e){var t=u.call(arguments,1);return T.filter(T.uniq(e),function(e){return T.every(t,function(t){return T.indexOf(t,e)>=0})})},T.difference=function(e){var t=a.apply(r,u.call(arguments,1));return T.filter(e,function(e){return!T.contains(t,e)})},T.zip=function(){var e=u.call(arguments),t=T.max(T.pluck(e,"length")),n=new Array(t);for(var r=0;r<t;r++)n[r]=T.pluck(e,""+r);return n},T.object=function(e,t){var n={};for(var r=0,i=e.length;r<i;r++)t?n[e[r]]=t[r]:n[e[r][0]]=e[r][1];return n},T.indexOf=function(e,t,n){if(e==null)return-1;var r=0,i=e.length;if(n){if(typeof n!="number")return r=T.sortedIndex(e,t),e[r]===t?r:-1;r=n<0?Math.max(0,i+n):n}if(b&&e.indexOf===b)return e.indexOf(t,n);for(;r<i;r++)if(e[r]===t)return r;return-1},T.lastIndexOf=function(e,t,n){if(e==null)return-1;var r=n!=null;if(w&&e.lastIndexOf===w)return r?e.lastIndexOf(t,n):e.lastIndexOf(t);var i=r?n:e.length;while(i--)if(e[i]===t)return i;return-1},T.range=function(e,t,n){arguments.length<=1&&(t=e||0,e=0),n=arguments[2]||1;var r=Math.max(Math.ceil((t-e)/n),0),i=0,s=new Array(r);while(i<r)s[i++]=e,e+=n;return s};var O=function(){};T.bind=function(t,n){var r,i;if(t.bind===x&&x)return x.apply(t,u.call(arguments,1));if(!T.isFunction(t))throw new TypeError;return i=u.call(arguments,2),r=function(){if(this instanceof r){O.prototype=t.prototype;var e=new O,s=t.apply(e,i.concat(u.call(arguments)));return Object(s)===s?s:e}return t.apply(n,i.concat(u.call(arguments)))}},T.bindAll=function(e){var t=u.call(arguments,1);return t.length==0&&(t=T.functions(e)),N(t,function(t){e[t]=T.bind(e[t],e)}),e},T.memoize=function(e,t){var n={};return t||(t=T.identity),function(){var r=t.apply(this,arguments);return T.has(n,r)?n[r]:n[r]=e.apply(this,arguments)}},T.delay=function(e,t){var n=u.call(arguments,2);return setTimeout(function(){return e.apply(null,n)},t)},T.defer=function(e){return T.delay.apply(T,[e,1].concat(u.call(arguments,1)))},T.throttle=function(e,t){var n,r,i,s,o,u,a=T.debounce(function(){o=s=!1},t);return function(){n=this,r=arguments;var f=function(){i=null,o&&(u=e.apply(n,r)),a()};return i||(i=setTimeout(f,t)),s?o=!0:(s=!0,u=e.apply(n,r)),a(),u}},T.debounce=function(e,t,n){var r,i;return function(){var s=this,o=arguments,u=function(){r=null,n||(i=e.apply(s,o))},a=n&&!r;return clearTimeout(r),r=setTimeout(u,t),a&&(i=e.apply(s,o)),i}},T.once=function(e){var t=!1,n;return function(){return t?n:(t=!0,n=e.apply(this,arguments),e=null,n)}},T.wrap=function(e,t){return function(){var n=[e];return o.apply(n,arguments),t.apply(this,n)}},T.compose=function(){var e=arguments;return function(){var t=arguments;for(var n=e.length-1;n>=0;n--)t=[e[n].apply(this,t)];return t[0]}},T.after=function(e,t){return e<=0?t():function(){if(--e<1)return t.apply(this,arguments)}},T.keys=S||function(e){if(e!==Object(e))throw new TypeError("Invalid object");var t=[];for(var n in e)T.has(e,n)&&(t[t.length]=n);return t},T.values=function(e){var t=[];for(var n in e)T.has(e,n)&&t.push(e[n]);return t},T.pairs=function(e){var t=[];for(var n in e)T.has(e,n)&&t.push([n,e[n]]);return t},T.invert=function(e){var t={};for(var n in e)T.has(e,n)&&(t[e[n]]=n);return t},T.functions=T.methods=function(e){var t=[];for(var n in e)T.isFunction(e[n])&&t.push(n);return t.sort()},T.extend=function(e){return N(u.call(arguments,1),function(t){for(var n in t)e[n]=t[n]}),e},T.pick=function(e){var t={},n=a.apply(r,u.call(arguments,1));return N(n,function(n){n in e&&(t[n]=e[n])}),t},T.omit=function(e){var t={},n=a.apply(r,u.call(arguments,1));for(var i in e)T.contains(n,i)||(t[i]=e[i]);return t},T.defaults=function(e){return N(u.call(arguments,1),function(t){for(var n in t)e[n]==null&&(e[n]=t[n])}),e},T.clone=function(e){return T.isObject(e)?T.isArray(e)?e.slice():T.extend({},e):e},T.tap=function(e,t){return t(e),e};var M=function(e,t,n,r){if(e===t)return e!==0||1/e==1/t;if(e==null||t==null)return e===t;e instanceof T&&(e=e._wrapped),t instanceof T&&(t=t._wrapped);var i=l.call(e);if(i!=l.call(t))return!1;switch(i){case"[object String]":return e==String(t);case"[object Number]":return e!=+e?t!=+t:e==0?1/e==1/t:e==+t;case"[object Date]":case"[object Boolean]":return+e==+t;case"[object RegExp]":return e.source==t.source&&e.global==t.global&&e.multiline==t.multiline&&e.ignoreCase==t.ignoreCase}if(typeof e!="object"||typeof t!="object")return!1;var s=n.length;while(s--)if(n[s]==e)return r[s]==t;n.push(e),r.push(t);var o=0,u=!0;if(i=="[object Array]"){o=e.length,u=o==t.length;if(u)while(o--)if(!(u=M(e[o],t[o],n,r)))break}else{var a=e.constructor,f=t.constructor;if(a!==f&&!(T.isFunction(a)&&a instanceof a&&T.isFunction(f)&&f instanceof f))return!1;for(var c in e)if(T.has(e,c)){o++;if(!(u=T.has(t,c)&&M(e[c],t[c],n,r)))break}if(u){for(c in t)if(T.has(t,c)&&!(o--))break;u=!o}}return n.pop(),r.pop(),u};T.isEqual=function(e,t){return M(e,t,[],[])},T.isEmpty=function(e){if(e==null)return!0;if(T.isArray(e)||T.isString(e))return e.length===0;for(var t in e)if(T.has(e,t))return!1;return!0},T.isElement=function(e){return!!e&&e.nodeType===1},T.isArray=E||function(e){return l.call(e)=="[object Array]"},T.isObject=function(e){return e===Object(e)},N(["Arguments","Function","String","Number","Date","RegExp"],function(e){T["is"+e]=function(t){return l.call(t)=="[object "+e+"]"}}),T.isArguments(arguments)||(T.isArguments=function(e){return!!e&&!!T.has(e,"callee")}),typeof /./!="function"&&(T.isFunction=function(e){return typeof e=="function"}),T.isFinite=function(e){return T.isNumber(e)&&isFinite(e)},T.isNaN=function(e){return T.isNumber(e)&&e!=+e},T.isBoolean=function(e){return e===!0||e===!1||l.call(e)=="[object Boolean]"},T.isNull=function(e){return e===null},T.isUndefined=function(e){return e===void 0},T.has=function(e,t){return c.call(e,t)},T.noConflict=function(){return e._=t,this},T.identity=function(e){return e},T.times=function(e,t,n){for(var r=0;r<e;r++)t.call(n,r)},T.random=function(e,t){return t==null&&(t=e,e=0),e+(0|Math.random()*(t-e+1))};var _={escape:{"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#x27;","/":"&#x2F;"}};_.unescape=T.invert(_.escape);var D={escape:new RegExp("["+T.keys(_.escape).join("")+"]","g"),unescape:new RegExp("("+T.keys(_.unescape).join("|")+")","g")};T.each(["escape","unescape"],function(e){T[e]=function(t){return t==null?"":(""+t).replace(D[e],function(t){return _[e][t]})}}),T.result=function(e,t){if(e==null)return null;var n=e[t];return T.isFunction(n)?n.call(e):n},T.mixin=function(e){N(T.functions(e),function(t){var n=T[t]=e[t];T.prototype[t]=function(){var e=[this._wrapped];return o.apply(e,arguments),F.call(this,n.apply(T,e))}})};var P=0;T.uniqueId=function(e){var t=P++;return e?e+t:t},T.templateSettings={evaluate:/<%([\s\S]+?)%>/g,interpolate:/<%=([\s\S]+?)%>/g,escape:/<%-([\s\S]+?)%>/g};var H=/(.)^/,B={"'":"'","\\":"\\","\r":"r","\n":"n","  ":"t","\u2028":"u2028","\u2029":"u2029"},j=/\\|'|\r|\n|\t|\u2028|\u2029/g;T.template=function(e,t,n){n=T.defaults({},n,T.templateSettings);var r=new RegExp([(n.escape||H).source,(n.interpolate||H).source,(n.evaluate||H).source].join("|")+"|$","g"),i=0,s="__p+='";e.replace(r,function(t,n,r,o,u){s+=e.slice(i,u).replace(j,function(e){return"\\"+B[e]}),s+=n?"'+\n((__t=("+n+"))==null?'':_.escape(__t))+\n'":r?"'+\n((__t=("+r+"))==null?'':__t)+\n'":o?"';\n"+o+"\n__p+='":"",i=u+t.length}),s+="';\n",n.variable||(s="with(obj||{}){\n"+s+"}\n"),s="var __t,__p='',__j=Array.prototype.join,print=function(){__p+=__j.call(arguments,'');};\n"+s+"return __p;\n";try{var o=new Function(n.variable||"obj","_",s)}catch(u){throw u.source=s,u}if(t)return o(t,T);var a=function(e){return o.call(this,e,T)};return a.source="function("+(n.variable||"obj")+"){\n"+s+"}",a},T.chain=function(e){return T(e).chain()};var F=function(e){return this._chain?T(e).chain():e};T.mixin(T),N(["pop","push","reverse","shift","sort","splice","unshift"],function(e){var t=r[e];T.prototype[e]=function(){var n=this._wrapped;return t.apply(n,arguments),(e=="shift"||e=="splice")&&n.length===0&&delete n[0],F.call(this,n)}}),N(["concat","join","slice"],function(e){var t=r[e];T.prototype[e]=function(){return F.call(this,t.apply(this._wrapped,arguments))}}),T.extend(T.prototype,{chain:function(){return this._chain=!0,this},value:function(){return this._wrapped}})}).call(this);
/* Simple JavaScript Inheritance
 * By John Resig http://ejohn.org/
 * MIT Licensed.
 */
// Inspired by base2 and Prototype
// Slightly modified to run in strict mode
(function(){
  var initializing = false, fnTest = /xyz/.test(function(){xyz;}) ? /\b_super\b/ : /.*/;
  // The base Class implementation (does nothing)
  window.Class = function(){};

  // Create a new Class that inherits from this class
  Class.extend = function extend(prop) {
    var _super = this.prototype;

    // Instantiate a base class (but only create the instance,
    // don't run the init constructor)
    initializing = true;
    var prototype = new this();
    initializing = false;

    // Copy the properties over onto the new prototype
    for (var name in prop) {
      // Check if we're overwriting an existing function
      prototype[name] = typeof prop[name] == "function" &&
        typeof _super[name] == "function" && fnTest.test(prop[name]) ?
        (function(name, fn){
          return function() {
            var tmp = this._super;

            // Add a new ._super() method that is the same method
            // but on the super-class
            this._super = _super[name];

            // The method only need to be bound temporarily, so we
            // remove it when we're done executing
            var ret = fn.apply(this, arguments);
            this._super = tmp;

            return ret;
          };
        })(name, prop[name]) :
        prop[name];
    }

    // The dummy class constructor
    function Class() {
      // All construction is actually done in the init method
      if ( !initializing && this.init )
        this.init.apply(this, arguments);
    }

    // Populate our constructed prototype object
    Class.prototype = prototype;

    // Enforce the constructor to be what we expect
    Class.prototype.constructor = Class;

    // And make this class extendable
    Class.extend = extend;

    return Class;
  };
})();
var Conversion = (function() {
  return {
    intFromBytes: function(x) {
      var val = 0;
      for (var i = 0; i < x.length; ++i) {
        val += x[i];
        if (i < x.length - 1) {
          val = val << 8;
        }
      }
      return val;
    },
    bytesFromInt: function(x, arraySize) {
      var bytes = [];
      var i = arraySize;
      do {
        bytes[--i] = x & (255);
        x = x >> 8;
      } while (i);
      return bytes;
    },

    /**
     * CRC-16-CCITT implementation
     * http://introcs.cs.princeton.edu/java/51data/CRC16CCITT.java.html
     * @param array
     */
    crc: function(array) {
      var crc = 0xFFFF;
      var polynomial = 0x1021;

      for (var i = 0; i < array.length; i++) {
        var b = array[i];
        for (var j = 0; j < 8; j++) {
          var bit = ((b >> (7 - j) & 1) == 1);
          var c15 = ((crc >> 15 & 1) == 1);
          crc <<= 1;
          if (c15 ^ bit) {
            crc ^= polynomial;
          }
        }
      }
      crc &= 0xFFFF;

      return crc;
    }
  };
})();
function toa(text) {
  var bytes = [];
  for (var i = 0; i < text.length; i++) {
    bytes.push(text[i].charCodeAt(0));
  }
  return bytes;
}
function hex(bytes) {
  var array = [];
  _.each(bytes, function(item) {
    var b = item.toString(16).toUpperCase();
    if (b.length == 1) {
      b = '0' + b;
    }
    array.push(b);
  });
  return array.join(' ');
}
var resources = {
    constants:{
        PNDR_SESSION_START:"PNDR_SESSION_START",
        PNDR_UPDATE_BRANDING_IMAGE:"PNDR_UPDATE_BRANDING_IMAGE",
        PNDR_RETURN_BRANDING_IMAGE_SEGMENT:"PNDR_RETURN_BRANDING_IMAGE_SEGMENT",
        PNDR_GET_STATUS:"PNDR_GET_STATUS",
        PNDR_GET_NOTICE_TEXT:"PNDR_GET_NOTICE_TEXT",
        PNDR_SESSION_TERMINATE:"PNDR_SESSION_TERMINATE",
        PNDR_GET_LISTENER:"PNDR_GET_LISTENER",
        PNDR_GET_TRACK_INFO:"PNDR_GET_TRACK_INFO",
        PNDR_GET_TRACK_TITLE:"PNDR_GET_TRACK_TITLE",
        PNDR_GET_TRACK_ARTIST:"PNDR_GET_TRACK_ARTIST",
        PNDR_GET_TRACK_ALBUM:"PNDR_GET_TRACK_ALBUM",
        PNDR_GET_TRACK_ALBUM_ART:"PNDR_GET_TRACK_ALBUM_ART",
        PNDR_SET_TRACK_ELAPSED_POLLING:"PNDR_SET_TRACK_ELAPSED_POLLING",
        PNDR_GET_TRACK_INFO_EXTENDED:"PNDR_GET_TRACK_INFO_EXTENDED",
        PNDR_EVENT_TRACK_PLAY:"PNDR_EVENT_TRACK_PLAY",
        PNDR_EVENT_TRACK_PAUSE:"PNDR_EVENT_TRACK_PAUSE",
        PNDR_EVENT_TRACK_SKIP:"PNDR_EVENT_TRACK_SKIP",
        PNDR_EVENT_TRACK_RATE_POSITIVE:"PNDR_EVENT_TRACK_RATE_POSITIVE",
        PNDR_EVENT_TRACK_RATE_NEGATIVE:"PNDR_EVENT_TRACK_RATE_NEGATIVE",
        PNDR_EVENT_TRACK_EXPLAIN:"PNDR_EVENT_TRACK_EXPLAIN",
        PNDR_GET_TRACK_EXPLAIN:"PNDR_GET_TRACK_EXPLAIN",
        PNDR_EVENT_TRACK_BOOKMARK_TRACK:"PNDR_EVENT_TRACK_BOOKMARK_TRACK",
        PNDR_EVENT_TRACK_BOOKMARK_ARTIST:"PNDR_EVENT_TRACK_BOOKMARK_ARTIST",
        PNDR_GET_STATION_ACTIVE:"PNDR_GET_STATION_ACTIVE",
        PNDR_GET_STATION_COUNT:"PNDR_GET_STATION_COUNT",
        PNDR_GET_STATION_TOKENS:"PNDR_GET_STATION_TOKENS",
        PNDR_GET_ALL_STATION_TOKENS:"PNDR_GET_ALL_STATION_TOKENS",
        PNDR_GET_STATION_INFO:"PNDR_GET_STATION_INFO",
        PNDR_GET_STATIONS_ORDER:"PNDR_GET_STATIONS_ORDER",
        PNDR_EVENT_STATIONS_SORT:"PNDR_EVENT_STATIONS_SORT",
        PNDR_EVENT_STATION_SELECT:"PNDR_EVENT_STATION_SELECT",
        PNDR_EVENT_STATION_DELETE:"PNDR_EVENT_STATION_DELETE",
        PNDR_EVENT_STATION_CREATE_FROM_CURRENT_ARTIST:"PNDR_EVENT_STATION_CREATE_FROM_CURRENT_ARTIST",
        PNDR_EVENT_STATION_CREATE_FROM_CURRENT_TRACK:"PNDR_EVENT_STATION_CREATE_FROM_CURRENT_TRACK",
        PNDR_GET_STATION_ART:"PNDR_GET_STATION_ART",
        PNDR_EVENT_CANCEL_STATION_ART:"PNDR_EVENT_CANCEL_STATION_ART",
        PNDR_GET_GENRE_CATEGORY_COUNT:"PNDR_GET_GENRE_CATEGORY_COUNT",
        PNDR_GET_GENRE_CATEGORY_NAMES:"PNDR_GET_GENRE_CATEGORY_NAMES",
        PNDR_GET_ALL_GENRE_CATEGORY_NAMES:"PNDR_GET_ALL_GENRE_CATEGORY_NAMES",
        PNDR_GET_GENRE_CATEGORY_STATION_COUNT:"PNDR_GET_GENRE_CATEGORY_STATION_COUNT",
        PNDR_GET_GENRE_STATION_NAMES:"PNDR_GET_GENRE_STATION_NAMES",
        PNDR_EVENT_SELECT_GENRE_STATION:"PNDR_EVENT_SELECT_GENRE_STATION",
        PNDR_GET_GENRE_STATION_ART:"PNDR_GET_GENRE_STATION_ART",
        PNDR_EVENT_CANCEL_GENRE_STATION_ART:"PNDR_EVENT_CANCEL_GENRE_STATION_ART",
        PNDR_EVENT_SEARCH_AUTO_COMPLETE:"PNDR_EVENT_SEARCH_AUTO_COMPLETE",
        PNDR_EVENT_SEARCH_EXTENDED:"PNDR_EVENT_SEARCH_EXTENDED",
        PNDR_GET_SEARCH_RESULT_INFO:"PNDR_GET_SEARCH_RESULT_INFO",
        PNDR_EVENT_SEARCH_SELECT:"PNDR_EVENT_SEARCH_SELECT",
        PNDR_SEARCH_DISCARD:"PNDR_SEARCH_DISCARD",
        PNDR_EVENT_OPEN_APP:"PNDR_EVENT_OPEN_APP",
        PNDR_GET_BRANDING_IMAGE:"PNDR_GET_BRANDING_IMAGE",
        PNDR_UPDATE_STATUS:"PNDR_UPDATE_STATUS",
        PNDR_RETURN_STATUS:"PNDR_RETURN_STATUS",
        PNDR_UPDATE_NOTICE:"PNDR_UPDATE_NOTICE",
        PNDR_RETURN_NOTICE_TEXT:"PNDR_RETURN_NOTICE_TEXT",
        PNDR_RETURN_LISTENER:"PNDR_RETURN_LISTENER",
        PNDR_UPDATE_TRACK:"PNDR_UPDATE_TRACK",
        PNDR_RETURN_TRACK_INFO:"PNDR_RETURN_TRACK_INFO",
        PNDR_RETURN_TRACK_TITLE:"PNDR_RETURN_TRACK_TITLE",
        PNDR_RETURN_TRACK_ARTIST:"PNDR_RETURN_TRACK_ARTIST",
        PNDR_RETURN_TRACK_ALBUM:"PNDR_RETURN_TRACK_ALBUM",
        PNDR_RETURN_TRACK_ALBUM_ART_SEGMENT:"PNDR_RETURN_TRACK_ALBUM_ART_SEGMENT",
        PNDR_UPDATE_TRACK_ALBUM_ART:"PNDR_UPDATE_TRACK_ALBUM_ART",
        PNDR_UPDATE_TRACK_ELAPSED:"PNDR_UPDATE_TRACK_ELAPSED",
        PNDR_UPDATE_TRACK_RATING:"PNDR_UPDATE_TRACK_RATING",
        PNDR_UPDATE_TRACK_EXPLAIN:"PNDR_UPDATE_TRACK_EXPLAIN",
        PNDR_RETURN_TRACK_EXPLAIN_SEGMENT:"PNDR_RETURN_TRACK_EXPLAIN_SEGMENT",
        PNDR_UPDATE_TRACK_BOOKMARK_TRACK:"PNDR_UPDATE_TRACK_BOOKMARK_TRACK",
        PNDR_UPDATE_TRACK_BOOKMARK_ARTIST:"PNDR_UPDATE_TRACK_BOOKMARK_ARTIST",
        PNDR_RETURN_TRACK_INFO_EXTENDED:"PNDR_RETURN_TRACK_INFO_EXTENDED",
        PNDR_UPDATE_TRACK_COMPLETED:"PNDR_UPDATE_TRACK_COMPLETED",
        PNDR_UPDATE_STATION_ACTIVE:"PNDR_UPDATE_STATION_ACTIVE",
        PNDR_RETURN_STATION_ACTIVE:"PNDR_RETURN_STATION_ACTIVE",
        PNDR_RETURN_STATION_COUNT:"PNDR_RETURN_STATION_COUNT",
        PNDR_RETURN_STATION_TOKENS:"PNDR_RETURN_STATION_TOKENS",
        PNDR_RETURN_STATION_INFO:"PNDR_RETURN_STATION_INFO",
        PNDR_RETURN_STATIONS_ORDER:"PNDR_RETURN_STATIONS_ORDER",
        PNDR_UPDATE_STATIONS_ORDER:"PNDR_UPDATE_STATIONS_ORDER",
        PNDR_UPDATE_STATION_DELETED:"PNDR_UPDATE_STATION_DELETED",
        PNDR_RETURN_STATION_ART_SEGMENT:"PNDR_RETURN_STATION_ART_SEGMENT",
        PNDR_RETURN_GENRE_CATEGORY_COUNT:"PNDR_RETURN_GENRE_CATEGORY_COUNT",
        PNDR_RETURN_GENRE_CATEGORY_NAMES:"PNDR_RETURN_GENRE_CATEGORY_NAMES",
        PNDR_RETURN_GENRE_CATEGORY_STATION_COUNT:"PNDR_RETURN_GENRE_CATEGORY_STATION_COUNT",
        PNDR_RETURN_GENRE_STATION_NAMES:"PNDR_RETURN_GENRE_STATION_NAMES",
        PNDR_RETURN_GENRE_STATION_ART_SEGMENT:"PNDR_RETURN_GENRE_STATION_ART_SEGMENT",
        PNDR_UPDATE_SEARCH:"PNDR_UPDATE_SEARCH",
        PNDR_RETURN_SEARCH_RESULT_INFO:"PNDR_RETURN_SEARCH_RESULT_INFO",
        PNDR_UPDATE_STATION_ADDED:"PNDR_UPDATE_STATION_ADDED",
        PNDR_ECHO_REQUEST:"PNDR_ECHO_REQUEST",
        PNDR_ECHO_RESPONSE:"PNDR_ECHO_RESPONSE",
        PNDR_STATUS_UNKNOWN_ERROR:"PNDR_STATUS_UNKNOWN_ERROR",
        PNDR_NOTICE_SKIP_LIMIT_REACHED:"PNDR_NOTICE_SKIP_LIMIT_REACHED",
        PNDR_NOTICE_STATION_LIMIT_REACHED:"PNDR_NOTICE_STATION_LIMIT_REACHED",
        PNDR_NOTICE_ERROR_TRACK_RATING:"PNDR_NOTICE_ERROR_TRACK_RATING",
        PNDR_NOTICE_ERROR_STATION_DELETE:"PNDR_NOTICE_ERROR_STATION_DELETE",
        PNDR_NOTICE_ERROR_SEARCH_EXTENDED:"PNDR_NOTICE_ERROR_SEARCH_EXTENDED",
        PNDR_NOTICE_ERROR_SEARCH_SELECT:"PNDR_NOTICE_ERROR_SEARCH_SELECT",
        PNDR_NOTICE_ERROR_BOOKMARK:"PNDR_NOTICE_ERROR_BOOKMARK",
        PNDR_NOTICE_ERROR_MAINTENANCE:"PNDR_NOTICE_ERROR_MAINTENANCE",
        PNDR_NOTICE_ERROR_TRACK_EXPLAIN:"PNDR_NOTICE_ERROR_TRACK_EXPLAIN",
        PNDR_NOTICE_ERROR_SESSION_ALREADY_STARTED:"PNDR_NOTICE_ERROR_SESSION_ALREADY_STARTED",
        PNDR_NOTICE_ERROR_NO_ACTIVE_SESSION:"PNDR_NOTICE_ERROR_NO_ACTIVE_SESSION",
        PNDR_NOTICE_ERROR_APP_URL_NOT_SUPPORTED:"PNDR_NOTICE_ERROR_APP_URL_NOT_SUPPORTED",
        PNDR_NOTICE_ERROR_STATION_DOES_NOT_EXIST:"PNDR_NOTICE_ERROR_STATION_DOES_NOT_EXIST",
        PNDR_STATUS_LICENSING_RESTRICTIONS:"PNDR_STATUS_LICENSING_RESTRICTIONS",
        PNDR_STATUS_INVALID_LOGIN:"PNDR_STATUS_INVALID_LOGIN",
        PNDR_STATION_NONE:"PNDR_STATION_NONE",
        PNDR_TRACK_NONE:"PNDR_TRACK_NONE",
        PNDR_SORT_BY_NAME:"PNDR_SORT_BY_NAME",
        PNDR_SORT_BY_DATE:"PNDR_SORT_BY_DATE",
        PNDR_STATION_FLAG_ALLOW_DELETE: "PNDR_STATION_FLAG_ALLOW_DELETE",
        PNDR_STATION_FLAG_IS_SHARED: "PNDR_STATION_FLAG_IS_SHARED",
        PNDR_STATION_FLAG_IS_QUICKMIX: "PNDR_STATION_FLAG_IS_QUICKMIX"
    },
    command:{
        fromCode:{
            UNKNOWN:"UNKNOWN",
            0x00:"PNDR_SESSION_START",
            0x01:"PNDR_UPDATE_BRANDING_IMAGE",
            0x02:"PNDR_RETURN_BRANDING_IMAGE_SEGMENT",
            0x03:"PNDR_GET_STATUS",
            0x04:"PNDR_GET_NOTICE_TEXT",
            0x05:"PNDR_SESSION_TERMINATE",
            0x06:"PNDR_GET_LISTENER",

            0x10:"PNDR_GET_TRACK_INFO",
            0x11:"PNDR_GET_TRACK_TITLE",
            0x12:"PNDR_GET_TRACK_ARTIST",
            0x13:"PNDR_GET_TRACK_ALBUM",
            0x14:"PNDR_GET_TRACK_ALBUM_ART",
            0x15:"PNDR_SET_TRACK_ELAPSED_POLLING",
            0x16:"PNDR_GET_TRACK_INFO_EXTENDED",

            0x30:"PNDR_EVENT_TRACK_PLAY",
            0x31:"PNDR_EVENT_TRACK_PAUSE",
            0x32:"PNDR_EVENT_TRACK_SKIP",
            0x33:"PNDR_EVENT_TRACK_RATE_POSITIVE",
            0x34:"PNDR_EVENT_TRACK_RATE_NEGATIVE",
            0x35:"PNDR_EVENT_TRACK_EXPLAIN",
            0x36:"PNDR_GET_TRACK_EXPLAIN",
            0x37:"PNDR_EVENT_TRACK_BOOKMARK_TRACK",
            0x38:"PNDR_EVENT_TRACK_BOOKMARK_ARTIST",

            0x40:"PNDR_GET_STATION_ACTIVE",
            0x41:"PNDR_GET_STATION_COUNT",
            0x42:"PNDR_GET_STATION_TOKENS",
            0x43:"PNDR_GET_ALL_STATION_TOKENS",
            0x44:"PNDR_GET_STATION_INFO",
            0x45:"PNDR_GET_STATIONS_ORDER",
            0x46:"PNDR_EVENT_STATIONS_SORT",
            0x47:"PNDR_EVENT_STATION_SELECT",
            0x48:"PNDR_EVENT_STATION_DELETE",
            0x49:"PNDR_EVENT_STATION_CREATE_FROM_CURRENT_ARTIST",
            0x4a:"PNDR_EVENT_STATION_CREATE_FROM_CURRENT_TRACK",
            0x4b:"PNDR_GET_STATION_ART",
            0x4c:"PNDR_EVENT_CANCEL_STATION_ART",

            0x4d:"PNDR_GET_GENRE_CATEGORY_COUNT",
            0x4e:"PNDR_GET_GENRE_CATEGORY_NAMES",
            0x4f:"PNDR_GET_ALL_GENRE_CATEGORY_NAMES",
            0x50:"PNDR_GET_GENRE_CATEGORY_STATION_COUNT",
            0x51:"PNDR_GET_GENRE_STATION_NAMES",
            0x52:"PNDR_EVENT_SELECT_GENRE_STATION",
            0x53:"PNDR_GET_GENRE_STATION_ART",
            0x54:"PNDR_EVENT_CANCEL_GENRE_STATION_ART",

            0x60:"PNDR_EVENT_SEARCH_AUTO_COMPLETE",
            0x61:"PNDR_EVENT_SEARCH_EXTENDED",
            0x62:"PNDR_GET_SEARCH_RESULT_INFO",
            0x63:"PNDR_EVENT_SEARCH_SELECT",
            0x64:"PNDR_SEARCH_DISCARD",

            0x70:"PNDR_EVENT_OPEN_APP",

            0x80:"PNDR_GET_BRANDING_IMAGE",
            0x81:"PNDR_UPDATE_STATUS",
            0x82:"PNDR_RETURN_STATUS",
            0x83:"PNDR_UPDATE_NOTICE",
            0x84:"PNDR_RETURN_NOTICE_TEXT",
            0x85:"PNDR_RETURN_LISTENER",

            0x90:"PNDR_UPDATE_TRACK",
            0x91:"PNDR_RETURN_TRACK_INFO",
            0x92:"PNDR_RETURN_TRACK_TITLE",
            0x93:"PNDR_RETURN_TRACK_ARTIST",
            0x94:"PNDR_RETURN_TRACK_ALBUM",
            0x95:"PNDR_RETURN_TRACK_ALBUM_ART_SEGMENT",
            0x96:"PNDR_UPDATE_TRACK_ALBUM_ART",
            0x97:"PNDR_UPDATE_TRACK_ELAPSED",
            0x98:"PNDR_UPDATE_TRACK_RATING",
            0x99:"PNDR_UPDATE_TRACK_EXPLAIN",
            0x9a:"PNDR_RETURN_TRACK_EXPLAIN_SEGMENT",
            0x9b:"PNDR_UPDATE_TRACK_BOOKMARK_TRACK",
            0x9c:"PNDR_UPDATE_TRACK_BOOKMARK_ARTIST",
            0x9d:"PNDR_RETURN_TRACK_INFO_EXTENDED",
            0x9e:"PNDR_UPDATE_TRACK_COMPLETED",

            0xb1:"PNDR_RETURN_STATION_ACTIVE",
            0xb2:"PNDR_RETURN_STATION_COUNT",
            0xb3:"PNDR_RETURN_STATION_TOKENS",
            0xb4:"PNDR_RETURN_STATION_INFO",
            0xb5:"PNDR_RETURN_STATIONS_ORDER",
            0xb6:"PNDR_UPDATE_STATIONS_ORDER",
            0xb7:"PNDR_UPDATE_STATION_DELETED",
            0xb8:"PNDR_RETURN_STATION_ART_SEGMENT",

            0xb9:"PNDR_RETURN_GENRE_CATEGORY_COUNT",
            0xba:"PNDR_UPDATE_STATION_ACTIVE",
            0xbb:"PNDR_RETURN_GENRE_CATEGORY_NAMES",
            0xbc:"PNDR_RETURN_GENRE_CATEGORY_STATION_COUNT",
            0xbd:"PNDR_RETURN_GENRE_STATION_NAMES",
            0xbe:"PNDR_RETURN_GENRE_STATION_ART_SEGMENT",


            0xd0:"PNDR_UPDATE_SEARCH",
            0xd1:"PNDR_RETURN_SEARCH_RESULT_INFO",
            0xd2:"PNDR_UPDATE_STATION_ADDED",

            0x7f:"PNDR_ECHO_REQUEST",
            0xff:"PNDR_ECHO_RESPONSE"

        },

        toCode:{
            PNDR_SESSION_START:0x00,
            PNDR_UPDATE_BRANDING_IMAGE:0x01,
            PNDR_RETURN_BRANDING_IMAGE_SEGMENT:0x02,
            PNDR_GET_STATUS:0x03,
            PNDR_GET_NOTICE_TEXT:0x04,
            PNDR_SESSION_TERMINATE:0x05,
            PNDR_GET_LISTENER:0x06,

            PNDR_GET_TRACK_INFO:0x10,
            PNDR_GET_TRACK_TITLE:0x11,
            PNDR_GET_TRACK_ARTIST:0x12,
            PNDR_GET_TRACK_ALBUM:0x13,
            PNDR_GET_TRACK_ALBUM_ART:0x14,
            PNDR_SET_TRACK_ELAPSED_POLLING:0x15,
            PNDR_GET_TRACK_INFO_EXTENDED:0x16,

            PNDR_EVENT_TRACK_PLAY:0x30,
            PNDR_EVENT_TRACK_PAUSE:0x31,
            PNDR_EVENT_TRACK_SKIP:0x32,
            PNDR_EVENT_TRACK_RATE_POSITIVE:0x33,
            PNDR_EVENT_TRACK_RATE_NEGATIVE:0x34,
            PNDR_EVENT_TRACK_EXPLAIN:0x35,
            PNDR_GET_TRACK_EXPLAIN:0x36,
            PNDR_EVENT_TRACK_BOOKMARK_TRACK:0x37,
            PNDR_EVENT_TRACK_BOOKMARK_ARTIST:0x38,

            PNDR_GET_STATION_ACTIVE:0x40,
            PNDR_GET_STATION_COUNT:0x41,
            PNDR_GET_STATION_TOKENS:0x42,
            PNDR_GET_ALL_STATION_TOKENS:0x43,
            PNDR_GET_STATION_INFO:0x44,
            PNDR_GET_STATIONS_ORDER:0x45,
            PNDR_EVENT_STATIONS_SORT:0x46,
            PNDR_EVENT_STATION_SELECT:0x47,
            PNDR_EVENT_STATION_DELETE:0x48,
            PNDR_EVENT_STATION_CREATE_FROM_CURRENT_ARTIST:0x49,
            PNDR_EVENT_STATION_CREATE_FROM_CURRENT_TRACK:0x4a,
            PNDR_GET_STATION_ART:0x4b,
            PNDR_EVENT_CANCEL_STATION_ART:0x4c,

            PNDR_GET_GENRE_CATEGORY_COUNT:0x4d,
            PNDR_GET_GENRE_CATEGORY_NAMES:0x4e,
            PNDR_GET_ALL_GENRE_CATEGORY_NAMES:0x4f,
            PNDR_GET_GENRE_CATEGORY_STATION_COUNT:0x50,
            PNDR_GET_GENRE_STATION_NAMES:0x51,
            PNDR_EVENT_SELECT_GENRE_STATION:0x52,
            PNDR_GET_GENRE_STATION_ART:0x53,
            PNDR_EVENT_CANCEL_GENRE_STATION_ART:0x54,

            PNDR_EVENT_SEARCH_AUTO_COMPLETE:0x60,
            PNDR_EVENT_SEARCH_EXTENDED:0x61,
            PNDR_GET_SEARCH_RESULT_INFO:0x62,
            PNDR_EVENT_SEARCH_SELECT:0x63,
            PNDR_SEARCH_DISCARD:0x64,

            PNDR_EVENT_OPEN_APP:0x70,

            PNDR_GET_BRANDING_IMAGE:0x80,
            PNDR_UPDATE_STATUS:0x81,
            PNDR_RETURN_STATUS:0x82,
            PNDR_UPDATE_NOTICE:0x83,
            PNDR_RETURN_NOTICE_TEXT:0x84,
            PNDR_RETURN_LISTENER:0x85,

            PNDR_UPDATE_TRACK:0x90,
            PNDR_RETURN_TRACK_INFO:0x91,
            PNDR_RETURN_TRACK_TITLE:0x92,
            PNDR_RETURN_TRACK_ARTIST:0x93,
            PNDR_RETURN_TRACK_ALBUM:0x94,
            PNDR_RETURN_TRACK_ALBUM_ART_SEGMENT:0x95,
            PNDR_UPDATE_TRACK_ALBUM_ART:0x96,
            PNDR_UPDATE_TRACK_ELAPSED:0x97,
            PNDR_UPDATE_TRACK_RATING:0x98,
            PNDR_UPDATE_TRACK_EXPLAIN:0x99,
            PNDR_RETURN_TRACK_EXPLAIN_SEGMENT:0x9a,
            PNDR_UPDATE_TRACK_BOOKMARK_TRACK:0x9b,
            PNDR_UPDATE_TRACK_BOOKMARK_ARTIST:0x9c,
            PNDR_RETURN_TRACK_INFO_EXTENDED:0x9d,
            PNDR_UPDATE_TRACK_COMPLETED:0x9e,

            PNDR_UPDATE_STATION_ACTIVE:0xba,
            PNDR_RETURN_STATION_ACTIVE:0xb1,
            PNDR_RETURN_STATION_COUNT:0xb2,
            PNDR_RETURN_STATION_TOKENS:0xb3,
            PNDR_RETURN_STATION_INFO:0xb4,
            PNDR_RETURN_STATIONS_ORDER:0xb5,
            PNDR_UPDATE_STATIONS_ORDER:0xb6,
            PNDR_UPDATE_STATION_DELETED:0xb7,
            PNDR_RETURN_STATION_ART_SEGMENT:0xb8,

            PNDR_RETURN_GENRE_CATEGORY_COUNT:0xb9,
            PNDR_RETURN_GENRE_CATEGORY_NAMES:0xbb,
            PNDR_RETURN_GENRE_CATEGORY_STATION_COUNT:0xbc,
            PNDR_RETURN_GENRE_STATION_NAMES:0xbd,
            PNDR_RETURN_GENRE_STATION_ART_SEGMENT:0xbe,

            PNDR_UPDATE_SEARCH:0xd0,
            PNDR_RETURN_SEARCH_RESULT_INFO:0xd1,
            PNDR_UPDATE_STATION_ADDED:0xd2,

            PNDR_ECHO_REQUEST:0x7f,
            PNDR_ECHO_RESPONSE:0xff,

            PNDR_TRACK_NONE:0,
            PNDR_STATION_NONE:0
        }
    },

    notice:{
        PNDR_NOTICE_SKIP_LIMIT_REACHED:0x00,
        PNDR_NOTICE_STATION_LIMIT_REACHED:0x01,
        PNDR_NOTICE_ERROR_TRACK_RATING:0x02,
        PNDR_NOTICE_ERROR_STATION_DELETE:0x03,
        PNDR_NOTICE_ERROR_SEARCH_EXTENDED:0x04,
        PNDR_NOTICE_ERROR_SEARCH_SELECT:0x05,
        PNDR_NOTICE_ERROR_BOOKMARK:0x06,
        PNDR_NOTICE_ERROR_MAINTENANCE:0x07,
        PNDR_NOTICE_ERROR_TRACK_EXPLAIN:0x08,
        PNDR_NOTICE_ERROR_SESSION_ALREADY_STARTED:0x09,
        PNDR_NOTICE_ERROR_NO_ACTIVE_SESSION:0x0A,
        PNDR_NOTICE_ERROR_APP_URL_NOT_SUPPORTED:0x0B,
        PNDR_NOTICE_ERROR_STATION_DOES_NOT_EXIST:0x0C
    },

    status:{
        PNDR_STATUS_PLAYING:0x01,
        PNDR_STATUS_PAUSED:0x02,
        PNDR_STATUS_INCOMPATIBLE_API_VERSION:0x03,
        PNDR_STATUS_UNKNOWN_ERROR:0x04,
        PNDR_STATUS_NO_STATIONS:0x05,
        PNDR_STATUS_NO_STATION_ACTIVE:0x06,
        PNDR_STATUS_INSUFFICIENT_CONNECTIVITY:0x07,
        PNDR_STATUS_LICENSING_RESTRICTIONS:0x08,
        PNDR_STATUS_INVALID_LOGIN:0x09
    },

    track:{
        rating:{
            PNDR_RATING_NONE:0x00,
            PNDR_RATING_POSITIVE:0x01,
            PNDR_RATING_NEGATIVE:0x02
        },
        permissionflag:{
            PNDR_TRACK_FLAG_ALLOW_RATING:0X01,
            PNDR_TRACK_FLAG_ALLOW_SKIP:0X02,
            PNDR_TRACK_FLAG_ALLOW_BOOKMARK:0X04,
            PNDR_TRACK_FLAG_ALLOW_EXPLAIN:0X08,
            PNDR_TRACK_FLAG_ALLOW_CREATE_STATION_FROM:0X10
        },
        identityflag:{
            PNDR_TRACK_FLAG_IS_AUDIO_AD:0X01,
            PNDR_TRACK_FLAG_IS_TRACK_BOOKMARKED:0X02,
            PNDR_TRACK_FLAG_IS_ARTIST_BOOKMARKED:0X04
        }
    },

    sort:{
        PNDR_SORT_BY_DATE:0x00,
        PNDR_SORT_BY_NAME:0x01
    },

    station:{
        PNDR_STATION_FLAG_ALLOW_DELETE: 0x01,
        PNDR_STATION_FLAG_IS_SHARED: 0x02,
        PNDR_STATION_FLAG_IS_QUICKMIX: 0x04
    },

  getStringForCode:function(object, code) {
    for (var key in object) {
      var val = object[key];
      if (val == code) {
        return key;
      }
    }
  },

  getStringsForBitmask:function(object, bitmask) {
    var filter = 1;
    var retString = "";
    for (var i = 0; i < 32; i++) {
      if (filter & bitmask) {
        if (retString.length > 0) {
          retString += ", ";
        }
        retString += this.getStringForCode(object, filter);
      }
      filter *= 2;
    }
    return retString;
  }

};
function Parser() {
  this.handlers = new Array();
  this.handlers.push(new HandlerStart());
  this.handlers.push(new HandlerType());
  this.handlers.push(new HandlerSequence());
  this.handlers.push(new HandlerSize());
  this.handlers.push(new HandlerPayload());
  this.handlers.push(new HandlerCRC());
  this.handlers.push(new HandlerEnd());
}

/**
 * @param {Object} data the raw data
 */
Parser.prototype.parse = function(data) {
  var context = {};
  context.nextIndex = 0;
  var frame = new Frame();
  for (var i = 0; i < this.handlers.length; i++) {
    var handler = this.handlers[i];
    //short circuit the loop to avoid redundant processing if the frame is bad.
    if (context.hasError) {
      break;
    }
    handler.handle(data, context, frame);
  }
  return frame;
};

/**
 * Handler Start
 */
function HandlerStart() {

}

HandlerStart.prototype.handle = function(data, context, frame) {
  if (!data || context.hasError) {
    return;
  }

  var first = data[context.nextIndex];
  if (first == Frame.FLAG_FIRST) {
    context.nextIndex = 1;
    frame.setFirst(first);
  } else {
    context.hasError = true;
  }
};

/**
 * Handler type
 */
function HandlerType() {

}

HandlerType.prototype.handle = function(data, context, frame) {
  if (!data || context.hasError) {
    return;
  }

  var type = data[context.nextIndex];
  if (type == 0x00 || type == 0x01) {
    frame.setType(type);
    context.nextIndex = 2;
  } else {
    context.hasError = true;
  }
};

/**
 * Handler sequence
 */
function HandlerSequence() {

}

HandlerSequence.prototype.handle = function(data, context, frame) {
  if (!data || context.hasError) {
    return;
  }

  var sequence = data[context.nextIndex];
  if (sequence == 0x00 || sequence == 0x01) {
    frame.setSequence(sequence);
    context.nextIndex = 3;
  } else {
    context.hasError = true;
  }
};

/**
 * Handler size
 */
function HandlerSize() {

}

HandlerSize.prototype.handle = function(data, context, frame) {
  if (!data || context.hasError) {
    return;
  }


  //4 bytes
  var temp = new Array();
  for (var i = 0; i < 4; i++) {
    temp.push(data[context.nextIndex+i]);
  }

  var size = Conversion.intFromBytes(temp);

  frame.setSize(size);
  context.payloadSize = size;
  context.nextIndex = context.nextIndex + 4 /*4 bytes*/;
};


/**
 * Handler payload
 */
function HandlerPayload() {

}

HandlerPayload.prototype.handle = function(data, context, frame) {
  if (!data || context.hasError) {
    return;
  }
  var payloadSize = context.payloadSize;

  //not ack and empty payload is bad data.
  if (!payloadSize && !frame.isAck()) {
    context.hasError = true;
    return;
  }

  var payload = new Array();
  if (payloadSize) {
    payload = data.slice(context.nextIndex, context.nextIndex + payloadSize);
    context.nextIndex = context.nextIndex + payloadSize;
  }
    frame.setPayload(Payload.factory(payload));
};

/**
 * Handler crc
 */
function HandlerCRC() {

}

HandlerCRC.prototype.handle = function(data, context, frame) {
  if (context.hasError) {
    return;
  }
  var start = context.nextIndex;
  //2 bytes
  var tmp = new Array();
  tmp.push(data[start]);
  tmp.push(data[start+1]);

  var crc = Conversion.intFromBytes(tmp);

  frame.setCrc(crc);
  context.nextIndex += 2;
};

/**
 * Handler crc
 */
function HandlerEnd() {

}

HandlerEnd.prototype.handle = function(data, context, frame) {
  if (!data || context.hasError) {
    return;
  }

  var index = context.nextIndex;
  var last = data[index];
  if (last != Frame.FLAG_LAST) {
    context.hasError = true;
    return;
  }
  frame.setLast(last);
};


/**
 * @constructor
 * @param {Object=} first
 * @param {Object=} type
 * @param {Object=} sequence
 * @param {Object=} size
 * @param {Payload=} payload
 * @param {Object=} crc
 * @param {Object=} last
 */
function Frame(first, type, sequence, size, payload, crc, last) {
    this.first = null;
    this.type = 0;
    this.sequence = 0;
    this.size = null;
    this.payload = null;
    this.crc = null;
    this.last = null;

    if (first) {
        this.first = first;
    }

    if (type) {
        this.type = type;
    }
    if (sequence) {
        this.sequence = sequence;
    }
    if (size) {
        this.size = size;
    }
    if (payload) {
        this.payload = payload;
    }
    if (crc) {
        this.crc = crc;
    }
    if (last) {
        this.last = last;
    }
}

/*
 * STATIC FRAMES
 */
/**
 * generate an ACK frame with the given sequence
 * @param {Frame} frame
 */
Frame.ACK = function (frame) {
    return new Frame(Frame.FLAG_FIRST, Frame.FLAG_TYPE_ACK, frame.getSequence(), 0, new Payload([]), -1, Frame.FLAG_LAST);
};

/**
 * helper to create a frame
 * @param {Payload} payload
 * @return {Frame}
 */
Frame.DATA = function (payload) {
    return new Frame(Frame.FLAG_FIRST, Frame.FLAG_TYPE_DATA, -1, payload.toByte().length, payload, -1, Frame.FLAG_LAST);
};

/*
 * Constants
 */
Frame.FLAG_FIRST = 0x7E;
Frame.FLAG_FIRST_ESCAPE = 0x5E;

Frame.FLAG_LAST = 0x7C;
Frame.FLAG_LAST_ESCAPE = 0x5C;

Frame.FLAG_ESCAPE = 0x7D;
Frame.FLAG_ESCAPE_ESCAPE = 0x5D;

Frame.FLAG_TYPE_ACK = 0x01;
Frame.FLAG_TYPE_DATA = 0x00;

Frame.prototype.isAck = function () {
    return this.getType() == Frame.FLAG_TYPE_ACK;
};

Frame.prototype.isValid = function () {
    return this.getLast() == Frame.FLAG_LAST;
};

Frame.prototype.toByte = function (useCrc) {
    var result = new Array();

    result.push(this.getFirst());
    result.push(this.getType());
    result.push(this.getSequence());

  var dataBytes = [];
  if (this.getPayload()) {
    dataBytes = this.getPayload().toByte();
    if (!dataBytes || dataBytes.length == 0) {
      dataBytes = [];
    }
    }


  var size = this.getSize();
    //4 bytes for the size,
  if (dataBytes.length != this.getSize()/*the payload size has been modified, we need to modify the size.*/) {
    size = dataBytes.length;
  }

  //append the size
  result = result.concat(Conversion.bytesFromInt(size, 4));
  //append the payload
  result = result.concat(dataBytes);


    if (useCrc) {
        //2 bytes for crc
        result = result.concat(Conversion.bytesFromInt(this.getCrc(), 2));
    } else {
        var temp = new Array();

        temp.push(this.getType());
        temp.push(this.getSequence());
        temp = temp.concat(Conversion.bytesFromInt(size, 4));
        temp = temp.concat(dataBytes);
        result = result.concat(Conversion.bytesFromInt(Conversion.crc(temp), 2));
    }
    result.push(this.getLast());
    return EscapeTool.escape(result);
};

Frame.prototype.setFirst = function (first) {
    this.first = first;
};

Frame.prototype.getFirst = function () {
    return this.first;
};

Frame.prototype.setLast = function (last) {
    this.last = last;
};

Frame.prototype.getLast = function () {
    return this.last;
};

Frame.prototype.setType = function (type) {
    this.type = type;
};

Frame.prototype.getType = function () {
    return this.type;
};

Frame.prototype.setSize = function (size) {
    this.size = size;
};

Frame.prototype.getSize = function () {
    return this.size;
};

Frame.prototype.setCrc = function (crc) {
    this.crc = crc;
};

Frame.prototype.getCrc = function () {
    return this.crc;
};

Frame.prototype.setSequence = function (sequence) {
    this.sequence = sequence;
};

Frame.prototype.getSequence = function () {
    return this.sequence;
};

Frame.prototype.setPayload = function (payload) {
    this.payload = payload;
};

Frame.prototype.getPayload = function () {
    return this.payload;
};
/**
 * pandoralink escape and unescape logic.
 */
var EscapeTool = (function() {
  return {
    /**
     *
     * @param {Array} data
     */
    escape: function(data) {

      var encounteredStart = false;

      var escaped = new Array();
      for (var i = 0; i < data.length; i++) {
        var b = data[i];
        //encounter escaped char, escape it
        if (b == Frame.FLAG_ESCAPE) {
          escaped.push(Frame.FLAG_ESCAPE);
          escaped.push(Frame.FLAG_ESCAPE_ESCAPE);
          continue;
        }

        //enounter the first again, escape it
        if (encounteredStart && b == Frame.FLAG_FIRST) {
          escaped.push(Frame.FLAG_ESCAPE);
          escaped.push(Frame.FLAG_FIRST_ESCAPE);
          continue;
        }

        //encounter last, but not last yet.
        if (b == Frame.FLAG_LAST && i + 1 < data.length) {
          escaped.push(Frame.FLAG_ESCAPE);
          escaped.push(Frame.FLAG_LAST_ESCAPE);
          continue;
        }

        escaped.push(b);

        if (b == Frame.FLAG_FIRST && !encounteredStart) {
          encounteredStart = true;
        }
      }
      return escaped;
    },
    /**
     * @param {Array} data
     */
    unescape: function(data) {
      var encounteredEscape = false;
      var unescaped = new Array();
      for (var i = 0; i < data.length; i++) {
        var b = data[i];
        if (b == Frame.FLAG_ESCAPE) {
          encounteredEscape = true;
          continue;
        }
        if (encounteredEscape) {
          if (b == Frame.FLAG_FIRST_ESCAPE) {
            unescaped.push(Frame.FLAG_FIRST);
          } else if (b == Frame.FLAG_ESCAPE_ESCAPE) {
            unescaped.push(Frame.FLAG_ESCAPE)
          } else if (b == Frame.FLAG_LAST_ESCAPE) {
            unescaped.push(Frame.FLAG_LAST);
          }
          encounteredEscape = false;
          continue;
        }
        unescaped.push(b);
      }

      return unescaped;
    }
  };
})();


var FrameHandler = {};

FrameHandler.setCallback = function(callbackObj) {
  FrameHandler.callbackObj = callbackObj;
};

FrameHandler.onFrame = function(data) {
  if (FrameHandler.callbackObj == null) {
    console.log("No callback object has been registered!");
    return;
  }
  var parser = new Parser();
  var frame = parser.parse(EscapeTool.unescape(data));
  var payload = frame.getPayload();
  switch(payload.getCommand()) {
        case resources.command.toCode.PNDR_SESSION_START:
            this.callCallback(FrameHandler.callbackObj.onSessionStart, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_BRANDING_IMAGE:
            this.callCallback(FrameHandler.callbackObj.onUpdateBrandingImage, payload);
      break;
    case resources.command.toCode.PNDR_RETURN_BRANDING_IMAGE_SEGMENT:
            this.callCallback(FrameHandler.callbackObj.onReturnBrandingImageSegment, payload);
      break;
    case resources.command.toCode.PNDR_GET_STATUS:
            this.callCallback(FrameHandler.callbackObj.onGetStatus, payload);
      break;
        case resources.command.toCode.PNDR_SESSION_TERMINATE:
            this.callCallback(FrameHandler.callbackObj.onSessionTerminate, payload);
      break;
    case resources.command.toCode.PNDR_GET_LISTENER:
            this.callCallback(FrameHandler.callbackObj.onGetListener, payload);
      break;
    case resources.command.toCode.PNDR_GET_TRACK_INFO:
            this.callCallback(FrameHandler.callbackObj.onGetTrackInfo, payload);
      break;
    case resources.command.toCode.PNDR_GET_TRACK_TITLE:
            this.callCallback(FrameHandler.callbackObj.onGetTrackTitle, payload);
      break;
    case resources.command.toCode.PNDR_GET_TRACK_ARTIST:
            this.callCallback(FrameHandler.callbackObj.onGetTrackArtist, payload);
      break;
    case resources.command.toCode.PNDR_GET_TRACK_ALBUM:
            this.callCallback(FrameHandler.callbackObj.onGetTrackAlbum, payload);
      break;
    case resources.command.toCode.PNDR_GET_TRACK_ALBUM_ART:
            this.callCallback(FrameHandler.callbackObj.onGetTrackAlbumArt, payload);
      break;
    case resources.command.toCode.PNDR_SET_TRACK_ELAPSED_POLLING:
            this.callCallback(FrameHandler.callbackObj.onSetTrackElapsedPolling, payload);
      break;
    case resources.command.toCode.PNDR_GET_TRACK_INFO_EXTENDED:
            this.callCallback(FrameHandler.callbackObj.onGetTrackInfoExtended, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_TRACK_PLAY:
      this.callCallback(FrameHandler.callbackObj.onEventTrackPlay, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_TRACK_PAUSE:
      this.callCallback(FrameHandler.callbackObj.onEventTrackPause, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_TRACK_SKIP:
      this.callCallback(FrameHandler.callbackObj.onEventTrackSkip, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_TRACK_RATE_POSITIVE:
      this.callCallback(FrameHandler.callbackObj.onEventTrackRatePositive, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_TRACK_RATE_NEGATIVE:
      this.callCallback(FrameHandler.callbackObj.onEventTrackRateNegative, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_TRACK_EXPLAIN:
      this.callCallback(FrameHandler.callbackObj.onEventTrackExplain, payload);
      break;
    case resources.command.toCode.PNDR_GET_TRACK_EXPLAIN:
      this.callCallback(FrameHandler.callbackObj.onGetTrackExplain, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_TRACK_BOOKMARK_TRACK:
      this.callCallback(FrameHandler.callbackObj.onEventTrackBookmarkTrack, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_TRACK_BOOKMARK_ARTIST:
      this.callCallback(FrameHandler.callbackObj.onEventTrackBookmarkArtist, payload);
      break;
    case resources.command.toCode.PNDR_GET_STATION_ACTIVE:
      this.callCallback(FrameHandler.callbackObj.onGetStationActive, payload);
      break;
    case resources.command.toCode.PNDR_GET_STATION_COUNT:
      this.callCallback(FrameHandler.callbackObj.onGetStationCount, payload);
      break;
    case resources.command.toCode.PNDR_GET_STATION_TOKENS:
      this.callCallback(FrameHandler.callbackObj.onGetStationTokens, payload);
      break;
    case resources.command.toCode.PNDR_GET_ALL_STATION_TOKENS:
      this.callCallback(FrameHandler.callbackObj.onGetAllStationTokens, payload);
      break;
    case resources.command.toCode.PNDR_GET_STATION_INFO:
      this.callCallback(FrameHandler.callbackObj.onGetStationInfo, payload);
      break;
    case resources.command.toCode.PNDR_GET_STATIONS_ORDER:
      this.callCallback(FrameHandler.callbackObj.onGetStationsOrder, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_STATIONS_SORT:
      this.callCallback(FrameHandler.callbackObj.onEventStationsSort, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_STATION_SELECT:
      this.callCallback(FrameHandler.callbackObj.onEventStationSelect, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_STATION_DELETE:
          this.callCallback(FrameHandler.callbackObj.onEventStationDelete, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_STATION_CREATE_FROM_CURRENT_ARTIST:
          this.callCallback(FrameHandler.callbackObj.onEventStationCreateFromCurrentArtist, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_STATION_CREATE_FROM_CURRENT_TRACK:
          this.callCallback(FrameHandler.callbackObj.onEventStationCreateFromCurrentTrack, payload);
      break;
    case resources.command.toCode.PNDR_GET_STATION_ART:
          this.callCallback(FrameHandler.callbackObj.onGetStationArt, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_CANCEL_STATION_ART:
          this.callCallback(FrameHandler.callbackObj.onEventCancelStationArt, payload);
      break;
    case resources.command.toCode.PNDR_GET_GENRE_CATEGORY_COUNT:
          this.callCallback(FrameHandler.callbackObj.onGetGenreCategoryCount, payload);
      break;
    case resources.command.toCode.PNDR_GET_GENRE_CATEGORY_NAMES:
          this.callCallback(FrameHandler.callbackObj.onGetGenreCategoryNames, payload);
      break;
    case resources.command.toCode.PNDR_GET_ALL_GENRE_CATEGORY_NAMES:
          this.callCallback(FrameHandler.callbackObj.onGetAllGenreCategoryNames, payload);
      break;
    case resources.command.toCode.PNDR_GET_GENRE_CATEGORY_STATION_COUNT:
          this.callCallback(FrameHandler.callbackObj.onGetGenreCategoryStationCount, payload);
      break;
    case resources.command.toCode.PNDR_GET_GENRE_STATION_NAMES:
          this.callCallback(FrameHandler.callbackObj.onGetGenreStationNames, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_SELECT_GENRE_STATION:
          this.callCallback(FrameHandler.callbackObj.onEventSelectGenreStation, payload);
      break;
    case resources.command.toCode.PNDR_GET_GENRE_STATION_ART:
          this.callCallback(FrameHandler.callbackObj.onGetGenreStationArt, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_CANCEL_GENRE_STATION_ART:
          this.callCallback(FrameHandler.callbackObj.onEventCancelGenreStationArt, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_SEARCH_AUTO_COMPLETE:
          this.callCallback(FrameHandler.callbackObj.onEventSearchAutoComplete, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_SEARCH_EXTENDED:
          this.callCallback(FrameHandler.callbackObj.onEventSearchExtended, payload);
      break;
    case resources.command.toCode.PNDR_GET_SEARCH_RESULT_INFO:
          this.callCallback(FrameHandler.callbackObj.onGetSearchResultInfo, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_SEARCH_SELECT:
          this.callCallback(FrameHandler.callbackObj.onEventSearchSelect, payload);
      break;
    case resources.command.toCode.PNDR_EVENT_OPEN_APP:
      this.callCallback(FrameHandler.callbackObj.onEventOpenApp, payload);
      break;

    case resources.command.toCode.PNDR_GET_BRANDING_IMAGE:
          this.callCallback(FrameHandler.callbackObj.onGetBrandingImage, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_STATUS:
        this.callCallback(FrameHandler.callbackObj.onUpdateStatus, payload);
      break;
    case resources.command.toCode.PNDR_RETURN_STATUS:
        this.callCallback(FrameHandler.callbackObj.onReturnStatus, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_NOTICE:
        this.callCallback(FrameHandler.callbackObj.onUpdateNotice, payload);
      break;
    case resources.command.toCode.PNDR_RETURN_LISTENER:
        this.callCallback(FrameHandler.callbackObj.onReturnListener, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_TRACK:
      this.callCallback(FrameHandler.callbackObj.onUpdateTrack, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_TRACK_INFO:
            this.callCallback(FrameHandler.callbackObj.onReturnTrackInfo, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_TRACK_TITLE:
            this.callCallback(FrameHandler.callbackObj.onReturnTrackTitle, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_TRACK_ARTIST:
            this.callCallback(FrameHandler.callbackObj.onReturnTrackArtist, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_TRACK_ALBUM:
            this.callCallback(FrameHandler.callbackObj.onReturnTrackAlbum, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_TRACK_ALBUM_ART_SEGMENT:
            this.callCallback(FrameHandler.callbackObj.onReturnTrackAlbumArtSegment, payload);
      break;
        case resources.command.toCode.PNDR_UPDATE_TRACK_ALBUM_ART:
            this.callCallback(FrameHandler.callbackObj.onUpdateTrackAlbumArt, payload);
      break;
        case resources.command.toCode.PNDR_UPDATE_TRACK_ELAPSED:
            this.callCallback(FrameHandler.callbackObj.onUpdateTrackElapsed, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_TRACK_RATING:
      this.callCallback(FrameHandler.callbackObj.onUpdateTrackRating, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_TRACK_EXPLAIN:
      this.callCallback(FrameHandler.callbackObj.onUpdateTrackExplain, payload);
      break;
    case resources.command.toCode.PNDR_RETURN_TRACK_EXPLAIN_SEGMENT:
      this.callCallback(FrameHandler.callbackObj.onReturnTrackExplainSegment, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_TRACK_BOOKMARK_TRACK:
      this.callCallback(FrameHandler.callbackObj.onUpdateTrackBookmarkTrack, payload);
            break;
    case resources.command.toCode.PNDR_UPDATE_TRACK_BOOKMARK_ARTIST:
      this.callCallback(FrameHandler.callbackObj.onUpdateTrackBookmarkArtist, payload);
            break;
        case resources.command.toCode.PNDR_RETURN_TRACK_INFO_EXTENDED:
            this.callCallback(FrameHandler.callbackObj.onReturnTrackInfoExtended, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_TRACK_COMPLETED:
      this.callCallback(FrameHandler.callbackObj.onUpdateTrackCompleted, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_STATION_ACTIVE:
      this.callCallback(FrameHandler.callbackObj.onUpdateStationActive, payload);
      break;
    case resources.command.toCode.PNDR_RETURN_STATION_ACTIVE:
      this.callCallback(FrameHandler.callbackObj.onReturnStationActive, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_STATION_COUNT:
            this.callCallback(FrameHandler.callbackObj.onReturnStationCount, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_STATION_TOKENS:
            this.callCallback(FrameHandler.callbackObj.onReturnStationTokens, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_STATION_INFO:
            this.callCallback(FrameHandler.callbackObj.onReturnStationInfo, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_STATIONS_ORDER:
            this.callCallback(FrameHandler.callbackObj.onReturnStationsOrder, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_STATIONS_ORDER:
      this.callCallback(FrameHandler.callbackObj.onUpdateStationsOrder, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_STATION_DELETED:
      this.callCallback(FrameHandler.callbackObj.onUpdateStationDeleted, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_STATION_ART_SEGMENT:
            this.callCallback(FrameHandler.callbackObj.onReturnStationArtSegment, payload);
      break;
    case resources.command.toCode.PNDR_RETURN_GENRE_CATEGORY_COUNT:
      this.callCallback(FrameHandler.callbackObj.onReturnGenreCategoryCount, payload);
      break;
    case resources.command.toCode.PNDR_RETURN_GENRE_CATEGORY_NAMES:
      this.callCallback(FrameHandler.callbackObj.onReturnGenreCategoryNames, payload);
      break;
    case resources.command.toCode.PNDR_RETURN_GENRE_CATEGORY_STATION_COUNT:
      this.callCallback(FrameHandler.callbackObj.onReturnGenreCategoryStationCount, payload);
      break;
    case resources.command.toCode.PNDR_RETURN_GENRE_STATION_NAMES:
      this.callCallback(FrameHandler.callbackObj.onReturnGenreStationNames, payload);
      break;
        case resources.command.toCode.PNDR_RETURN_GENRE_STATION_ART_SEGMENT:
            this.callCallback(FrameHandler.callbackObj.onReturnGenreStationArtSegment, payload);
      break;
    case resources.command.toCode.PNDR_UPDATE_SEARCH:
      this.callCallback(FrameHandler.callbackObj.onUpdateSearch, payload);
      break;
    case resources.command.toCode.PNDR_RETURN_SEARCH_RESULT_INFO:
      this.callCallback(FrameHandler.callbackObj.onReturnSearchResultInfo, payload);
      break;
    default:
      break;
  }

};

FrameHandler.callCallback = function(callback, payload) {
  if (typeof callback == "function") {
    callback(payload);
  } else {
    console.log("No callback found for " + payload.getCommandName());
  }
};

FrameHandler.frameBytesForPayload = function(payload) {
  payload.markAsDirty();
  var frame = Frame.DATA(payload);
  frame.setSequence(0);
  return frame.toByte();
};
/**
 *
 * @type {*}
 */
var Payload = Class.extend({
    init:function (data) {
        this.command = resources.command.fromCode.UNKNOWN;
        this.raw = data;
    this.dirty = false;
        if (data && data.length > 0) {
            this.parse(data);
        }

    //reflectively override the setters to mark the dirty flag.
    var me = this;
    var methods = [];
    for (var m in me) {
      var o = me[m];
      if (typeof(o) == "function" && m.indexOf("set") == 0) {
        methods.push({name: m, method: o, source:me});
      }
    }

    for (var i = 0; i < methods.length; i++) {
      var result = methods[i];

      me[result.name] = function(result) {
        return function() {
          result.source.dirty = true;
          return result.method.apply(result.source, arguments);
        };
      }(result);
    }
  },

  markAsDirty: function() {
    this.dirty = true;
  },

  parse:function (data) {
    var command = data[0];
        this.raw = data;
        this.command = command;
        this.commandName = resources.command.fromCode[command];
        return this;
    },
    getCommand:function () {
        return this.command;
    },
    setCommand:function (command) {
        this.command = command;
    },
    getCommandName:function () {
        return this.commandName;
    },

    //this is being used in Engine, so we have to put it here. Subclass will override this
    getTrackToken:function () {
        return null;
    },

  //go through all of the getters and build up a json data.
  toJSON: function() {
    var me = this;
    var methods = [];
    for (var m in me) {
      var o = me[m];
      if (typeof(o) == "function" && m.indexOf("get") == 0) {
        methods.push({name: m, method: o, source:me});
      }
    }

    var data = {};
    for (var i = 0; i < methods.length; i++) {
      var result = methods[i];
      data[result.name.slice(3)] = result.method.apply(result.source,null);
    }

    return data;
  },

    //return payload as a string
    //TODO implement other bases, only supports hex right now
    toString:function (base) {
        var payloadStr = "";
        var payloadBytes = this.toByte();
    for (var i = 0; i < payloadBytes.length; i += 1) {
        if(base == 16) {
        payloadStr = payloadStr + payloadBytes[i].toString(16) + " ";
      }
    }
    return payloadStr;
  },

    //reconstruct the byte array.
    toByte:function () {
        if(this.dirty){
            return this.buildBytes();
        }
        if (this.raw && this.getCommand() != resources.command.fromCode.UNKNOWN) {
            this.raw[0] = this.getCommand();
            return this.raw;
        }
        return [];
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.getCommand());
        return result;
    }
});

Payload.factory = function (data) {
    var payload = new Payload(data);
    var command = payload.getCommand();
    switch (command) {
        case resources.command.toCode.PNDR_SESSION_START:
            return new SessionStartPayload(data);
    case resources.command.toCode.PNDR_UPDATE_BRANDING_IMAGE:
            return new UpdateBrandingImagePayload(data);
    case resources.command.toCode.PNDR_RETURN_BRANDING_IMAGE_SEGMENT:
            return new ReturnBrandingImageSegmentPayload(data);
    case resources.command.toCode.PNDR_GET_STATUS:
            return new GetStatusPayload(data);
        case resources.command.toCode.PNDR_SESSION_TERMINATE:
            return new SessionTerminatePayload(data);
    case resources.command.toCode.PNDR_GET_LISTENER:
            return new GetListenerPayload(data);
    case resources.command.toCode.PNDR_GET_TRACK_INFO:
            return new GetTrackInfoPayload(data);
    case resources.command.toCode.PNDR_GET_TRACK_TITLE:
            return new GetTrackTitlePayload(data);
    case resources.command.toCode.PNDR_GET_TRACK_ARTIST:
            return new GetTrackArtistPayload(data);
    case resources.command.toCode.PNDR_GET_TRACK_ALBUM:
            return new GetTrackAlbumPayload(data);
    case resources.command.toCode.PNDR_GET_TRACK_ALBUM_ART:
            return new GetTrackAlbumArtPayload(data);
    case resources.command.toCode.PNDR_SET_TRACK_ELAPSED_POLLING:
            return new SetTrackElapsedPollingPayload(data);
    case resources.command.toCode.PNDR_GET_TRACK_INFO_EXTENDED:
            return new GetTrackInfoExtendedPayload(data);
    case resources.command.toCode.PNDR_EVENT_TRACK_PLAY:
      return new EventTrackPlayPayload(data);
    case resources.command.toCode.PNDR_EVENT_TRACK_PAUSE:
      return new EventTrackPausePayload(data);
    case resources.command.toCode.PNDR_EVENT_TRACK_SKIP:
      return new EventTrackSkipPayload(data);
    case resources.command.toCode.PNDR_EVENT_TRACK_RATE_POSITIVE:
      return new EventTrackRatePositivePayload(data);
    case resources.command.toCode.PNDR_EVENT_TRACK_RATE_NEGATIVE:
      return new EventTrackRateNegativePayload(data);
    case resources.command.toCode.PNDR_EVENT_TRACK_EXPLAIN:
      return new EventTrackExplainPayload(data);
    case resources.command.toCode.PNDR_GET_TRACK_EXPLAIN:
      return new GetTrackExplainPayload(data);
    case resources.command.toCode.PNDR_EVENT_TRACK_BOOKMARK_TRACK:
      return new EventTrackBookmarkTrackPayload(data);
    case resources.command.toCode.PNDR_EVENT_TRACK_BOOKMARK_ARTIST:
      return new EventTrackBookmarkArtistPayload(data);
    case resources.command.toCode.PNDR_GET_STATION_ACTIVE:
      return new GetStationActivePayload(data);
    case resources.command.toCode.PNDR_GET_STATION_COUNT:
      return new GetStationCountPayload(data);
    case resources.command.toCode.PNDR_GET_STATION_TOKENS:
      return new GetStationTokensPayload(data);
    case resources.command.toCode.PNDR_GET_ALL_STATION_TOKENS:
      return new GetAllStationTokensPayload(data);
    case resources.command.toCode.PNDR_GET_STATION_INFO:
      return new GetStationInfoPayload(data);
    case resources.command.toCode.PNDR_GET_STATIONS_ORDER:
      return new GetStationsOrderPayload(data);
    case resources.command.toCode.PNDR_EVENT_STATIONS_SORT:
      return new EventStationsSortPayload(data);
    case resources.command.toCode.PNDR_EVENT_STATION_SELECT:
      return new EventStationSelectPayload(data);
    case resources.command.toCode.PNDR_EVENT_STATION_DELETE:
          return new EventStationDeletePayload(data);
    case resources.command.toCode.PNDR_EVENT_STATION_CREATE_FROM_CURRENT_ARTIST:
          return new EventStationCreateFromCurrentArtistPayload(data);
    case resources.command.toCode.PNDR_EVENT_STATION_CREATE_FROM_CURRENT_TRACK:
          return new EventStationCreateFromCurrentTrackPayload(data);
    case resources.command.toCode.PNDR_GET_STATION_ART:
          return new GetStationArtPayload(data);
    case resources.command.toCode.PNDR_EVENT_CANCEL_STATION_ART:
          return new EventCancelStationArtPayload(data);
    case resources.command.toCode.PNDR_GET_GENRE_CATEGORY_COUNT:
          return new GetGenreCategoryCountPayload(data);
    case resources.command.toCode.PNDR_GET_GENRE_CATEGORY_NAMES:
          return new GetGenreCategoryNamesPayload(data);
    case resources.command.toCode.PNDR_GET_ALL_GENRE_CATEGORY_NAMES:
          return new GetAllGenreCategoryNamesPayload(data);
    case resources.command.toCode.PNDR_GET_GENRE_CATEGORY_STATION_COUNT:
          return new GetGenreCategoryStationCountPayload(data);
    case resources.command.toCode.PNDR_GET_GENRE_STATION_NAMES:
          return new GetGenreStationNamesPayload(data);
    case resources.command.toCode.PNDR_EVENT_SELECT_GENRE_STATION:
          return new EventSelectGenreStationPayload(data);
    case resources.command.toCode.PNDR_GET_GENRE_STATION_ART:
          return new GetGenreStationArtPayload(data);
    case resources.command.toCode.PNDR_EVENT_CANCEL_GENRE_STATION_ART:
          return new EventCancelGenreStationArtPayload(data);
    case resources.command.toCode.PNDR_EVENT_SEARCH_AUTO_COMPLETE:
          return new EventSearchAutoCompletePayload(data);
    case resources.command.toCode.PNDR_EVENT_SEARCH_EXTENDED:
          return new EventSearchExtendedPayload(data);
    case resources.command.toCode.PNDR_GET_SEARCH_RESULT_INFO:
          return new GetSearchResultInfoPayload(data);
    case resources.command.toCode.PNDR_EVENT_SEARCH_SELECT:
          return new EventSearchSelectPayload(data);
    case resources.command.toCode.PNDR_EVENT_OPEN_APP:
      return new EventOpenAppPayload(data);

    case resources.command.toCode.PNDR_GET_BRANDING_IMAGE:
          return new GetBrandingImagePayload(data);
    case resources.command.toCode.PNDR_UPDATE_STATUS:
        return new UpdateStatusPayload(data);
    case resources.command.toCode.PNDR_RETURN_STATUS:
        return new ReturnStatusPayload(data);
    case resources.command.toCode.PNDR_UPDATE_NOTICE:
        return new UpdateNoticePayload(data);
    case resources.command.toCode.PNDR_RETURN_LISTENER:
        return new ReturnListenerPayload(data);
    case resources.command.toCode.PNDR_UPDATE_TRACK:
      return new UpdateTrackPayload(data);
        case resources.command.toCode.PNDR_RETURN_TRACK_INFO:
            return new ReturnTrackInfoPayload(data);
        case resources.command.toCode.PNDR_RETURN_TRACK_TITLE:
            return new ReturnTrackTitlePayload(data);
        case resources.command.toCode.PNDR_RETURN_TRACK_ARTIST:
            return new ReturnTrackArtistPayload(data);
        case resources.command.toCode.PNDR_RETURN_TRACK_ALBUM:
            return new ReturnTrackAlbumPayload(data);
        case resources.command.toCode.PNDR_RETURN_TRACK_ALBUM_ART_SEGMENT:
            return new ReturnTrackAlbumArtSegmentPayload(data);
        case resources.command.toCode.PNDR_UPDATE_TRACK_ALBUM_ART:
            return new UpdateTrackAlbumArtPayload(data);
        case resources.command.toCode.PNDR_UPDATE_TRACK_ELAPSED:
            return new UpdateTrackElapsedPayload(data);
    case resources.command.toCode.PNDR_UPDATE_TRACK_RATING:
      return new UpdateTrackRatingPayload(data);
    case resources.command.toCode.PNDR_UPDATE_TRACK_EXPLAIN:
      return new UpdateTrackExplainPayload(data);
    case resources.command.toCode.PNDR_RETURN_TRACK_EXPLAIN_SEGMENT:
      return new ReturnTrackExplainSegmentPayload(data);
    case resources.command.toCode.PNDR_UPDATE_TRACK_BOOKMARK_TRACK:
      return new UpdateTrackBookmarkTrackPayload(data);
    case resources.command.toCode.PNDR_UPDATE_TRACK_BOOKMARK_ARTIST:
      return new UpdateTrackBookmarkArtistPayload(data);
        case resources.command.toCode.PNDR_RETURN_TRACK_INFO_EXTENDED:
            return new ReturnTrackInfoExtendedPayload(data);
    case resources.command.toCode.PNDR_UPDATE_TRACK_COMPLETED:
      return new UpdateTrackCompletedPayload(data);
    case resources.command.toCode.PNDR_UPDATE_STATION_ACTIVE:
      return new UpdateStationActivePayload(data);
    case resources.command.toCode.PNDR_RETURN_STATION_ACTIVE:
      return new ReturnStationActivePayload(data);
        case resources.command.toCode.PNDR_RETURN_STATION_COUNT:
            return new ReturnStationCountPayload(data);
        case resources.command.toCode.PNDR_RETURN_STATION_TOKENS:
            return new ReturnStationTokensPayload(data);
        case resources.command.toCode.PNDR_RETURN_STATION_INFO:
            return new ReturnStationInfoPayload(data);
        case resources.command.toCode.PNDR_RETURN_STATIONS_ORDER:
            return new ReturnStationsOrderPayload(data);
    case resources.command.toCode.PNDR_UPDATE_STATIONS_ORDER:
      return new UpdateStationsOrderPayload(data);
    case resources.command.toCode.PNDR_UPDATE_STATION_DELETED:
      return new UpdateStationDeletedPayload(data);
        case resources.command.toCode.PNDR_RETURN_STATION_ART_SEGMENT:
            return new ReturnStationArtSegmentPayload(data);
    case resources.command.toCode.PNDR_RETURN_GENRE_CATEGORY_COUNT:
      return new ReturnGenreCategoryCountPayload(data);
    case resources.command.toCode.PNDR_RETURN_GENRE_CATEGORY_NAMES:
      return new ReturnGenreCategoryNamesPayload(data);
    case resources.command.toCode.PNDR_RETURN_GENRE_CATEGORY_STATION_COUNT:
      return new ReturnGenreCategoryStationCountPayload(data);
    case resources.command.toCode.PNDR_RETURN_GENRE_STATION_NAMES:
      return new ReturnGenreStationNamesPayload(data);
        case resources.command.toCode.PNDR_RETURN_GENRE_STATION_ART_SEGMENT:
            return new ReturnGenreStationArtSegmentPayload(data);
    case resources.command.toCode.PNDR_UPDATE_SEARCH:
      return new UpdateSearchPayload(data);
    case resources.command.toCode.PNDR_RETURN_SEARCH_RESULT_INFO:
      return new ReturnSearchResultInfoPayload(data);
    case resources.command.toCode.PNDR_UPDATE_STATION_ADDED:
      return new UpdateStationAddedPayload(data);
    }
    return payload;
};
var ReturnTrackInfoPayload = Payload.extend({

  init:function (data) {
    this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_TRACK_INFO);
    this.version = 1;
    this.trackToken = null;
    this.albumArtLength = null;
    this.duration = null;
    this.elapsed = null;
    this.rating = null;
    this.clearAlbumArtLength = false;

    this.permissionFlags = 31;
    this.identityFlags = 0;
  },

  parse:function (data) {
    this._super(data);
    this.version = 1;
    this.trackToken = null;
    this.albumArtLength = null;
    this.duration = null;
    this.elapsed = null;
    this.rating = null;
    this.clearAlbumArtLength = false;

    this.rating = data[13];

    this.permissionFlags = 31;
    this.identityFlags = 0;

    if (data.length > 15) {
      this.version = 3;
      this.permissionFlags = data[14];
      this.identityFlags = data[15];
    }

    return this;
  },

  getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
    return this.trackToken;
  },

  setTrackToken:function (token) {
    this.trackToken = token;
  },

  getAlbumArtLength:function () {
    if (this.clearAlbumArtLength){
      return 0;
    }
    if (this.albumArtLength == null) {
      //album art length
      var albumArtLengthBytes = this.raw.slice(5, 9);
      this.albumArtLength = Conversion.intFromBytes(albumArtLengthBytes);
    }
    return this.albumArtLength;
  },

  setAlbumArtLength:function (length) {
    this.albumArtLength = length;
  },

  setClearAlbumArtLength: function(flag){
      this.clearAlbumArtLength = flag;
  },

  getDuration:function () {
    if (this.duration == null) {
      var durationBytes = this.raw.slice(9, 11);
      this.duration = Conversion.intFromBytes(durationBytes);
    }
    return this.duration;
  },

  setDuration:function (duration) {
    this.duration = duration;
  },

  getElapsed:function () {
    if (this.elapsed == null) {
      var elapsedBytes = this.raw.slice(11, 13);
      this.elapsed = Conversion.intFromBytes(elapsedBytes);
    }
    return this.elapsed;
  },

  setElapsed:function (elapsed) {
    this.elapsed = elapsed;
  },

  getRating:function () {
    return this.rating;
  },

  setRating:function (rating) {
    this.rating = rating;
  },

  getRatingString:function() {
    return resources.getStringForCode(resources.track.rating, this.rating);
  },

  getPermissionFlags:function () {
    return this.permissionFlags;
  },

  setPermissionFlags:function(flags) {
    if (flags.length == 0) {
      return;
    }
    this.permissionFlags = flags[0];
    for (var j = 1; j < flags.length; j++) {
      this.permissionFlags = this.permissionFlags + flags[j];
    }
  },

  getPermissionStrings:function() {
    return resources.getStringsForBitmask(resources.track.permissionflag, this.permissionFlags);
  },

  getIdentityFlags:function () {
    return this.identityFlags;
  },

  setIdentityFlags:function(flags) {
    if (flags.length == 0) {
      return;
    }
    this.identityFlags = flags[0];
    for (var j = 1; j < flags.length; j++) {
      this.identityFlags = this.identityFlags + flags[j];
    }
  },

  getIdentityStrings:function() {
    return resources.getStringsForBitmask(resources.track.identityflag, this.identityFlags);
  },

  getAllowSkip:function() {
    return this.permissionFlags & resources.track.permissionflag.PNDR_TRACK_FLAG_ALLOW_SKIP;
  },

  getIsAudioAd:function() {
    if (this.version < 3){
      return 0;
    }
    return this.identityFlags & resources.track.identityflag.PNDR_TRACK_FLAG_IS_AUDIO_AD;
  },

  buildBytes:function () {
    var result = new Array();
    result.push(this.getCommand());
    result = result.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));

    result = result.concat(Conversion.bytesFromInt(this.getAlbumArtLength(), 4));
    result = result.concat(Conversion.bytesFromInt(this.getDuration(), 2));
    result = result.concat(Conversion.bytesFromInt(this.getElapsed(), 2));
    result.push(this.getRating());
    result.push(this.getPermissionFlags());
    result.push(this.getIdentityFlags());
    return result;
  }

});
var EventCancelGenreStationArtPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_CANCEL_GENRE_STATION_ART);
    }

});
var EventCancelStationArtPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_CANCEL_STATION_ART);
    }

});
var EventOpenAppPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_OPEN_APP);
    },

    parse:function (data) {
        this._super(data);

        this.flags = this.raw[1];

    var appURLBytes = this.raw.slice(2);
    var appURL = "";
    for (var j = 0; j < appURLBytes.length; j++) {
      appURL = appURL + String.fromCharCode(appURLBytes[j]);
    }
    this.appURL = appURL;

    return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
        result.push(this.flags);
    for (var j = 0; j < this.appURL.length; j++) {
      result = result.concat(this.appURL.charCodeAt(j));
    }
    result = result.concat(0);
    return result;
    },

    getFlags:function () {
        return this.flags;
    },

    setFlags: function(flags) {
        this.flags = flags;
    },

  getAppURL:function () {
        return this.appURL;
    },

    setAppURL: function(appURL) {
        this.appURL = appURL;
    }

});
var EventSearchAutoCompletePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_SEARCH_AUTO_COMPLETE);
    },

    parse:function (data) {
        this._super(data);

        var stationTokenBytes = this.raw.slice(1, 5);
    this.stationToken = Conversion.intFromBytes(stationTokenBytes);

    var searchInputBytes = this.raw.slice(5);
    var searchInput = "";
    for (var j = 0; j < searchInputBytes.length; j++) {
      searchInput = searchInput + String.fromCharCode(searchInputBytes[j]);
    }
    this.searchInput = searchInput;

    return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result = result.concat(Conversion.bytesFromInt(this.searchId, 4));
    for (var j = 0; j < this.searchInput.length; j++) {
      result = result.concat(this.searchInput.charCodeAt(j));
    }
    result = result.concat(0);
    return result;
    },

    getSearchId:function () {
        return this.searchId;
    },

    setSearchId: function(searchId) {
        this.searchId = searchId;
    },

  getSearchInput:function () {
        return this.searchInput;
    },

    setSearchInput: function(searchInput) {
        this.searchInput = searchInput;
    }

});
var EventSearchExtendedPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_SEARCH_EXTENDED);
    },

    parse:function (data) {
        this._super(data);

        var stationTokenBytes = this.raw.slice(1, 5);
    this.stationToken = Conversion.intFromBytes(stationTokenBytes);

    var searchInputBytes = this.raw.slice(5);
    var searchInput = "";
    for (var j = 0; j < searchInputBytes.length; j++) {
      searchInput = searchInput + String.fromCharCode(searchInputBytes[j]);
    }
    this.searchInput = searchInput;

    return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result = result.concat(Conversion.bytesFromInt(this.searchId, 4));
    for (var j = 0; j < this.searchInput.length; j++) {
      result = result.concat(this.searchInput.charCodeAt(j));
    }
    result = result.concat(0);
    return result;
    },

    getSearchId:function () {
        return this.searchId;
    },

    setSearchId: function(searchId) {
        this.searchId = searchId;
    },

  getSearchInput:function () {
        return this.searchInput;
    },

    setSearchInput: function(searchInput) {
        this.searchInput = searchInput;
    }

});
var EventSearchSelectPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_SEARCH_SELECT);
    },

    parse:function (data) {
        this._super(data);

        var searchIdBytes = this.raw.slice(1, 5);
    this.searchId = Conversion.intFromBytes(searchIdBytes);

        var musicTokenBytes = this.raw.slice(5, 9);
    this.musicToken = Conversion.intFromBytes(musicTokenBytes);

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result = result.concat(Conversion.bytesFromInt(this.searchId, 4));
    result = result.concat(Conversion.bytesFromInt(this.musicToken, 4));

        return result;
    },

    getSearchId:function () {
        return this.searchId;
    },

    setSearchId: function(searchId) {
        this.searchId = searchId;
    },

    getMusicToken:function () {
        return this.musicToken;
    },

    setMusicToken: function(musicToken) {
        this.musicToken = musicToken;
    }

});
var EventSelectGenreStationPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_SELECT_GENRE_STATION);
    },

    parse:function (data) {
        this._super(data);

    this.categoryIndex = data[1];
    this.stationIndex = data[2];

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result.push(this.categoryIndex);
    result.push(this.stationIndex);

        return result;
    },

    getCategoryIndex:function () {
        return this.categoryIndex;
    },
    setCategoryIndex: function(categoryIndex){
        this.categoryIndex = categoryIndex;
    },

    getStationIndex:function () {
        return this.stationIndex;
    },
    setStationIndex: function(stationIndex){
        this.stationIndex = stationIndex;
    }
});
var EventStationCreateFromCurrentArtistPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_STATION_CREATE_FROM_CURRENT_ARTIST);
    }

});
var EventStationCreateFromCurrentTrackPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_STATION_CREATE_FROM_CURRENT_TRACK);
    }

});
var EventStationDeletePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_STATION_DELETE);
    },

    parse:function (data) {
        this._super(data);

        var stationTokenBytes = this.raw.slice(1, 5);
    this.stationToken = Conversion.intFromBytes(stationTokenBytes);

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result = result.concat(Conversion.bytesFromInt(this.stationToken, 4));

        return result;
    },

    getStationToken:function () {
        return this.stationToken;
    },

    setStationToken: function(stationToken) {
        this.stationToken = stationToken;
    }

});
var EventStationSelectPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_STATION_SELECT);
    },

    parse:function (data) {
        this._super(data);

        var stationTokenBytes = this.raw.slice(1, 5);
    this.stationToken = Conversion.intFromBytes(stationTokenBytes);

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result = result.concat(Conversion.bytesFromInt(this.stationToken, 4));

        return result;
    },

    getStationToken:function () {
        return this.stationToken;
    },

    setStationToken: function(stationToken) {
        this.stationToken = stationToken;
    }

});
var EventStationsSortPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_STATIONS_SORT);
    },

    parse:function (data) {
        this._super(data);

        this.sortOrder = data[1];

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result.push(this.sortOrder);

        return result;
    },

    getSortOrder:function () {
        return this.sortOrder;
    },

    setSortOrder: function(sortOrder) {
        this.sortOrder = sortOrder;
    },

  getSortOrderString:function() {
    return resources.getStringForCode(resources.sort, this.sortOrder);
  }

});
var EventTrackBookmarkArtistPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_TRACK_BOOKMARK_ARTIST);
    }

});
var EventTrackBookmarkTrackPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_TRACK_BOOKMARK_TRACK);
    }

});
var EventTrackExplainPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_TRACK_EXPLAIN);
    }

});
var EventTrackPausePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_TRACK_PAUSE);
    }

});
var EventTrackPlayPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_TRACK_PLAY);
    }

});
var EventTrackRateNegativePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_TRACK_RATE_NEGATIVE);
    }

});
var EventTrackRatePositivePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_TRACK_RATE_POSITIVE);
    }

});
var EventTrackSkipPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_EVENT_TRACK_SKIP);
    }

});
var GetAllGenreCategoryNamesPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_ALL_GENRE_CATEGORY_NAMES);
    }

});
var GetAllStationTokensPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_ALL_STATION_TOKENS);
    }

});
var GetBrandingImagePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_BRANDING_IMAGE);
    }

});
var GetGenreCategoryCountPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_GENRE_CATEGORY_COUNT);
    }

});
var GetGenreCategoryNamesPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_GENRE_CATEGORY_NAMES);
    },

    parse:function (data) {
        this._super(data);

    this.startIndex = data[1];
    this.count = data[2];

        return this;
    },

    getStartIndex:function () {
        return this.startIndex;
    },

    setStartIndex: function(startIndex){
        this.startIndex = startIndex;
    },

  getCount:function () {
        return this.count;
    },

    setCount: function(count){
        this.count = count;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result.push(this.startIndex);
        result.push(this.count);

        return result;
    }

});
var GetGenreCategoryStationCountPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_GENRE_CATEGORY_STATION_COUNT);
    },

    parse:function (data) {
        this._super(data);

    this.categoryIndex = this.data[1];

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result.push(this.categoryIndex);

        return result;
    },

    getCategoryIndex:function () {
        return this.categoryIndex;
    },

    setStationToken: function(categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

});
var GetGenreStationArtPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_GENRE_STATION_ART);
    },

    parse:function (data) {
        this._super(data);

        var maxPayloadLengthBytes = data.slice(1, 5);
    this.maxPayloadLength = Conversion.intFromBytes(maxPayloadLengthBytes);
    this.categoryIndex = data[5];
    this.startIndex = data[6];
    this.count = data[7];

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result = result.concat(Conversion.bytesFromInt(this.maxPayloadLength, 4));
    result.push(this.categoryIndex);
    result.push(this.startIndex);
        result.push(this.count);

        return result;
    },

    getMaxPayloadLength:function () {
        return this.maxPayloadLength;
    },
    setMaxPayloadLength:function(maxPayloadLength){
        this.maxPayloadLength = maxPayloadLength;
    },

    getCategoryIndex:function () {
        return this.categoryIndex;
    },
    setCategoryIndex:function(categoryIndex){
        this.categoryIndex = categoryIndex;
    },

    getStartIndex:function () {
        return this.startIndex;
    },
    setStartIndex:function(startIndex){
        this.startIndex = startIndex;
    },

  getCount:function () {
        return this.count;
    },
    setCount:function(count){
        this.count = count;
    }
});
var GetGenreStationNamesPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_GENRE_STATION_NAMES);
    },

    parse:function (data) {
        this._super(data);

    this.categoryIndex = data[1];
    this.startIndex = data[2];
    this.count = data[3];

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
    result.push(this.categoryIndex);
    result.push(this.startIndex);
        result.push(this.count);

        return result;
    },

    getCategoryIndex:function () {
        return this.categoryIndex;
    },
    setCategoryIndex: function(categoryIndex){
        this.categoryIndex = categoryIndex;
    },

    getStartIndex:function () {
        return this.startIndex;
    },
    setStartIndex: function(startIndex){
        this.startIndex = startIndex;
    },

  getCount:function () {
        return this.count;
    },
    setCount: function(count){
        this.count = count;
    }
});
var GetListenerPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_LISTENER);
    }

});
var GetSearchResultInfoPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_SEARCH_RESULT_INFO);
        this.searchId = null;
    this.musicTokens = null;
    },

    parse:function (data) {
        this._super(data);
        this.searchId = null;
    this.musicTokens = null;

        return this;
    },

    getSearchId:function () {
    if(this.searchId == null) {
      this.searchId = Conversion.intFromBytes(this.raw.slice(1, 5));
    }
        return this.searchId;
    },

    setSearchId: function(searchId){
        this.searchId = searchId;
    },

  getMusicTokens:function () {
    if (this.musicTokens == null) {
      this.musicTokens = new Array();
      for (var i = 5; i < this.raw.length; i += 4) {
        this.musicTokens.push(Conversion.intFromBytes(this.raw.slice(i, i + 4)));
      }
    }
    return this.musicTokens;
    },

  setMusicTokens:function (musicTokens) {
        this.musicTokens = musicTokens;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
        result = result.concat(Conversion.bytesFromInt(this.getSearchId(), 4));

        for (var i = 0; i < this.getMusicTokens().length; i++) {
            result = result.concat(Conversion.bytesFromInt(this.getMusicTokens()[i], 4));
        }

        return result;
    }
});
var GetStationActivePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_STATION_ACTIVE);
    }

});
var GetStationArtPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_STATION_ART);
    this.maxPayloadLength = null;
    this.stationTokens = null;
    },

    parse:function (data) {
        this._super(data);
    this.maxPayloadLength = null;
    this.stationTokens = null;

        return this;
    },

    getMaxPayloadLength:function () {
    if(this.maxPayloadLength == null) {
      this.maxPayloadLength = Conversion.intFromBytes(this.raw.slice(1, 5));
    }
        return this.maxPayloadLength;
    },

    setMaxPayloadLength: function(maxPayloadLength){
        this.maxPayloadLength = maxPayloadLength;
    },

  getStationTokens:function () {
    if (this.stationTokens == null) {
      this.stationTokens = new Array();
      for (var i = 5; i < this.raw.length; i += 4) {
        this.stationTokens.push(Conversion.intFromBytes(this.raw.slice(i, i + 4)));
      }
    }
    return this.stationTokens;
    },

  setStationTokens:function (stationTokens) {
        this.stationTokens = stationTokens;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.command);
        result = result.concat(Conversion.bytesFromInt(this.getMaxPayloadLength(), 4));

        for (var i = 0; i < this.getStationTokens().length; i++) {
            result = result.concat(Conversion.bytesFromInt(this.getStationTokens()[i], 4));
        }

        return result;
    }
});
var GetStationCountPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_STATION_COUNT);
    }

});
var GetStationInfoPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_STATION_INFO);
    this.stationTokens = null;
    },
    parse:function (data) {
        this._super(data);
    this.stationTokens = null;

        return this;
    },

    getStationTokens:function () {
    if (this.stationTokens == null) {
      this.stationTokens = new Array();
      for (var i = 1; i < this.raw.length; i += 4) {
        this.stationTokens.push(Conversion.intFromBytes(this.raw.slice(i, i + 4)));
      }
    }
    return this.stationTokens;
    },

  setStationTokens:function (stationTokens) {
        this.stationTokens = stationTokens;
    },

    buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());

        for (var i = 0; i < this.getStationTokens().length; i++) {
            array = array.concat(Conversion.bytesFromInt(this.getStationTokens()[i], 4));
        }

        return array;
    }
});
var GetStationTokensPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_STATION_TOKENS);
    },

    parse:function (data) {
        this._super(data);

    this.startIndex = data[1];
    this.count = data[2];

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.getCommand());
    result = result.concat(Conversion.bytesFromInt(this.getStartIndex(), 1));
    result = result.concat(Conversion.bytesFromInt(this.getCount(), 1));

        return result;
    },

    getStartIndex:function () {
        return this.startIndex;
    },
    setStartIndex: function(startIndex){
        this.startIndex = startIndex;
    },

  getCount:function () {
        return this.count;
    },
    setCount: function(count){
        this.count = count;
    }
});
var GetStationsOrderPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_STATIONS_ORDER);
    }

});
var GetStatusPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_STATUS);
    }

});
var GetTrackAlbumArtPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_TRACK_ALBUM_ART);
    },

    parse:function (data) {
        this._super(data);

        var maxPayloadLengthBytes = data.slice(1, 5);
        this.maxPayloadLength = Conversion.intFromBytes(maxPayloadLengthBytes);

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getMaxPayloadLength(), 4));

        return result;
    },

    getMaxPayloadLength:function () {
        return this.maxPayloadLength;
    },
    setMaxPayloadLength: function(maxPayloadLength){
        this.maxPayloadLength = maxPayloadLength;
    }
});
var GetTrackAlbumPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_TRACK_ALBUM);
    }

});
var GetTrackArtistPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_TRACK_ARTIST);
    }

});
var GetTrackExplainPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_TRACK_EXPLAIN);
    },

    parse:function (data) {
        this._super(data);

        var maxPayloadLengthBytes = data.slice(1, 5);
        this.maxPayloadLength = Conversion.intFromBytes(maxPayloadLengthBytes);

        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getMaxPayloadLength(), 4));

        return result;
    },

    getMaxPayloadLength:function () {
        return this.maxPayloadLength;
    },
    setMaxPayloadLength: function(maxPayloadLength){
        this.maxPayloadLength = maxPayloadLength;
    }
});
var GetTrackInfoExtendedPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_TRACK_INFO_EXTENDED);
    }

});
var GetTrackInfoPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_TRACK_INFO);
    }

});
var GetTrackTitlePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_GET_TRACK_TITLE);
    }

});
var ReturnBrandingImageSegmentPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_BRANDING_IMAGE_SEGMENT);
    this.segmentIndex = null;
    this.totalSegments = null;
    this.imageData = null;
    },

    parse:function (data) {
        this._super(data);
    this.segmentIndex = null;
    this.totalSegments = null;
    this.imageData = null;

        return this;
    },

    getSegmentIndex:function () {
    if(this.segmentData == null) {
      this.segmentData = this.raw[1];
    }
        return this.segmentData;
    },

  setSegmentIndex:function(segmentIndex) {
    this.segmentIndex = segmentIndex;
  },

    getTotalSegments:function () {
    if(this.totalSegments == null) {
      this.totalSegments = this.raw[2];
    }
        return this.totalSegments;
    },

  setTotalSegments:function(totalSegments) {
    this.totalSegments = totalSegments;
  },

    getImageData:function () {
    if (this.imageData == null) {
            this.imageData = this.raw.slice(3);
    }
        return this.imageData;
    },

  setImageData:function(imageData) {
    this.imageData = imageData;
  },

    buildBytes:function() {
        var result = new Array();
        result.push(this.getCommand());
        result.push(this.getSegmentIndex());
        result.push(this.getTotalSegments());
        result = result.concat(this.getImageData());
        return result;
    }

});
var ReturnGenreCategoryCountPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_GENRE_CATEGORY_COUNT);
    },
    parse:function (data) {
        this._super(data);
        this.count = data[1];
        return this;
    },
    getCount:function () {
        return this.count;
    },
    setCount:function (count) {
        this.count = count;
    },
  buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array.push(this.getCount());
        return array;
    }
});
var ReturnGenreCategoryNamesPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_GENRE_CATEGORY_NAMES);
    this.startIndex = null;
    this.categoryNames = null;
    },
    parse:function (data) {
        this._super(data);
    this.startIndex = null;
    this.categoryNames = null;

        return this;
    },

    getStartIndex:function() {
        if (this.startIndex == null) {
      this.startIndex = this.raw[1];
    }
    return this.startIndex;
    },

  setStartIndex:function(startIndex) {
    this.startIndex = startIndex;
  },

  getCategoryNames:function() {
    if (this.categoryNames == null) {
      this.categoryNames = new Array();
      var categoryNamesBytes = this.raw.slice(2);
      var position = 0;
      while (position < categoryNamesBytes.length) {
        var categoryName = "";
        for (var j = position; j < categoryNamesBytes.length; j++) {
          if (categoryNamesBytes[j] == 0) {
            break;
          }
          categoryName += String.fromCharCode(categoryNamesBytes[j]);
        }
        this.categoryNames.push(categoryName);
        position += categoryName.length + 1;
      }
    }
    return this.categoryNames;
  },

  setCategoryNames:function(categoryNames) {
    this.categoryNames = categoryNames;
  },

  buildBytes:function() {
        var array = new Array();
        array.push(this.getCommand());
        array.push(this.getStartIndex());

    for (var i = 0; i < this.getCategoryNames().length; i++) {
      for (var j = 0; j < this.getCategoryNames()[i].length; j++) {
        array = array.concat(this.getCategoryNames()[i].charCodeAt(j));
      }
      array = array.concat(0);
    }

        return array;
  }
});
var ReturnGenreCategoryStationCountPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_GENRE_CATEGORY_STATION_COUNT);
    this.categoryIndex = null;
    this.count = null;
    },

    parse:function (data) {
        this._super(data);
    this.categoryIndex = null;
    this.count = null;

        return this;
    },

    getCategoryIndex:function() {
        if (this.categoryIndex == null) {
      this.categoryIndex = this.raw[1];
    }
    return this.categoryIndex;
    },

  setCategoryIndex:function(categoryIndex) {
    this.categoryIndex = categoryIndex;
  },

  getCount:function() {
        if (this.count == null) {
      this.count = this.raw[2];
    }
    return this.count;
    },

  setCount:function(count) {
    this.count = count;
  },

  buildBytes:function() {
        var array = new Array();
        array.push(this.getCommand());
        array.push(this.getCategoryIndex());
    array.push(this.getCount());

        return array;
  }
});
var ReturnGenreStationArtSegmentPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_GENRE_STATION_ART_SEGMENT);
    this.categoryIndex = null;
    this.stationIndex = null;
    this.artLength = null;
    this.segmentIndex = null;
    this.totalSegments = null;
    this.imageData = null;
    },

  parse: function(data){
        this._super(data);
    this.categoryIndex = null;
    this.stationIndex = null;
    this.artLength = null;
    this.segmentIndex = null;
    this.totalSegments = null;
    this.imageData = null;

    return this;
  },

    getCategoryIndex:function () {
    if(this.categoryIndex == null) {
      this.categoryIndex = this.raw[1];
    }
        return this.categoryIndex;
    },

  setCategoryIndex:function(categoryIndex) {
     this.categoryIndex = categoryIndex;
  },

    getStationIndex:function () {
    if(this.stationIndex == null) {
      this.stationIndex = this.raw[2];
    }
        return this.stationIndex;
    },

  setStationIndex:function(stationIndex) {
    this.stationIndex = stationIndex;
  },

    getArtLength:function () {
    if (this.artLength == null){
      this.artLength = Conversion.intFromBytes(this.raw.slice(3, 7));
    }
        return this.artLength;
    },

  setArtLength:function(artLength) {
    this.artLength = artLength;
  },

    getSegmentIndex:function () {
    if(this.segmentIndex == null) {
      this.segmentIndex = this.raw[7];
    }
        return this.segmentIndex;
    },

  setSegmentIndex:function(segmentIndex) {
    this.segmentIndex = segmentIndex;
  },

    getTotalSegments:function () {
    if(this.totalSegments == null) {
      this.totalSegments = this.raw[8];
    }
        return this.totalSegments;
    },

  setTotalSegments:function(totalSegments) {
    this.totalSegments = totalSegments;
  },

  getImageData:function () {
    if (this.imageData == null) {
            this.imageData = this.raw.slice(9);
    }
    return this.imageData;
  },

  setImageData:function (imageData) {
    this.imageData = imageData;
  },

    buildBytes:function() {
        var result = new Array();
        result.push(this.getCommand());
    result.push(this.getCategoryIndex());
    result.push(this.getStationIndex());
        result = result.concat(Conversion.bytesFromInt(this.getArtLength(), 4));
        result.push(this.getSegmentIndex());
        result.push(this.getTotalSegments());
        result = result.concat(this.getImageData());
        return result;
    }

});
var ReturnGenreStationNamesPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_GENRE_STATION_NAMES);
    this.categoryIndex = null;
    this.startIndex = null;
    this.names = null;
    },
    parse:function (data) {
        this._super(data);
    this.categoryIndex = null;
    this.startIndex = null;
    this.names = null;

        return this;
    },

    getCategoryIndex:function() {
        if (this.categoryIndex == null) {
      this.categoryIndex = this.raw[1];
    }
    return this.categoryIndex;
    },

  setCategoryIndex:function(categoryIndex) {
    this.categoryIndex = categoryIndex;
  },

    getStartIndex:function() {
        if (this.startIndex == null) {
      this.startIndex = this.raw[2];
    }
    return this.startIndex;
    },

  setStartIndex:function(startIndex) {
    this.startIndex = startIndex;
  },

  getNames:function() {
    if (this.names == null) {
      this.names = new Array();
      var namesBytes = this.raw.slice(3);
      var position = 0;
      while (position < namesBytes.length) {
        var name = "";
        for (var j = position; j < namesBytes.length; j++) {
          if (namesBytes[j] == 0) {
            break;
          }
          name += String.fromCharCode(namesBytes[j]);
        }
        this.names.push(name);
        position += name.length + 1;
      }
    }
    return this.names;
  },

  setNames:function(names) {
    this.names = names;
  },

  buildBytes:function() {
        var array = new Array();
        array.push(this.getCommand());
        array.push(this.getCategoryIndex());
        array.push(this.getStartIndex());

    for (var i = 0; i < this.getNames().length; i++) {
      for (var j = 0; j < this.getNames()[i].length; j++) {
        array = array.concat(this.getNames()[i].charCodeAt(j));
      }
      array = array.concat(0);
    }

        return array;
  }
});
var ReturnListenerPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_LISTENER);
    this.listenerId = null;
    },

    parse:function (data) {
        this._super(data);
    this.listenerId = null;

    return this;
    },

  getListenerId:function () {
    if(this.listenerId == null) {
      var listenerIdBytes = this.raw.slice(1);
      var listenerId = "";
      for (var j = 0; j < listenerIdBytes.length; j++) {
        listenerId = listenerId + String.fromCharCode(listenerIdBytes[j]);
      }
      this.listenerId = listenerId;
    }
        return this.listenerId;
    },

    setListenerId: function(listenerId) {
        this.listenerId = listenerId;
    },

  buildBytes:function () {
    var result = new Array();
    result.push(this.getCommand());
    for (var j = 0; j < this.getListenerId().length; j++) {
      result = result.concat(this.getListenerId().charCodeAt(j));
    }
    result = result.concat(0);
    return result;
  }
});
var ReturnSearchResultInfoPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_SEARCH_RESULT_INFO);
    this.searchId = null;
    this.results = null;
    },

    parse:function (data) {
        this._super(data);
    this.searchId = null;
    this.results = null;

        return this;
    },

  getSearchId:function () {
    if(this.searchId == null) {
      var searchIdBytes = this.raw.slice(1, 5);
      this.searchId = Conversion.intFromBytes(searchIdBytes);
    }
    return this.searchId;
  },

  setSearchId:function(searchId) {
    this.searchId = searchId;
  },

  getResults:function () {
    if (this.results == null) {
      this.results = new Array();
      var position = 5;
      while (position < this.raw.length) {
        var fourBytes = this.raw.slice(position, position + 4);
        var musicToken = Conversion.intFromBytes(fourBytes);
        var type = this.raw[position + 4];
        var restOfBytes = this.raw.slice(position + 5);
        var description = "";
        var descLength = 0;
        for (var j = 0; j < restOfBytes.length; j++) {
          if (restOfBytes[j] == 0) {
            break;
          }
          description += String.fromCharCode(restOfBytes[j]);
          descLength++;
        }
        this.results.push({musicToken: musicToken,type: type,description: description});
        position = position + descLength + 1 + 5;
      }
    }
        return this.results;
    },

  setResults:function(results) {
    this.results = results;
  },

  buildBytes:function() {
    var result = new Array();
    result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getSearchId(), 4));
    for (var i = 0; i < this.getResults().length; i++) {
      result = result.concat(Conversion.bytesFromInt(this.getResults()[i].musicToken, 4));
      result.push(this.getResults()[i].type);

      var description = this.getResults()[i].description;
      for (var j = 0; j < description.length; j++) {
        result.push(description.charCodeAt(j)); //no double byte.
      }
      result.push(0); //null terminator for description string
    }
        return result;
  }
});
var ReturnStationActivePayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_STATION_ACTIVE);
    this.stationToken = null;
    },

    parse:function (data) {
        this._super(data);
    this.stationToken = null;

        return this;
    },

    getStationToken:function () {
    if (this.stationToken == null) {
      var stationTokenBytes = this.raw.slice(1, 5);
      this.stationToken = Conversion.intFromBytes(stationTokenBytes);
    }
    return this.stationToken;
    },

  setStationToken:function(stationToken){
    this.stationToken = stationToken;
  },

  buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array = array.concat(Conversion.bytesFromInt(this.getStationToken(), 4));

        return array;
    }
});
var ReturnStationArtSegmentPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_STATION_ART_SEGMENT);
    this.stationToken = null;
    this.artLength = null;
    this.segmentIndex = null;
    this.totalSegments = null;
    this.imageData = null;
    },

  parse: function(data){
    this._super(data);
    this.stationToken = null;
    this.artLength = null;
    this.segmentIndex = null;
    this.totalSegments = null;
    this.imageData = null;

    return this;
  },

  getStationToken:function () {
    if (this.stationToken == null){
      this.stationToken = Conversion.intFromBytes(this.raw.slice(1,5));
    }
        return this.stationToken;
    },

  setStationToken:function(stationToken) {
    this.stationToken = stationToken;
  },

    getArtLength:function () {
    if (this.artLength == null){
      this.artLength = Conversion.intFromBytes(this.raw.slice(5, 9));
    }
        return this.artLength;
    },

  setArtLength:function(artLength) {
    this.artLength = artLength;
  },

    getSegmentIndex:function () {
    if(this.segmentIndex == null) {
      this.segmentIndex = this.raw[9];
    }
        return this.segmentIndex;
    },

  setSegmentIndex:function(segmentIndex) {
    this.segmentIndex = segmentIndex;
  },

    getTotalSegments:function () {
    if(this.totalSegments == null) {
      this.totalSegments = this.raw[10];
    }
        return this.totalSegments;
    },

  setTotalSegments:function(totalSegments) {
    this.totalSegments = totalSegments;
  },

  getImageData:function () {
    if (this.imageData == null) {
            this.imageData = this.raw.slice(11);
    }
    return this.imageData;
  },

  setImageData:function (imageData) {
    this.imageData = imageData;
  },

    buildBytes:function() {
        var result = new Array();
        result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getStationToken(), 4));
        result = result.concat(Conversion.bytesFromInt(this.getArtLength(), 4));
        result.push(this.getSegmentIndex());
        result.push(this.getTotalSegments());
        result = result.concat(this.getImageData());
        return result;
    }

});
var ReturnStationCountPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_STATION_COUNT);
    },
    parse:function (data) {
        this._super(data);
        this.count = data[1];
        return this;
    },
    getCount:function () {
        return this.count;
    },
    setCount:function (count) {
        this.count = count;
    },
  buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array.push(this.getCount());
        return array;
    }
});
var ReturnStationInfoPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_STATION_INFO);
    this.version = 3;
    this.parsed = false;
    },

    parse:function (data) {
        this._super(data);

        this.stations = new Array();

        return this;
    },

    getFirstStationInfo:function () {
    if(!this.parsed){
      this.parseStationInfoPayload(this.raw);
    }

        if (this.stations && this.stations.length > 0) {
            return this.stations[0];
        }
        return {};
    },

    getStationInfoList:function () {
    if (!this.parsed) {
      this.parseStationInfoPayload(this.raw);
    }
    return this.stations;
  },

  parseStationInfoPayload: function(data) {

    var handleStationToken = function (counter, data, station) {
      var bytes = data.slice(counter, counter + 4);
      station["stationToken"] = Conversion.intFromBytes(bytes);
      return counter + 4;
    };

    var handleFlags = function (counter, data, station) {
      station["flags"] = data[counter];
      return counter + 1;
    };

    var handleStationName = function (counter, data, station) {
      var name = "";
      for (var i = counter; i < data.length; i++) {
        if (data[i] == 0) {
          break;
        }
        name += String.fromCharCode(data[i]);
      }
      if (name.length == 248) {
        this.version = 1;
      }
      station["stationName"] = name;
      return counter + (name.length + 1); //include the null terminated character 0
    };

    var counter = 1; //skip the first byte.
    while (counter < data.length) {
      var station = {};
      counter = handleStationToken(counter, data, station);
      counter = handleFlags(counter, data, station);
      counter = handleStationName(counter, data, station);
      this.stations.push(station);
    }
    this.parsed = true;
  },

  buildBytes :function () {
    var result = new Array();
    result.push(this.getCommand());

    var stations = this.getStationInfoList();

    for (var j = 0; j < stations.length; j++) {
      result = result.concat(Conversion.bytesFromInt(stations[j]['stationToken'], 4));

      result = result.concat(Conversion.bytesFromInt(stations[j]['flags'],1));

      for (var n = 0; n < stations[j]['stationName'].length; n++) {
        result = result.concat(stations[j]['stationName'].charCodeAt(n));
      }
      result = result.concat(0);
//      if (this.version == 1){
//        for (var r = this.stations[j]['stationName'].length; r < 285; r++) {
//          result = result.concat(Conversion.bytesFromInt(0));
//        }
//      }

    }
        return result;
    }
});
var ReturnStationTokensPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_STATION_TOKENS);
    this.startIndex = null;
        this.stationTokens = null;
    },

    parse:function (data) {
        this._super(data);
    this.startIndex = null;
        this.stationTokens = null;

        return this;
    },

    getStartIndex:function () {
    if(this.startIndex == null)
    {
      this.startIndex = this.raw[1];
    }
        return this.startIndex;
    },

    setStartIndex:function (startIndex) {
        this.startIndex = startIndex;
    },

    getStationTokens:function () {
    if (this.stationTokens == null) {
      this.stationTokens = new Array();
      for (var i = 2; i < this.raw.length; i += 4) {
        this.stationTokens.push(Conversion.intFromBytes(this.raw.slice(i, i + 4)));
      }
    }
    return this.stationTokens;
    },

  setStationTokens:function (stationTokens) {
        this.stationTokens = stationTokens;
    },

    buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array.push(this.getStartIndex());

        for (var i = 0; i < this.getStationTokens().length; i++) {
            array = array.concat(Conversion.bytesFromInt(this.getStationTokens()[i], 4));
        }

        return array;
    }
});
var ReturnStationsOrderPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_STATIONS_ORDER);
    this.order = null;
    },

    parse:function (data) {
        this._super(data);
    this.order = null;

        return this;
    },

    getOrder:function () {
    if (this.order == null) {
      this.order = this.raw[1];
    }
    return this.order;
    },

  setOrder:function(order){
    this.order = order;
  },

  buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array.push(this.getOrder());

        return array;
    }
});
var ReturnStatusPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_STATUS);
    },
    parse:function (data) {
        this._super(data);

        this.code = data[1];
        return this;
    },
    getCode:function () {
        return this.code;
    },
    setCode:function (code) {
        this.code = code;
    },
    getStatus:function () {
    return resources.getStringForCode(resources.status, this.code);
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.getCommand());
        result.push(this.getCode());
        return result;
    }
});
var ReturnTrackAlbumArtSegmentPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_TRACK_ALBUM_ART_SEGMENT);
    this.trackToken = null;
    this.segmentIndex = null;
    this.totalSegments = null;
    this.imageData = null;
    },

  parse: function(data){
    this._super(data);
    this.trackToken = null;
    this.segmentIndex = null;
    this.totalSegments = null;
    this.imageData = null;

    return this;
  },

  getTrackToken:function () {
    if (this.trackToken == null){
      this.trackToken = Conversion.intFromBytes(this.raw.slice(1,5));
    }
        return this.trackToken;
    },

  setTrackToken:function(token) {
    this.trackToken = token;
  },

    getSegmentIndex:function () {
    if(this.segmentData == null) {
      this.segmentData = this.raw[5];
    }
        return this.segmentData;
    },

  setSegmentIndex:function(segmentIndex) {
    this.segmentIndex = segmentIndex;
  },

    getTotalSegments:function () {
    if(this.totalSegments == null) {
      this.totalSegments = this.raw[6];
    }
        return this.totalSegments;
    },

  setTotalSegments:function(totalSegments) {
    this.totalSegments = totalSegments;
  },

  getImageData:function () {
    if (this.imageData == null) {
            this.imageData = this.raw.slice(7);
    }
    return this.imageData;
  },

  setImageData:function(imageData) {
    this.imageData = imageData;
  },

    buildBytes:function() {
        var result = new Array();
        result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
        result.push(this.getSegmentIndex());
        result.push(this.getTotalSegments());
        result = result.concat(this.getImageData());
        return result;
    }
});
var ReturnTrackAlbumPayload = Payload.extend({

  init:function (data) {
    this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_TRACK_ALBUM);
    this.trackToken = null;
    this.albumName = null;
  },

  parse:function (data) {
    this._super(data);
    this.trackToken = null;
    this.albumName = null;

    return this;
  },

  getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
    return this.trackToken;
  },

  setTrackToken: function(token) {
    this.trackToken = token;
  },

  getAlbumName:function () {
    if (this.albumName == null) {
      var albumNameBytes = this.raw.slice(5);
      var albumName = "";
      for (var j = 0; j < albumNameBytes.length; j++) {
        albumName = albumName + String.fromCharCode(albumNameBytes[j]);
      }
      this.albumName = albumName;
    }
    return this.albumName;
  },

  setAlbumName:function (name) {
    this.albumName = name;
  },

  buildBytes: function() {
    var result = new Array();
    result.push(this.getCommand());
    result = result.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
    for (var j = 0; j < this.getAlbumName().length; j++) {
      result = result.concat(this.getAlbumName().charCodeAt(j));
    }
    result = result.concat(0);
    return result;
  }
});
var ReturnTrackArtistPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_TRACK_ARTIST);
    this.trackToken = null;
    this.artistName = null;

    },

    parse:function (data) {
        this._super(data);
    this.trackToken = null;
    this.artistName = null;

        return this;
    },

    getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
    return this.trackToken;
    },

  setTrackToken: function(token){
    this.trackToken = token;
  },

  getArtistName:function () {
    if (this.artistName == null) {
      var artistNameBytes = this.raw.slice(5);
      var artistName = "";
      for (var j = 0; j < artistNameBytes.length; j++) {
        artistName = artistName + String.fromCharCode(artistNameBytes[j]);
      }
      this.artistName = artistName;
    }
    return this.artistName;
  },

    setArtistName:function (name) {
        this.artistName = name;
    },

  buildBytes: function() {
    var result = new Array();
        result.push(this.getCommand());
    result = result.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
    for (var j = 0; j < this.getArtistName().length; j++) {
      result = result.concat(this.getArtistName().charCodeAt(j));
    }
    result = result.concat(0);
    return result;
  }
});
var ReturnTrackExplainSegmentPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_TRACK_EXPLAIN_SEGMENT);
    this.trackToken = null;
    this.segmentIndex = null;
    this.totalSegments = null;
    this.explainData = null;
    },

  parse: function(data){
    this._super(data);
    this.trackToken = null;
    this.segmentIndex = null;
    this.totalSegments = null;
    this.explainData = null;

    return this;
  },

  getTrackToken:function () {
    if (this.trackToken == null){
      this.trackToken = Conversion.intFromBytes(this.raw.slice(1,5));
    }
        return this.trackToken;
    },

  setTrackToken:function(token) {
    this.trackToken = token;
  },

    getSegmentIndex:function () {
    if(this.segmentData == null) {
      this.segmentData = this.raw[5];
    }
        return this.segmentData;
    },

  setSegmentIndex:function(segmentIndex) {
    this.segmentIndex = segmentIndex;
  },

    getTotalSegments:function () {
    if(this.totalSegments == null) {
      this.totalSegments = this.raw[6];
    }
        return this.totalSegments;
    },

  setTotalSegments:function(totalSegments) {
    this.totalSegments = totalSegments;
  },

  getExplainData:function () {
    if (this.explainData == null) {
            this.explainData = this.raw.slice(7);
    }
    return this.explainData;
  },

  setExplainData:function(explainData) {
    this.explainData = explainData;
  },

    buildBytes:function() {
        var result = new Array();
        result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
        result.push(this.getSegmentIndex());
        result.push(this.getTotalSegments());
        result = result.concat(this.getExplainData());
        return result;
    }
});
var ReturnTrackInfoExtendedPayload = ReturnTrackInfoPayload.extend({
  init:function (data) {
    this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_TRACK_INFO_EXTENDED);
  },
  parse:function (data) {
    this._super(data);
    this.parsed = false;
    return this;
  },

  parsePayload: function(data) {
    var state = 0;
    var extendedBytes = data.slice(16, data.length);
    var temp = "";
    for (var j = 0; j < extendedBytes.length; j++) {
      if (extendedBytes[j] == 0) {
        if (state == 0) {
          this.trackName = temp;
        } else if (state == 1) {
          this.artistName = temp;
        } else {
          this.albumName = temp;
        }
        state++;
        temp = "";
      } else {
        temp = temp + String.fromCharCode(extendedBytes[j]);
      }
    }
    this.parsed = true;
  },

  getText: function() {
    if (!this.parsed) {
      this.parsePayload(this.raw);
    }
    return this.text;
  },

  setText: function(text) {
    this.text = text;
  },

  getTrackName: function() {
    if (!this.parsed) {
      this.parsePayload(this.raw);
    }
    return this.trackName;
  },

  setTrackName: function(name) {
    this.trackName = name;
  },

  getAlbumName: function() {
    if (!this.parsed) {
      this.parsePayload(this.raw);
    }
    return this.albumName;
  },

  setAlbumName: function(name) {
    this.albumName = name;
  },

  getArtistName: function() {
    if (!this.parsed) {
      this.parsePayload(this.raw);
    }
    return this.artistName;
  },

  setArtistName: function(name) {
    this.artistName = name;
  },

  buildBytes:function () {
    var result = this._super();
    var j = 0;
    //TODO check with UTF8
    for (j = 0; j < this.getTrackName().length; j++) {
      result = result.concat(this.getTrackName().charCodeAt(j));
    }
    result = result.concat(0);
    for (j = 0; j < this.getArtistName().length; j++) {
      result = result.concat(this.getArtistName().charCodeAt(j));
    }
    result = result.concat(0);
    for (j = 0; j < this.getAlbumName().length; j++) {
      result = result.concat(this.getAlbumName().charCodeAt(j));
    }
    result = result.concat(0);
    return result;
  }

});
var ReturnTrackTitlePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_RETURN_TRACK_TITLE);
    this.trackToken = null;
    this.trackName = null;
    },

    parse:function (data) {
        this._super(data);
    this.trackToken = null;
    this.trackName = null;

        return this;
    },

    getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
        return this.trackToken;
    },

  setTrackToken: function(token){
    this.trackToken = token;
  },

    getTrackName:function () {
    if (this.trackName == null) {
      var trackNameBytes = this.raw.slice(5);
      var trackName = "";
      for (var j = 0; j < trackNameBytes.length; j++) {
        trackName = trackName + String.fromCharCode(trackNameBytes[j]);
      }
      this.trackName = trackName;
    }
        return this.trackName;
    },

  setTrackName: function(name){
    this.trackName = name;
  },

  buildBytes: function() {
    var result = new Array();
        result.push(this.getCommand());
    result = result.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
    for (var j = 0; j < this.getTrackName().length; j++) {
      result = result.concat(this.getTrackName().charCodeAt(j));
    }
    result = result.concat(0);
    return result;
  }
});
var SessionStartPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_SESSION_START);
    },

    parse:function (data) {
        this._super(data);

        var firstTwoBytes = data.slice(1, 3);
        this.apiVersion = Conversion.intFromBytes(firstTwoBytes);
        var accessoryBytes = data.slice(3, 11);
        var accessory = "";
        for (var i = 0; i < accessoryBytes.length; i++) {
            accessory = accessory + String.fromCharCode(accessoryBytes[i]);
        }
        this.accessoryId = accessory;

        var albumArtSizeArray = data.slice(11, 13);
        this.albumArtSize = Conversion.intFromBytes(albumArtSizeArray);

        this.imageType = Number(data[13]);

        this.flags = data[14];

        if (this.apiVersion == 3) {
            var stationArtDimensionArray = data.slice(15);
            this.stationArtDimension = Conversion.intFromBytes(stationArtDimensionArray);
        }
        return this;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.getCommand());
        if (this.getApiVersion()) {
            result = result.concat(Conversion.bytesFromInt(this.getApiVersion(), 2));
        }
        var accessoryId = this.getAccessoryId();
        //8 character
        for (var i = 0; i < accessoryId.length; i++) {
            result.push(accessoryId[i].charCodeAt(0)); //no double byte.
        }
        result = result.concat(Conversion.bytesFromInt(this.getAlbumArtSize(), 2));
        result.push(this.getImageType());
        result.push(this.getFlags());
        if (this.getStationArtDimension()) {
            result = result.concat(Conversion.bytesFromInt(this.getStationArtDimension(), 2));
        }
        return result;
    },

    getApiVersion:function () {
        return this.apiVersion;
    },
    setApiVersion: function(version){
        this.apiVersion = version;
    },
    getAccessoryId:function () {
        return this.accessoryId;
    },
    setAccessoryId:function (accessoryId) {
        this.accessoryId = accessoryId;
    },
    getAlbumArtSize:function () {
        return this.albumArtSize;
    },
    setAlbumArtSize:function (albumArtSize) {
        this.albumArtSize = albumArtSize;
    },
    getStationArtDimension:function () {
        return this.stationArtDimension;
    },
    setStationArtDimension:function (stationArtDimension) {
        this.stationArtDimension = stationArtDimension;
    },
    getFlags:function () {
        return this.flags;
    },
    setFlags:function (flags) {
        this.flags = flags;
    },
    getImageType:function () {
        return this.imageType;
    },
  setImageType: function(imageType){
    this.imageType = imageType;
  }
});
var SessionTerminatePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_SESSION_TERMINATE);
    }

});
var SetTrackElapsedPollingPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_SET_TRACK_ELAPSED_POLLING);
    },
    parse:function (data) {
        this._super(data);
        this.enabled = data[1];

        return this;
    },
    setEnabled:function(enabled) {
        this.enabled = enabled;
    },
    getEnabled:function() {
        return this.enabled;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.getCommand());
        result.push(this.enabled);
        return result;
    }
});
var UpdateBrandingImagePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_BRANDING_IMAGE);
    },

    parse:function (data) {
        this._super(data);

        var imageLengthBytes = data.slice(1, 5);
        this.imageLength = Conversion.intFromBytes(imageLengthBytes);

        return this;
    },

    getImageLength:function () {
        return this.imageLength;
    },

    setImageLength: function(imageLength){
        this.imageLength = imageLength;
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getImageLength(), 4));

        return result;
    }
});
var UpdateNoticePayload = Payload.extend({
    init:function (data) {
        this._super(data);
    },
    parse:function (data) {
        this._super(data);

        this.code =  data[1];
        return this;
    },
    getCode:function () {
        return this.code;
    },
  getNotice:function () {
    return resources.getStringForCode(resources.notice, this.code);
    }
});
var UpdateSearchPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_SEARCH);
    this.searchId = null;
    this.musicTokens = null;
    },

    parse:function (data) {
        this._super(data);
    this.searchId = null;
    this.musicTokens = null;

        return this;
    },

    getSearchId:function () {
    if(this.searchId == null) {
      var searchIdBytes = this.raw.slice(1,5);
      this.searchId = Conversion.intFromBytes(searchIdBytes);
    }
        return this.searchId;
    },

    setSearchId:function (searchId) {
        this.searchId = searchId;
    },

    getMusicTokens:function () {
    if (this.musicTokens == null) {
      this.musicTokens = new Array();
      for (var i = 5; i < this.raw.length; i += 4) {
        this.musicTokens.push(Conversion.intFromBytes(this.raw.slice(i, i+4)));
      }
    }
    return this.musicTokens;
    },

    setMusicTokens:function (musicTokens) {
    this.musicTokens = musicTokens;
    },

    buildBytes:function () {
        var result = new Array();
    result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getSearchId(), 4));
    for (var i = 0; i < this.getMusicTokens().length; i++) {
      result = result.concat(Conversion.bytesFromInt(this.getMusicTokens()[i], 4));
    }
        return result;
    }
});
var UpdateStationActivePayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_STATION_ACTIVE);
    this.stationToken = null;
    },

    parse:function (data) {
        this._super(data);
    this.stationToken = null;

        return this;
    },

    getStationToken:function () {
    if (this.stationToken == null) {
      var stationTokenBytes = this.raw.slice(1, 5);
      this.stationToken = Conversion.intFromBytes(stationTokenBytes);
    }
    return this.stationToken;
    },

  setStationToken:function(stationToken){
    this.stationToken = stationToken;
  },

  buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array = array.concat(Conversion.bytesFromInt(this.getStationToken(), 4));

        return array;
    }
});
var UpdateStationAddedPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_STATION_ADDED);
    this.stationToken = null;
    },

    parse:function (data) {
        this._super(data);
    this.stationToken = null;

        return this;
    },

    getStationToken:function () {
    if (this.stationToken == null) {
      var stationTokenBytes = this.raw.slice(1, 5);
      this.stationToken = Conversion.intFromBytes(stationTokenBytes);
    }
    return this.stationToken;
    },

  setStationToken:function(stationToken){
    this.stationToken = stationToken;
  },

  buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array = array.concat(Conversion.bytesFromInt(this.getStationToken(), 4));

        return array;
    }
});
var UpdateStationDeletedPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_STATION_DELETED);
    this.stationToken = null;
    },

    parse:function (data) {
        this._super(data);
    this.stationToken = null;

        return this;
    },

    getStationToken:function () {
    if (this.stationToken == null) {
      var stationTokenBytes = this.raw.slice(1, 5);
      this.stationToken = Conversion.intFromBytes(stationTokenBytes);
    }
    return this.stationToken;
    },

  setStationToken:function(stationToken){
    this.stationToken = stationToken;
  },

  buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array = array.concat(Conversion.bytesFromInt(this.getStationToken(), 4));

        return array;
    }
});
var UpdateStationsOrderPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_STATIONS_ORDER);
    this.order = null;
    },

    parse:function (data) {
        this._super(data);
    this.order = null;

        return this;
    },

    getOrder:function () {
    if (this.order == null) {
      this.order = this.raw[1];
    }
    return this.order;
    },

  setOrder:function(order){
    this.order = order;
  },

  buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array.push(this.getOrder());

        return array;
    }
});
var UpdateStatusPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_STATUS);
    },

    parse:function (data) {
        this._super(data);

        this.code = data[1];
        return this;
    },

    getCode:function () {
        return this.code;
    },

    setCode:function (code) {
        this.code = code;
    },

    getStatus:function () {
    return resources.getStringForCode(resources.status, this.code);
    },

    buildBytes:function () {
        var result = new Array();
        result.push(this.getCommand());
        result.push(this.getCode());
        return result;
    }
});
var UpdateTrackAlbumArtPayload = Payload.extend({
    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_TRACK_ALBUM_ART);
    this.trackToken = null;
    this.imageLength = null;
    },

    parse:function (data) {
        this._super(data);
    this.trackToken = null;
    this.imageLength = null;

        return this;
    },

    getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
        return this.trackToken;
    },

  setTrackToken:function(token) {
    this.trackToken = token;
  },

  getImageLength:function () {
    if (this.imageLength == null) {
      var imageLengthBytes = this.raw.slice(5, 9);
      this.imageLength = Conversion.intFromBytes(imageLengthBytes);
    }
        return this.imageLength;
    },

  setImageLength:function(imageLength) {
    this.imageLength = imageLength;
  },

  buildBytes:function () {
        var result = new Array();
        result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
    result = result.concat(Conversion.bytesFromInt(this.getImageLength(), 4));
        return result;
    }

});
var UpdateTrackBookmarkArtistPayload = Payload.extend({

    init:function (data) {
        this._super(data);
        this.setCommand(resources.command.toCode.PNDR_UPDATE_TRACK_BOOKMARK_ARTIST);
    this.trackToken = null;
    this.isBookmarked = null;
    },

    parse:function (data) {
        this._super(data);
    this.trackToken = null;
    this.isBookmarked = null;

        return this;
    },

    getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
        return this.trackToken;
    },

    getIsBookmarked:function () {
    if (this.isBookmarked == null) {
      this.isBookmarked = this.raw[5];
    }
        return this.isBookmarked;
    },

    buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array = array.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
        array.push(this.getIsBookmarked());

        return array;
    }
});
var UpdateTrackBookmarkTrackPayload = Payload.extend({

    init:function (data) {
        this._super(data);
        this.setCommand(resources.command.toCode.PNDR_UPDATE_TRACK_BOOKMARK_TRACK);
    this.trackToken = null;
    this.isBookmarked = null;
    },

    parse:function (data) {
        this._super(data);
    this.trackToken = null;
    this.isBookmarked = null;

        return this;
    },

    getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
        return this.trackToken;
    },

    getIsBookmarked:function () {
    if (this.isBookmarked == null) {
      this.isBookmarked = this.raw[5];
    }
        return this.isBookmarked;
    },

    buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array = array.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
        array.push(this.getIsBookmarked());

        return array;
    }
});
var UpdateTrackCompletedPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_TRACK_COMPLETED);
    this.trackToken = null;
    },

    parse:function (data) {
        this._super(data);
    this.trackToken = null;

        return this;
    },

    getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
        return this.trackToken;
    },

  setTrackToken:function(token) {
    this.trackToken = token;
  },

    buildBytes:function() {
        var result = new Array();
        result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
        return result;
    }

});
var UpdateTrackElapsedPayload = Payload.extend({

  init:function (data) {
    this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_TRACK_ELAPSED);
    this.trackToken = null;
    this.timeElapsed = null;
  },

  parse:function (data) {
    this._super(data);
    this.trackToken = null;
    this.timeElapsed = null;

    return this;
  },

  getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
    return this.trackToken;
  },

  setTrackToken:function(token) {
    this.trackToken = token;
  },

  getTimeElapsed:function () {
    if (this.timeElapsed == null) {
      this.timeElapsed = Conversion.intFromBytes(this.raw.slice(5));
    }
    return this.timeElapsed;
  },

  setTimeElapsed:function(timeElapsed) {
    this.timeElapsed = timeElapsed;
  },

    buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array = array.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
        array = array.concat(Conversion.bytesFromInt(this.getTimeElapsed(), 2));

        return array;
    }

});
var UpdateTrackExplainPayload = Payload.extend({

    init:function (data) {
        this._super(data);
        this.setCommand(resources.command.toCode.PNDR_UPDATE_TRACK_EXPLAIN);
    this.trackToken = null;
    this.explainLength = null;
    },

    parse:function (data) {
        this._super(data);
    this.trackToken = null;
    this.explainLength = null;

        return this;
    },

    getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
        return this.trackToken;
    },

  setTrackToken:function(token) {
    this.trackToken = token;
  },

    getExplainLength:function () {
    if (this.explainLength == null) {
      var explainLengthBytes = this.raw.slice(5, 9);
      this.explainLength = Conversion.intFromBytes(explainLengthBytes);
    }
        return this.explainLength;
    },

  setExplainLength:function(explainLength) {
    this.explainLength = explainLength;
  },

    buildBytes:function () {
        var array = new Array();
        array.push(this.getCommand());
        array = array.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
        array = array.concat(Conversion.bytesFromInt(this.getExplainLength(), 4));

        return array;
    }
});
var UpdateTrackPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_TRACK);
    this.trackToken = null;
    },

    parse:function (data) {
        this._super(data);
    this.trackToken = null;

        return this;
    },

    getTrackToken:function () {
        if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
        return this.trackToken;
    },

    setTrackToken:function (token) {
        this.trackToken = token;
    },

    buildBytes:function () {
        var result = new Array();
    result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
        return result;
    }
});
var UpdateTrackRatingPayload = Payload.extend({

    init:function (data) {
        this._super(data);
    this.setCommand(resources.command.toCode.PNDR_UPDATE_TRACK_RATING);
    this.trackToken = null;
    this.rating = null;
    },

    parse:function (data) {
        this._super(data);
    this.trackToken = null;
    this.rating = null;

        return this;
    },

    getTrackToken:function () {
    if (this.trackToken == null) {
      var trackTokenBytes = this.raw.slice(1, 5);
      this.trackToken = Conversion.intFromBytes(trackTokenBytes);
    }
        return this.trackToken;
    },

  setTrackToken:function(token) {
    this.trackToken = token;
  },

  getRating:function () {
    if (this.rating == null) {
      this.rating = this.raw[5];
    }
        return this.rating;
    },

  setRating:function(rating) {
    this.rating = rating;
  },

    buildBytes:function() {
        var result = new Array();
        result.push(this.getCommand());
        result = result.concat(Conversion.bytesFromInt(this.getTrackToken(), 4));
        result.push(this.getRating());
        return result;
    }

});
