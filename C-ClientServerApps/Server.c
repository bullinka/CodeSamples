/*
Name: Karen Bullinger
Program no. 1
Program Title: Client Application
Course/Term: CSCE 386, Fall 2015
Date: 9/22/2015
Language, Compiler: C, Cygwin
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
#define PORT 5432
#define SIZE 256

int createSocketAndListen();
int acceptConnections();
int communicate();


char msg[SIZE];	
struct sockaddr_in serv;
struct sockaddr_in incoming;	
socklen_t incoming_len;

int main(int argc, char *argv[]){
	
	int listener;
	
	if((listener=createScoketAndListen()) == -1){
		return 1;
	}
	if(acceptConnections(listener) == -1)
	{
		return 1;
	}
	return 0;
}

/*
Creates socket. Binds socket to port and sets up residual connection settings.
Begins listening.  If any step fails, returns -1.  If all steps succeed
returns 1;
*/
int createScoketAndListen(){
	int listening;
	listening = socket(AF_INET, SOCK_STREAM, 0);

	if(listening == -1){
		perror("Could not create socket:");
		return -1;
	}
	
	//clears server address (WHY?)
	memset(serv.sin_zero, sizeof(serv.sin_addr), 0);
	
	serv.sin_family = AF_INET;
	
	//allows any IP To connect
	serv.sin_addr.s_addr = htons(INADDR_ANY);
	
	//listen on PORT for connection attempts
	serv.sin_port = htons(PORT);
	
	if(bind(listening, (struct sockaddr*)&serv, sizeof(serv)) == -1 ){
		perror("Error. Could not bind.");
		return -1;
	}
	
	if(listen(listening, 5) == -1)
	{
		perror("Error listening: ");
		return 1;
	}
	
	return listening; //socket identifier
}

/*
Accepts connects from clients. Once a connection is accepted,
calls, communicate();  After a client disconnects, closes socket.
*/
int acceptConnections(int list){
	int currentConnections;
	while(1){
		printf("Waiting for connections...\n");
		currentConnections = accept(list, (struct sockaddr*) &incoming, &incoming_len);
		if(currentConnections == -1)
		{
			perror("Error accepting:");
			return -1;
		}
		else communicate(currentConnections);
		close(currentConnections);
	}	
}

/*
Facilitates communication between client and server.  If client disconnects,
prints notification method and returns to calling location.
*/

int communicate(int currentConnection){
	int connected;		
		while(1){
			memset( msg, 0, sizeof(msg));
			connected = recv(currentConnection, msg, SIZE, 0);
			if( connected <= 0 ){
				printf("Client disconnected.\n");
				break;
			}
			else if( connected > 0){
				printf("%s", msg);
				send(currentConnection, msg, connected, 0);
			}
		}
		return 0;
}