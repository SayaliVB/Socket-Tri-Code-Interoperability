import unittest
import os
import sys

#analyser error: unable to import 'builder'
#add path variable
sys.path.append(os.path.join(os.getcwd().rsplit('/',1)[0], 'payload'))

from builder import BasicBuilder

class TestBuilder(unittest.TestCase):
    def test_setUp(self):
        pass

    def test_tearDown(self):
        pass

    def test_testEncode(self):
        n = "fred"
        g = "dogs"
        t = "hello"
        #time = "1234"

        b = BasicBuilder()
        r = b.encode(n,g,t) #,time
        print(f"encoded: {r}")

        parts = b.decode(r)

        self.assertEqual(n,parts[0])
        self.assertEqual(g,parts[1])
        self.assertEqual(t,parts[2])
        #self.assertEqual(time,parts[3])

if __name__ == '__main__':
    unittest.main()
