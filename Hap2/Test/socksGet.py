import httplib2
import socks

HAP_ADDRESS = '192.168.0.3'
HAP_PROXY_PORT = 1080

REQUEST_URI = 'http://posttestserver.com/post.php'

httplib2.debuglevel=4

h = httplib2.Http(proxy_info = httplib2.ProxyInfo(proxy_type=socks.PROXY_TYPE_SOCKS5, proxy_host=HAP_ADDRESS, proxy_port=HAP_PROXY_PORT))
resp,content = h.request(REQUEST_URI, 'POST', 'TEST')

print 'Reponse: ', resp
print 'Content: ', content
