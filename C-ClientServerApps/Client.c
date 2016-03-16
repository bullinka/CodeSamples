/*
Name: Karen Bullinger
Program no. 1
Program Title: Client Application
Course/Term: CSCE 386, Fall 2015
Date: 9/22/2015
Compiler: gcc 4.9.3
Sources:
Description: This program is a simple client intended to connect to a server application, send a short message, and receive a message
			  in response. 
To use: ./Client [IP Address of Host]
Resource:	Molly Maloney
			http://www.gta.ufrj.br/ensino/eel878/sockets/syscalls.html
			https://vcansimplify.wordpress.com/2013/03/14/c-socket-tutorial-echo-server/
*/

#include <sys/socket.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <netdb.h>
#define PORT 5432
#define SIZE 1024

int setUpHost(char *hst);
int socketConnect();

int sock = 0;
char msgBuffer [SIZE];
struct hostent *hp;
struct sockaddr_in addr;
int connected;
int sendBytes;



int main(int argc, char *argv[])
{				
	//check arguments
	if( argc != 2)
	{ 
		printf("To use: ./client [IP Address of host]\n");
		return 0;
	}
	
	if( setupHost(argv[1]) != 1){
		return 0;
	}
	
	//create socket
	if(socketConnect() != 1){
		return 0;
	}	
	
	//Send messages while still connected to the server. 
	while(1){
		printf("\nInput a message:\n");
		fgets(msgBuffer, sizeof(msgBuffer), stdin);		
		send(sock, msgBuffer, strlen(msgBuffer), 0);
		connected = recv(sock, msgBuffer, SIZE, 0);
		
		if(connected < 0 ){
			printf("Server disconnected... Exiting application");
			return 0;
		}
		printf("%s", msgBuffer);
		memset(msgBuffer, 0, sizeof(msgBuffer)); //Ensures actually receiving message back, not just reprinting previously stored value.
	}
}
/*
Sets up host information based on user input from command line.
Returns 1 if successful, 0 if not.
*/
int setupHost(char *hst) {
	hp = gethostbyname(hst);
	if(!hp){
		printf("Cannot find host... please try again.\n");
		return 0;
	} 
	else{
		bcopy(hp->h_name, (char *)&addr.sin_zero, hp->h_length);
		return 1;
	}
}

/*
Creates new socket and sets up neccessary address information for initializing connection
to server.  Connects to server. Returns 0 if either of the operations fail, returns 1 if
both complete successfully.
*/
int socketConnect(){
	if((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0 )
		{
			printf("Could not create socket.");
			return 0;
		}
		memset(addr.sin_zero, 0, sizeof(addr.sin_zero));
		addr.sin_family = AF_INET;
				//set port to connect to
		addr.sin_port = htons(PORT);
				
		if(connect(sock, ( struct sockaddr *)&addr, sizeof(addr)) == -1){
						printf("Error, could not connect. Please try again.");
			perror("error:");
			return 0;
		}
		else return 1;
}