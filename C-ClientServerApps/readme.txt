Simple command line TCP client with echo server.

To use server:

gcc -o Server Server.c
./Server

Server set to listen on port 5432.

To use client:

gcc -o Client Client.c
./Client [ip addr to connect to] (eg. ./Client 127.0.0.1)

Client set to connect on port 5432.

