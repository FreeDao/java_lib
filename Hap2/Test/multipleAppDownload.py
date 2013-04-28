from Queue import Queue
import httplib2, socks, threading, time

NUMBER_THREADS = 2
REQUEST_TIMEOUT = 60 # seconds

def requestFunc(id):
    ''' Send an application download request.'''
    HAP_ADDRESS = '192.168.0.3'
    HAP_PROXY_PORT = 1080
    
    REQUEST_URI = 'http://nissanmip-mipgw.viaaq.net/aqMIP/api/1.0/' + \
        'ProfileManager/application/appName/Facebook/appVersion/4.0' + \
        '/appType/HEADUNIT_APP/startingIndex/0/mipId/d4f8a757-d770-11e1-83a7-895fdff7e279'

    h = httplib2.Http(timeout=REQUEST_TIMEOUT,
        proxy_info = httplib2.ProxyInfo(
            proxy_type=socks.PROXY_TYPE_SOCKS5,
            proxy_host=HAP_ADDRESS,
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
    # init a Thread pool with the desired number of threads
    pool = ThreadPool(NUMBER_THREADS)
    
    threadList = range(NUMBER_THREADS)
    for i in threadList:
        # add the task to the queue
        pool.add_task(requestFunc, i + 1)

    # wait for completion
    while pool.tasks.unfinished_tasks > 0:
        time.sleep(1)
