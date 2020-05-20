This project uses Java SE 13.

Below you will find information on how to compile and run everything.

On the root directory of the project you will find 4 scripts: compile.sh, peer.sh, test.sh and cleanup.sh

Build the project
./compile.sh
This script creates a build/ directory and compiles all project files to it

Run rmiregistry
Inside the build/ directory, run rmiregistry

Start the Server
To start the server, run the server.sh script inside the project's root directory. Usage is as follows:
./server.sh <port>

Start a Peer
To start a peer, run the peer.sh script inside the project's root directory. Usage is as follows:
./peer.sh <svc_access_point> <port> <server_addr> <server_port>

Start the Test App
To start the Test App, run the test.sh script inside the project's root directory. Usage is as follows:
./test.sh <peer_ap> BACKUP|RESTORE|DELETE|RECLAIM|STATE [<opnd_1>] [<opnd_2]
For example, to start the BACKUP protocol for a test.txt file on peer 3 with a replication degree of 2 run:
./test.sh 3 BACKUP test.txt 2