
Pushed experimental app download with connection script to Test/multipleAppDownloadConnect.py.
 
Run as:
sudo python multipleAppDownloadConnect.py --address=CC:8C:E3:28:AB:B0 --num=1
 
address = phone’s Bluetooth address
num=number of simultaneous downloads
 
 
It may not kill ‘slattach’ if it fails.  May have to:
ps -ef | grep slattach
sudo kill <slattach PID>
