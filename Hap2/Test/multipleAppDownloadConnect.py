from Queue import Queue
import argparse, httplib2, socks, subprocess, threading, time
from bluetooth import find_service

#ADDRESS = '6c:83:36:6b:4f:e3'
#NUMBER_THREADS = 1
REQUEST_TIMEOUT = 60 # seconds


class SlipConnection:
    MIP_UUID = '3d92d640-ad7f-11e0-b780-0002a5d5c51b'
    HOST_ADDRESS = '192.168.0.2'
    DESTINATION_ADDRESS = '192.168.0.3'

    def __init__(self, address):
        self.address = address
        self.slattachProcess = None

    def connect(self):
        port = self._findPort(self.MIP_UUID, self.address)
        
        if port:
            print 'MIP SERVICE FOUND: port =', port
            self._bindToRemoteService(port)
            self._slattach()
            self._ifconfig()
            return True
    
    def disconnect(self):
        if self.slattachProcess:
            self.slattachProcess.kill()

    def _bindToRemoteService(self, port):
            subprocess.Popen(['rfcomm', 'release', '/dev/rfcomm/0'])
            time.sleep(2)
            subprocess.Popen(['rfcomm', 'bind', '/dev/rfcomm/0', self.address, str(port)])
            time.sleep(2)
        
    def _slattach(self):
            self.slattachProcess = subprocess.Popen(['slattach', '-d', '-p', 'slip', '/dev/rfcomm0'])
            time.sleep(2)

    def _ifconfig(self):
            subprocess.Popen(['ifconfig', 'sl0', self.HOST_ADDRESS, 'dstaddr', self.DESTINATION_ADDRESS])
            time.sleep(2)

    def _findPort(self, uuid, address):
        # Attempts to find the MIP service on the remote device.
        matches = find_service( uuid = self.MIP_UUID, address = self.address)
                
        if len(matches) == 0:
            print 'MIP service not found at ', self.address
            return False
        
        firstMatch = matches[0]
        return firstMatch['port']




def requestFunc(id, destinationAddress):
    ''' Send an application download request.'''
    HAP_PROXY_PORT = 1080
    
    REQUEST_URI = 'http://nissanmip-mipgw.viaaq.net/aqMIP/api/1.0/' + \
        'ProfileManager/application/appName/Facebook/appVersion/4.0' + \
        '/appType/HEADUNIT_APP/startingIndex/0/mipId/4b429313-cf90-11e1-9784-bfa7cfd88cba'

    h = httplib2.Http(timeout=REQUEST_TIMEOUT,
        proxy_info = httplib2.ProxyInfo(
            proxy_type=socks.PROXY_TYPE_SOCKS5,
            proxy_host=destinationAddress,
            proxy_port=HAP_PROXY_PORT
            )
        )
    print '----------\nRequest ' + str(id)
    resp,content = h.request(REQUEST_URI, 'GET')
    
    print '----------\nResponse ' + str(id) + ':', resp
    filename = 'facebook' + str(id) + '.zip'
    open(filename, 'wb').write(content)
    print 'Wrote content to', filename


class Worker(threading.Thread):
    """Thread executing tasks from a given tasks queue"""
    def __init__(self, tasks):
        threading.Thread.__init__(self)
        self.tasks = tasks
        self.daemon = True
        self.start()
    
    def run(self):
        while True:
            func, args, kargs = self.tasks.get()
            try: func(*args, **kargs)
            except Exception, e: print e
            self.tasks.task_done()

class ThreadPool:
    """Pool of threads consuming tasks from a queue"""
    def __init__(self, num_threads):
        self.tasks = Queue(num_threads)
        for _ in range(num_threads): Worker(self.tasks)

    def add_task(self, func, *args, **kargs):
        """Add a task to the queue"""
        self.tasks.put((func, args, kargs))

    def wait_completion(self):
        """Wait for completion of all the tasks in the queue"""
        self.tasks.join()


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Multiple Application Download Test')
    parser.add_argument('--address', help='Remote Bluetooth address')
    parser.add_argument('--num', help='Number of Application Download Requests')
    args = parser.parse_args()
    
    slip = SlipConnection(args.address)
    
    if slip.connect():
    
        # init a Thread pool with the desired number of threads
        pool = ThreadPool(int(args.num))
        
        threadList = range(int(args.num))
        for i in threadList:
            # add the task to the queue
            pool.add_task(requestFunc, i + 1, slip.DESTINATION_ADDRESS)
    
        # wait for completion
        while pool.tasks.unfinished_tasks > 0:
            time.sleep(1)
            
        slip.disconnect()
