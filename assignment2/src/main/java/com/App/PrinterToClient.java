package com.App;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.Domain.Pair;
import com.Domain.Printer;


public class PrinterToClient extends UnicastRemoteObject implements ClientToPrinter { // Printer to Client interface
    private static final long serialVersionUID = 1L; // Serial version UID
    private String name; // Name of server
    private List<Printer> printers = new ArrayList<>(); // List of printers




    public PrinterToClient(String name) throws RemoteException {
        super(); // Call to UnicastRemoteObject constructor
        this.name = name; // "Server"
    }
    
    public String echo(String input) throws RemoteException { // "Client"
        return "Hello " + input + " from " + name;
    }
    
    public String print(String filename, String printer){ // Print a file
        for (Printer specficPrinter : printers) { // Loop through printers
            if (specficPrinter.getPrinterName().equals(printer)) {
                return specficPrinter.addToqueue(filename);
            }
        }
        return null; // Printer not found
    }


    public String queue(String printer) { // Get queue for a printer
        for (Printer specficPrinter : printers) { // Loop through printers
            if (specficPrinter.getPrinterName().equals(printer)) {
                return specficPrinter.queue();
            }
        }
        return null; // Printer not found
    }

    public String topQueue(String printer, int job) { //job = job number in queue to be moved to top of queue 
        for (Printer specficPrinter : printers) { // Loop through printers
            if (specficPrinter.getPrinterName().equals(printer)) {
                return specficPrinter.topQueue(job);
            }
        }
        return null; // Printer not found
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


    public void addPrinter(String printerName) { // Add a printer
        printers.add(new Printer(printerName));
    }

    public String getPrinter(String printerName) { // Get a printer
        for (Printer printer : printers) {
            if (printer.getPrinterName().equals(printerName)) {
                return printer.getPrinterName();
            }
        }
        return null;
    }

    public String getPrinters() { // Get all printers
        String printerNames = "";
        for (Printer printer : printers) {
            printerNames += printer.getPrinterName() + " ";
        }
        return printerNames;
    }

    public int getJobID(int jobNumber, String printerName) { // Get job ID
        for (Printer printer : printers) {
            if (printer.getPrinterName().equals(printerName)) {
                return printer.getJobNumber(jobNumber);
            }
        }
        return -1;
    }   
    
    

    public String createUser(String username, String password) throws RemoteException{
        try{
            // Create file if it dosen't exist. Boolean in FileWriter makes sure we append to file and don't overwrite. 
            File file = new File("password.txt");
            file.createNewFile();
            boolean name_exist = false;
            Scanner myReader = new Scanner(file);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String name = data.substring(0, data.indexOf(':'));
                if (username.equals(name)){
                    name_exist = true;
                }
            }
            myReader.close();
            
            if (!name_exist){
                FileWriter fstream = new FileWriter(file, true);
                BufferedWriter out = new BufferedWriter(fstream);
                // Write to file and make a new line. 
                try {
                    // getInstance() method is called with algorithm SHA-512
                    MessageDigest md = MessageDigest.getInstance("SHA-512");
                    // digest() method is called
                    // to calculate message digest of the input string
                    // returned as array of byte
                    byte[] messageDigest = md.digest(password.getBytes());
          
                    // Convert byte array into signum representation
                    BigInteger no = new BigInteger(1, messageDigest);
          
                    // Convert message digest into hex value
                    String hashtext = no.toString(16);
          
                    // Add preceding 0s to make it 32 bit
                    while (hashtext.length() < 32) {
                        hashtext = "0" + hashtext;
                    }
          
                    // return the HashText
                    out.write(username + ":" + hashtext);
                    out.newLine();
                    //Close the output stream
                    out.close();
                    return "Account with Username: " + username + " created successfully." + "\n";
                }
                // For specifying wrong message digest algorithms
                catch (NoSuchAlgorithmException e) {
                    out.close();
                    throw new RuntimeException(e);
                }                
            }

            }catch (Exception e){//Catch exception if any
              System.err.println("Error: " + e.getMessage());
            }
            
            return "Account with Username: " + username + " already excist." + "\n";
    }


    public String login(String username, String password) throws RemoteException{
        boolean accepted = false;
        try {
            String hash = "";
            try {
                // getInstance() method is called with algorithm SHA-512
                MessageDigest md = MessageDigest.getInstance("SHA-512");
      
                // digest() method is called
                // to calculate message digest of the input string
                // returned as array of byte
                byte[] messageDigest = md.digest(password.getBytes());
      
                // Convert byte array into signum representation
                BigInteger no = new BigInteger(1, messageDigest);
      
                // Convert message digest into hex value
                String hashtext = no.toString(16);
      
                // Add preceding 0s to make it 32 bit
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }
      
                // return the HashText
                hash = hashtext;
            }
            // For specifying wrong message digest algorithms
            catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            if(hash.equals("")){
                return "Login failed";
            }

            // Setup read file. 
            File myObj = new File("password.txt");
            Scanner myReader = new Scanner(myObj);
            
            // Read through every line. 
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              
              String name = data.substring(0, data.indexOf(':'));
              // Check if username is the one we are looking for
              if(username.equals(name)){
                String pw = data.substring(data.indexOf(':') + 1);
                // Check that the password matches. 

                if (hash.equals(pw)){
                    accepted = true;
                }
                break;
              }
              
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        if(accepted){
            return "Login successful" + "\n";
        }else{
            return "Login failed, try again" + "\n";
        }
        //return "Password: " + password + "  accepted status: " + accepted + "\n";
    }



}
    

