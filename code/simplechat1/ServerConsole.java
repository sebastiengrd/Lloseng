import java.io.*;
import common.*;
import server.*;


public class ServerConsole implements ChatIF {
  //Class variables *************************************************

   /**
  * The default port to listen on.
  */
  final public static int DEFAULT_PORT = 5555;

  //Instance variables **********************************************

  EchoServer server;
  
  //Constructors ****************************************************
  
  public ServerConsole(int port) {
    
    server = new EchoServer(port, this);
    try 
    {
      server.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  //Instance methods ************************************************

  public void display(String s) {
    System.out.println("SERVER MSG> " + s);
  }

  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        server.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }
  
  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
    
    ServerConsole chat= new ServerConsole(port);

    chat.accept();  //Wait for console data
    


  }
}