'''
linter warning: Missing module docstring

This is a server code which listens to the client code
'''

import socket
import threading
from time import time
import sys
import os

#analyser error: unable to import 'builder'
#add path variable
sys.path.append(os.path.join(os.getcwd().rsplit('/',1)[0], 'payload'))

from builder import BasicBuilder


#linter warning: Class inherits from object, can be removed in python 3
class BasicServer():
    '''
    linter warning: Missing class docstring
    Server class to listen to client
    '''
    def __init__(self, ipaddr, port=2000):
        self.ipaddr = ipaddr
        self.port = port
        self._svr = None
        self.good = True

        #pylint warning : unnecessary elif after raise
        if self.ipaddr is None:
            raise ValueError("IP address is missing or empty")
        if self.port is None:
            raise ValueError("port number is missing")
        if self.port <=1024:
            raise ValueError(f"port number ({port}) must be above 1024")

    def __del__(self):
        '''close socket'''
        self.stop()

    def stop(self):
        '''
        linter warning: Missing method docstring
        closes the connection from client if client is lost
        '''
        self.good = False
        if self._svr is not None:
            self._svr.close()
            self._svr = None

    def run(self):
        '''
        linter warning: Missing method docstring
        server listens for client connection
        '''
        addr = (self.ipaddr,self.port)
        if socket.has_dualstack_ipv6():
            self._svr = socket.create_server(addr, family=socket.AF_INET6, dualstack_ipv6=True)
        else:
            self._svr = socket.create_server(addr)
        self._svr.listen(10)

        print(f"server ({self.ipaddr}) is listening on {self.port}")

        while self.good:
            cltconn, caddr = self._svr.accept()
            print(f"Connection from {caddr[0]}")
            csession = SessionHandler(cltconn,caddr)
            csession.start()

# ----------------------------------------------

class SessionHandler(threading.Thread):
    '''
    linter warning: Missing class docstring
    Session class to start a session for a client when client connectio is established
    '''
    def __init__(self,client_connection, client_addr):
        threading.Thread.__init__(self)
        self.daemon = False
        self._cltconn = client_connection
        self._cltaddr = client_addr
        self.good = True

    def __del__(self):
        self.close()

    def close(self):
        '''
        linter warning: Missing method docstring
        closes the connection from client if client is lost
        '''
        if self._cltconn is None:
            return
        #unnecessary else after break
        self._cltconn.close()
        self._cltconn = None
        self.good = False

    def process(self,raw, time2):
        '''
        linter warning: Missing method docstring
        recives and processes data from and client
        '''
        try:
            #print("in try")
            bldr = BasicBuilder()
            #print("build init")
            name,group,text = bldr.decode(raw) #,time1
            print(f"from {name}, to group: {group}, text: {text} ,recvd at {str(time2)}") # sent at {time1} 
        #unused variable e
        #Catching too general exception
        except Exception:
            pass

    def run(self):
        while self.good:
            try:
                buf = self._cltconn.recv(2048)
                #print(len(buf))
                if len(buf) <= 0:
                    self.good = False
                else:
                    #print("in else")
                    self.process(buf.decode("utf-8"), time())
                    self._cltconn.sendall(buf)
            #Catching too general exception
            except Exception as e:
                print(e)
                self.good = False

        print(f"clossing session {self._cltaddr}")


if __name__ == '__main__':
    svr = BasicServer("127.0.0.1", 2000)
    svr.run()
