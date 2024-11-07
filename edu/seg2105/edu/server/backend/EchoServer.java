package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
	//Instance variables
	ChatIF serverUI;
	
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
    
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
  
  public void handleMessageFromServer (String msg) throws IOException {
	  if (msg.startsWith("#")) {
		  handleCommand(msg);
	  } else {
		  serverUI.display(msg.toString());
		  this.sendToAllClients("SERVER MSG> " + msg);
	  }

  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
	/**
	 * Method called each time a new client connection is
	 * accepted. Displays a message indicating the connection
	 * @param client the connection connected to the client.
	 */
  @Override
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("Client " + client + " connected");
	}

	/**
	 * Method called each time a client disconnects.
	 * Displays a message indicating the disconnected
	 * may be overridden by subclasses but should remains synchronized.
	 *
	 * @param client the connection with the client.
	 */
	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("Client " + client + " disconnected");
	}
  
	private void handleCommand(String msg) throws IOException{
		if (msg.equals("#quit")) {
			this.close();
			System.exit(0);
			
		} else if (msg.equals("#stop")) {
			this.stopListening();
			
		} else if (msg.equals("#close")) {
			this.close();

		} else if (msg.startsWith("#setport")) {
			
			
		} else if (msg.equals("#start")) {
			
		} else if (msg.equals("#getport")) {
			
		} else {
			
		}
	}
  
  //Class methods ***************************************************
  
}
//End of EchoServer class
