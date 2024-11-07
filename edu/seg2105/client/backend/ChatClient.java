// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  /**
   * The client's login ID
   */
  private String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      if (message.startsWith("#")){
    	  handleCommand(message);
    	  
      } else {
		  sendToServer(message);
      }
    		  
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String message) throws IOException {
	  if (message.equals("#quit")) {
		  quit();
		  
	  } else if (message.equals("#logoff")) {
		  closeConnection();
		  
	  } else if (message.startsWith("#sethost")) {
		  if (!isConnected()) {
			  int hostStart = message.indexOf("<") + 1;
			  int hostEnd = message.indexOf(">");
			  
			  //Check if host inputted as part of command 
			  if ((hostStart > 0) && (hostEnd > 0) && (hostStart < hostEnd)) {
				  String host = message.substring(hostStart, hostEnd); 
				  setHost(host);
				  clientUI.display("Host set to " + host);

			  } else {
				  clientUI.display("ERROR Invalid format, Follow #setHost<12345>");
			  }
		  } else {
			  clientUI.display("ERROR Cannot change host while connected");
		  }
		  
	  } else if (message.startsWith("#setport")) {
		  if (!isConnected()) {
			  int portStart = message.indexOf("<") + 1;
			  int portEnd = message.indexOf(">");
			  
			  //Check if port inputted as part of command 
			  if ((portStart > 0) && (portEnd > 0) && (portStart < portEnd)) {
				  int port = Integer.parseInt(message.substring(portStart, portEnd));
				  setPort(port);
				  clientUI.display("Port set to " + port);

			  } else {
				  clientUI.display("ERROR Invalid format, Follow #setPost<12345>");
			  }
		  } else {
			  clientUI.display("ERROR Cannot change post while connected");
		  }
		  
	  } else if (message.equals("#login")) {
		  if (!isConnected()) {
			  openConnection();
		  } else {
			  clientUI.display("ERROR: Client already logged in");
		  }
		  
	  } else if (message.equals("#gethost")) {
		  clientUI.display(getHost());
	  } else if (message.equals("#getport")) {
		  clientUI.display(Integer.toString(getPort()));
	  } else {
	      clientUI.display("ERROR: Invalid Command");
	  }
  }
  
  /**
   * Method called after the connection has been established.
   * It sends the login message to the server.
   */
  @Override
  protected void connectionEstablished() {
    try {
      sendToServer("#login " + loginID);
    } catch (IOException e) {
      clientUI.display("ERROR: Failed to send login ID to server.");
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
	/**
	 * Method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override
	protected void connectionException(Exception exception) {
	  clientUI.display("The server has shut down");
	  quit();
	}
  
	/**
	 * Method called after the connection has been closed. The default
	 * implementation does nothing. 
	 */
	protected void connectionClosed() {
		clientUI.display("Connection closed");
	}
}
//End of ChatClient class
