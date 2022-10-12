import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloClient extends UnicastRemoteObject implements HelloPrinter {
    private static final long serialVersionUID = 1L;
    private String name;
    public HelloClient(String name) throws RemoteException {
        super();
        this.name = name; // "Server"

    }
    public String echo(String input) throws RemoteException { // "Client"
        return "Hello " + input + " from " + name;
    }
    
    public String print(String filename, String printer){ // 
        return "Printing " + filename + " on " + printer;
    }


    public String queue(String printer) {
        return "Queueing " + printer;
        
    }

    public String topQueue(String printer, int job) { //job = job number in queue to be moved to top of queue 
        return "Top queueing " + printer + " " + job;
    }

    public void Start() { //  start the print server
        System.out.println("Starting");
        
    }

    public void Stop() { // stop the print server
        System.out.println("Stopping");
        
    }

    public void Restart() { // restart the print server
        System.out.println("Restarting");
        
    }

    public String status(String printer) { // status of the printer
        return "Status of " + printer;
        
    }

    public String readConfig(String parameter) { // read the configuration file
        return "Reading " + parameter;
        
    }

    public String setConfig(String parameter, String value) { // set a configuration parameter
        return "Setting " + parameter + " to " + value;
        
    }

}
    

