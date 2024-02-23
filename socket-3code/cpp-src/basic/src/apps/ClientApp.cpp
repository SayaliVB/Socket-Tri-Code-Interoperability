#include <iostream>
#include <iomanip>
#include <sstream>
#include <thread>
#include <string>
#include <chrono>

#include "socket/client.hpp"

int main(int argc, char **argv) {
    basic::BasicClient clt;
    clt.connect();

    auto start_time = std::chrono::high_resolution_clock::now();

    for(int i = 0; i < 500; i++) {
        std::stringstream msg;
        msg << "hello. My name is Hritik" << std::ends;
        clt.sendMessage(msg.str());
    }

    auto end_time = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end_time - start_time);

    // Convert duration to seconds (floating-point)
    double seconds = duration.count() / 1000000.0;

    // Calculate requests per second
    double requests_per_second = 500 / seconds;

    std::cout << "Throughput: " << requests_per_second << " req/sec" << std::endl;
     
    std::cout << "Sleeping a bit before exiting..." << std::endl;
    std::this_thread::sleep_for(std::chrono::milliseconds(2000));

    return 0;
}
