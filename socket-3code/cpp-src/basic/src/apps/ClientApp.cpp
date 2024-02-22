#include <iostream>
#include <iomanip>
#include <sstream>
#include <thread>
#include <string>

#include "socket/client.hpp"

/**
 * @brief basic starting point 
 *
 *      Author: gash
 */
int main(int argc, char **argv) {
    basic::BasicClient clt;
    clt.connect();

    std::stringstream msg;
    msg << "hello. My name is Hritik" << std::ends;
    clt.sendMessage(msg.str());
     
    std::cout << "sleeping a bit before exiting..." << std::endl;
    std::this_thread::sleep_for(std::chrono::milliseconds(2000));

    // througput cpp to cpp: no of req/timetaken = 1/30microsecs
    // througput python client cpp server= 1/104microsecs
}