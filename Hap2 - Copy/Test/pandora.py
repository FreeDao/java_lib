import binascii, httplib2, threading, time


def printResponse(response, content):
	'''Prints and HTTP response with content.'''
	print 'Response: ', response
	
	# create a hex string if content is binary
	if response['content-type'] == 'application/octet-stream':
		content = binascii.hexlify(content)

	print 'Content: ', content


class LongPollingThread(threading.Thread):
	'''Thread waiting on long polling events.'''
	def __init__(self):
		super(LongPollingThread, self).__init__()
		self.daemon = True

	def run(self):
		print '--LONG POLLING THREAD STARTED--'
		while True:
			h = httplib2.Http()
			resp,content = h.request('http://192.168.0.3:8090/hap/api/1.0/getEventId', 'GET')
			print '--LONG POLLING EVENT--'
			printResponse(resp, content)

	
if __name__ == '__main__':
	httplib2.debuglevel=4
	h = httplib2.Http()

	# send handset profile request
	resp,content = h.request('http://192.168.0.3:8080/hap/api/1.0/handsetProfile',
				 'POST',
				 body='{\"command\":\"getHandsetProfile\"}',
				 headers={'content-type':'application/json'} )
	printResponse(resp, content)
	
	time.sleep(10)

	# setup thread to listen for long polling events
	lpThread = LongPollingThread()
	lpThread.start()
	
	time.sleep(10)
	
	# send Pandora session start command
	PNDR_SESSION_START = '7E000000000011000003415049544F4F4C3000C80100006488FA7C'
	REQUEST_URI = 'http://192.168.0.3:8080/hap/api/1.0/commandControl/Pandora'
	
	# send request to the agent 
	resp,content = h.request(REQUEST_URI,
		'POST',
		body=binascii.unhexlify(PNDR_SESSION_START),
		headers={'content-type':'application/octet-stream'} )
	printResponse(resp, content)
