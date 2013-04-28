import httplib2

httplib2.debuglevel=4

h = httplib2.Http()
resp,content = h.request('http://192.168.0.3:8090/hap/api/1.0/getEventId', 'GET')

print 'Reponse: ', resp
print 'Content: ', content

