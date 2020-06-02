// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.*;

// import com.lloseng.ocsf.server.ConnectionToClient;

//import com.lloseng.ocsf.server.ConnectionToClient;

import ocsf.server.*;
import common.*;


/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF clientUI) 
  {
    super(port);
    this.clientUI = clientUI;
  }

  
  //Instance methods ************************************************

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
      } else if(message.equals("#stop")) {
        sendToAllClients("WARNING - Server has stopped listening for connections.");
        stopListening();
      } else if(message.equals("#close")) {
        try {
          close();
        } catch (IOException e) {
          clientUI.display("An error corrured. Could not close.");
        }
        
      } else if(message.equals("#start")) {
        try {
          listen();
        } catch (IOException e){
          clientUI.display("Could not listen to new client");
        }
      } else if(message.equals("#getport")) {
        clientUI.display("Port is " + getPort());

      } else {
        String[] parts = message.split(" ");
        if (parts.length == 2 && parts[0].equals("#setport")) {
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
      sendToAllClients(message);
      clientUI.display(message);  
    }
  }


  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {

    System.out.println("Message received: " + msg + " from " + client + " with id " + client.getInfo("id"));

    String[] split = ((String)msg).split(" ");
    
    if(split.length == 2 && split[0].equals("#login")) {
      client.setInfo("id", split[1]);
      client.setInfo("idValidated", true);
      sendToAllClients(split[1] + " has logged on.");
    } else {
      if((boolean)client.getInfo("idValidated") == true) {
        sendToAllClients(client.getInfo("id") + " " + msg);
      } else {
        try {
          client.sendToClient("ERROR need to send id with the message #login <your_id>");
          client.close();
        } catch (IOException e) {
          System.exit(1);
        }
      }

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
   * This method overrides the one in the superclass. Called
   * when a client connects to the server.
   * @param client
   */
  public void clientConnected(ConnectionToClient client) {
    System.out.println("A new client is attempting to connect to the server.");
  }

  /**
   * This method overrides the one in the superclass. Called
   * when a client disconnects.
   * @param client
   */
  synchronized public void clientDisconnected(ConnectionToClient client) {
    System.out.println("Client " + client.getInfo("id") + " disconnected");
  }

  synchronized public void clientException(ConnectionToClient client, Throwable exception) {
    System.out.println("Client disconnected");
  }

  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  


}
//End of EchoServer class
