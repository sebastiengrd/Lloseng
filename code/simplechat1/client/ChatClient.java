// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  int loginId;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(int loginId, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.loginId = loginId;
    this.clientUI = clientUI;
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
   *  This method handle when the connection with the server closes
   */
  public void connectionClosed() {
    clientUI.display("Connection with server closed");
  }

  /**
   * This method overrides the one in the superclass. Called
   * when an exception occures when connecting to the server.
   * @param exception
   */
  public void connectionException(Exception exception) {
    clientUI.display("An exception occured when trying to connect with server");
    quit();
  }


  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    if(message.length()>0 && message.charAt(0) == '#'){
      
      if(message.equals("#quit")) {
        quit();
      } else if(message.equals("#logoff")) {
        try {
          closeConnection();
        } catch (IOException e) {
          clientUI.display("Exception occured. Cannot logoff");
        }
      } else if(message.equals("#login")) {
        try {
          openConnection();
        } catch (IOException e) {
          clientUI.display("Exception occured. Cannot login");
        }
      } else if(message.equals("#gethost")) {
        clientUI.display("current host is : " + getHost());
      } else if(message.equals("#getport")) {
        clientUI.display("current port is : " + getPort());
      } else {
        String[] parts = message.split(" ");
        if (parts.length == 2 && parts[0].equals("#sethost")) {
          setHost(parts[1]);
        } else if(parts.length == 2 && parts[0].equals("#setport")){
          try {
            setPort(Integer.parseInt(parts[1]));
          } catch (NumberFormatException e) {
            clientUI.display("Please enter a valid port number");
          }
        } else {
          clientUI.display("Unknown command");
        }
      }


    } else {
      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display
          ("Could not send message to server.  Terminating client.");
        quit();
      }
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
}
//End of ChatClient class
