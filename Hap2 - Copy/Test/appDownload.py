import httplib2
import socks

HAP_ADDRESS = '192.168.0.3'
HAP_PROXY_PORT = 1080

REQUEST_URI = 'http://nissanmip-mipgw.viaaq.net/aqMIP/api/1.0/ProfileManager/application/' + \
	'appName/Facebook/appVersion/4.0/appType/HEADUNIT_APP/startingIndex/0/mipId/4b429313-cf90-11e1-9784-bfa7cfd88cba'

httplib2.debuglevel=4

h = httplib2.Http(proxy_info = httplib2.ProxyInfo(proxy_type=socks.PROXY_TYPE_SOCKS5, proxy_host=HAP_ADDRESS, proxy_port=HAP_PROXY_PORT))
resp,content = h.request(REQUEST_URI, 'GET')

print 'Reponse: ', resp

open('facebook.zip', 'wb').write(content)
print 'Wrote content to facebook.zip'
