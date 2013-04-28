import httplib2
import socks


httplib2.debuglevel=4

h = httplib2.Http()
resp,content = h.request('http://192.168.0.3:8080/hap/api/1.0/handsetProfile',
			 'POST',
			 body='{\"command\":\"getHandsetProfile\"}',
			 headers={'content-type':'application/json'} )

print 'Reponse: ', resp
print 'Content: ', content

