import httplib2, json

REQUEST_URI = 'http://192.168.0.3:8080/hap/api/1.0/commandControl/com.clearchannel.iHeartRadio'

httplib2.debuglevel=4
 
h = httplib2.Http()
resp,content = h.request(REQUEST_URI, 'POST', body='{"command":"getMyStations"}', headers={'content-type':'text/plain'} )

print 'Reponse: ', resp
print 'Content: ', content
