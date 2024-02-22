'''
linter warning: Missing module docstring

This is a client code which connects to the server
'''


import socket
from time import time #standard import should be placed before builder
import os
import sys

#analyser error: unable to import 'builder'
#add path variable
sys.path.append(os.path.join(os.getcwd().rsplit('/',1)[0], 'payload'))

from builder import BasicBuilder



#linter warning: Class inherits from object, can be removed in python 3
class BasicClient():
    '''
    linter warning: Missing class docstring
    Client class to connect, disconnet and send message to server
    '''

    def __init__(self, name, ipaddr="127.0.0.1", port=2000):
        self._clt = None
        self.name = name
        self.ipaddr = ipaddr
        self.port = port

        self.group = "public"

        if self.ipaddr is None:
            raise ValueError("IP address is missing or empty")
        #pylint warning : unnecessary elif after raise
        if self.port is None:
            raise ValueError("port number is missing")

        self.connect()

    def __del__(self):
        self.stop()

    def stop(self):
        '''
        linter warning: Missing method docstring
        Call the function to close connection with server.
        '''
        if self._clt is not None:
            self._clt.close()
        self._clt = None

    def connect(self):
        '''
        linter warning: Missing method docstring
        Call the function to connect with the server.
        '''
        if self._clt is not None:
            return

        addr = (self.ipaddr,self.port)
        self._clt = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._clt.connect(addr)
        #self._clt.setblocking(False)

    def join(self, group):
        '''
        linter warning: Missing method docstring
        Call the function to join/change to public/private group.
        '''
        self.group = group

    #pylint warning: method name does not conform to snake_case naming style
    def send_msg(self, text):
        '''
        linter warning: Missing method docstring
        Call the function to send message to the server.
        '''
        if self._clt is None:
            raise RuntimeError("No connection to server exists")

        print(f"sending to group {self.group} from {self.name}: {text}")
        bldr = BasicBuilder()
        #linter warning redefining name from outer scope
        stime = time()
        msg = bldr.encode(self.name,self.group,text) #, str(stime)
        self._clt.send(bytes(msg, "utf-8"))
        response = self._clt.recv(2048)
        if len(response) <= 0:
            return False
        rtime = time()
        name,group,text = bldr.decode(response.decode("utf-8")) #,time1
        #print("recieved back "+ time1 + " at " + str(rtime))

        print("Total response time: " + str(rtime - stime))
        print("\n\n")
        return True

    def groups(self):
        '''
        linter warning: Missing method docstring
        todo
        '''
        #unnecessary pass statement

    def get_msgs(self):
        '''
        linter warning: Missing method docstring
        todo 
        '''
        #unnecessary pass statement

if __name__ == '__main__':
    #clt = BasicClient("frida_kahlo","192.168.1.201",2000)
    clt = BasicClient("frida_kahlo","127.0.0.1",2000)
    while True:
        m = input("enter message: ")

        #linter warning: merge comparisons
        #unnecessary else after break

        if m in ('', 'exit'):
            break

        total_requests = int(input("enter number of times: "))
        successful_requests = 0
        failed_requests = 0
        timestart = time()
        total_requests = 2000 if total_requests<0 or total_requests >2000 else total_requests
        for _ in range(total_requests):
            if clt.send_msg(m):
                successful_requests += 1
            else:
                failed_requests += 1
        timeend = time()

        print("Successful Requests:", successful_requests)
        print("Failed Requests:", failed_requests)
        print("Failure Rate:", (failed_requests / total_requests) * 100, "%")
        print("Throughput:", successful_requests / (timeend - timestart), "requests per second")

