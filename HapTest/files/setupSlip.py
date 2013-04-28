'''
Created on Apr 27, 2013

@author: xuzhuo
'''

import bluetooth
import subprocess 
import time

class SlipConnection:
    '''
    Creates a RFCOMM/SLIP/IP/TCP connection.
    '''

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
            self.slattachProcess.terminate()

    def _bindToRemoteService(self, port):
            subprocess.Popen(['rfcomm', 'release', '/dev/rfcomm/0'])
            time.sleep(2)
            subprocess.Popen(['rfcomm', 'bind', '/dev/rfcomm/0', self.address, str(port)])
            time.sleep(2)
        
    def _slattach(self):
            self.slattachProcess = subprocess.Popen(['slattach', '-p', 'slip', '/dev/rfcomm0'])
            time.sleep(8)

    def _ifconfig(self):
            subprocess.Popen(['ifconfig', 'sl0', self.HOST_ADDRESS, 'dstaddr', self.DESTINATION_ADDRESS])
            time.sleep(2)

    def _findPort(self, uuid, address):
        # Attempts to find the MIP service on the remote device.
        matches = bluetooth.find_service( uuid = self.MIP_UUID, address = self.address)
                
        if len(matches) == 0:
            print 'MIP service not found at ', self.address
            return False
        
        firstMatch = matches[0]
        return firstMatch['port']

if __name__ == '__main__':
    
    slip = SlipConnection('1C:E2:CC:7A:7F:18')
    slip.connect()
    print 'pass'
    pass