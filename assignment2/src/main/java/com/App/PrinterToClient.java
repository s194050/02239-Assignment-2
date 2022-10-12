package com.App;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import com.Domain.Pair;


public class PrinterToClient extends UnicastRemoteObject implements ClientToPrinter { // Printer to Client interface
    private static final long serialVersionUID = 1L; // Serial version UID
    private String name; // Name of the printer
    private String status = "Idle"; // Status of the printer

    private List<Pair> queue = new ArrayList<>(); // Queue for storing jobs


    public PrinterToClient(String name) throws RemoteException {
        super(); // Call to UnicastRemoteObject constructor
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
    

