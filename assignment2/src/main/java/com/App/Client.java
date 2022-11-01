package com.App;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client
{
    public static void main( String[] args ) throws MalformedURLException, RemoteException, NotBoundException, InterruptedException {
        String[] printers = {"Printer1", "Printer2", "Printer3", "Printer4", "Printer5"}; // List of printers
        ClientToPrinter client1 = (ClientToPrinter) Naming.lookup("rmi://localhost:1099/ClientToPrinter"); // Connect to server
        boolean run = true; // Used to keep the GUI running
        boolean loggedIn = false; // Keeps track of whether the user is logged in or not
        boolean serverStatus = false; // Simple flag to hanldes server status

        Scanner scanner = new Scanner(System.in);
        int selection;
        int job = 0;
        String printer = "";
        String parameter = "";

        for(int i = 0; i < printers.length; i++){ // Add printers
            client1.addPrinter(printers[i]);
        }


        while(run){ // Run the main print functions of the server
            while(!loggedIn){// Handle the login process
                System.out.println("Welcome to the print server \n 1: Login \n 2: Create user \n 3: Exit");

                selection = Integer.parseInt(scanner.next() + scanner.nextLine()); // Get the user input

                switch(selection){ // Handle the selection
                    case 1:
                        System.out.println("Enter username");
                        String username = scanner.next() + scanner.nextLine();
                        System.out.println("Enter password");
                        String password = scanner.next() + scanner.nextLine();
                        System.out.println(client1.login(username, password));
                        if(client1.login(username, password).equals("Login successful" + "\n")){ // If the login was successful, allow access to the printserver
                            loggedIn = true;
                        }// Otherwise break, and allow the user to try again
                        break;
                    case 2:
                        System.out.println("Enter username");
                        username = scanner.next() + scanner.nextLine();
                        System.out.println("Enter password");
                        password = scanner.next() + scanner.nextLine();
                        System.out.println(client1.createUser(username, password)); // Create user
                        break;
                    case 3:
                        // Hard exit the program
                        run = false;
                        loggedIn = true;
                        System.out.println("Thanks for using the print server");
                        break;
                }
            }
            if(run == false){ // If the user has exited the program, break the loop
                break;
            }

            System.out.print("Server Options: \n \t\t 1: Start Server \t\t\t 2: Stop Server \t\t\t 3: Restart Server \n" +
                    "Printer Functions: \n \t\t 4: Print file \n \t\t 5: Print the job queue of a specific printer \n" +
                    "\t\t 6: Move a job on a specfic printer to the top of the queue \nConfig Options: \n" +
                    "\t\t 7: Read a config parameter \n \t\t 8: Set a config parameter \n9: Exit Server GUI \n");


            selection = Integer.parseInt(scanner.next() + scanner.nextLine()); // Get the user input

            switch (selection){ // Handle the selection
                case 1:
                    System.out.println(client1.Start()); // Start the server
                    serverStatus = true;
                    break;

                case 2:
                    System.out.println(client1.Stop()); // Stop the server
                    serverStatus = false;
                    break;

                case 3:
                    System.out.println("Restarting server"); // Restart the server
                    System.out.println(client1.Restart());
                    serverStatus = true;
                    break;

                case 4:
                    if(serverStatus){
                        if(!checkSession(client1)){ // Check if the session is still valid
                            loggedIn = false;
                            break;
                        }
                        
                        getAvailablePrinters(client1, scanner); // Get available printers
                        System.out.println("Enter the name of the printer you want to print on: ");

                        printer = scanner.next() + scanner.nextLine();
                        if (client1.getPrinter(printer) == null){ // If the printer doesn't exist, break
                            System.out.println("Printer does not exist, try again");
                            break;
                        }

                        System.out.println("Enter the name of the file you want to print: ");

                        String filename = scanner.next() + scanner.nextLine();

                        System.out.println(client1.print(filename, printer));
                        break;
                    }else{
                        System.out.println("Server is not running");
                        break;
                    }

                case 5:
                if(serverStatus){
                    if(!checkSession(client1)){ // Check if the session is still valid
                        loggedIn = false;
                        break;
                    }
                    getAvailablePrinters(client1, scanner); // Get available printers
                    System.out.println("Enter the name of the printer you want to see the job queue of: ");
                    printer = scanner.next() + scanner.nextLine();

                    if (client1.getPrinter(printer) == null){ // Check if the printer exists
                        System.out.println("Printer does not exist, try again");
                        break;
                    }

                    System.out.println(client1.queue(printer));
                    break;
                }else{
                    System.out.println("Server is not running");
                    break;
                }
                case 6:
                if(serverStatus){
                    if(!checkSession(client1)){ // Check if the session is still valid
                        loggedIn = false;
                        break;
                    }
                    getAvailablePrinters(client1, scanner); // Get available printers
                    System.out.println("Enter the name of the printer you want to change the job queue of: ");
                    printer = scanner.next() + scanner.nextLine();
                    if (client1.getPrinter(printer) == null){ // Check if the printer exists
                        System.out.println("Printer does not exist, try again");
                        break;
                    }

                    getAvailableJobs(client1, scanner, printer); // Get available jobs
                    System.out.println("Enter the job number you want to move to the top of the queue: ");
                    try {job = Integer.parseInt(scanner.next() + scanner.nextLine());} catch (NumberFormatException e) {
                        System.out.println("Invalid input, try again"); // If not an integer allow user to try again
                        break;
                    }

                    if(client1.getJobID(job, printer) == -1){
                        System.out.println("Job does not exist, try again");
                        break;
                    }


                    System.out.println(client1.topQueue(printer, job));
                    break;
                }else{
                    System.out.println("Server is not running");
                    break;
                }
                case 7:
                if(serverStatus){
                    getAvailableParameters(client1, scanner);
                    System.out.println("Enter the name of the config parameter you want to read: ");
                    parameter = scanner.next() + scanner.nextLine();
                    System.out.println(client1.readConfig(parameter));
                    break;
                }else{
                    System.out.println("Server is not running");
                    break;
                }
                case 8:
                if(serverStatus){
                    getAvailableParameters(client1, scanner);
                    System.out.println("Enter the name of the config parameter you want to set: \n " + 
                    "If you want to add a new parameter, enter the name of the parameter you want to add");
                    parameter = scanner.next() + scanner.nextLine();
                    System.out.println("Enter the value you want to set the config parameter to: ");

                    String value = scanner.next() + scanner.nextLine();

                    System.out.println(client1.setConfig(parameter, value));
                    break;
                }else{
                    System.out.println("Server is not running");
                    break;
                }
                case 9:
                    run = false;
                    System.out.println("Thanks for using the print server");
                    break;
                default:
                    System.out.println("Invalid selection");
                    break;
            }
        }
    }


    public static void getAvailablePrinters(ClientToPrinter client, Scanner scanner) throws MalformedURLException, RemoteException, NotBoundException{
        // Function to output all available printers
        String available;
        String printers = "";
        System.out.println("Do you want a list of available printers? [y/n]");
        available = scanner.nextLine();
        if(available.equals("y")){
            printers = client.getPrinters();
        }
        System.out.println(printers);
    }

    public static void getAvailableJobs(ClientToPrinter client, Scanner scanner, String printerName) throws MalformedURLException, RemoteException, NotBoundException{
        // Function to output all available jobs
        String available;
        String jobs = "";
        System.out.println("Do you want a list of available jobs? [y/n]");
        available = scanner.nextLine();
        if(available.equals("y")){
            jobs = client.queue(printerName);
        }
        System.out.println(jobs);
    }

    public static void getAvailableParameters(ClientToPrinter client, Scanner scanner) throws MalformedURLException, RemoteException, NotBoundException{
        // Function to output all available parameters
        String available;
        String parameters = "";
        System.out.println("Do you want a list of available parameters? [y/n]");
        available = scanner.nextLine();
        if(available.equals("y")){
            parameters = client.getParameters();
        }
        System.out.println(parameters);
    }

    public static boolean checkSession(ClientToPrinter client1) throws MalformedURLException, RemoteException, NotBoundException{
        if(!client1.checkSession()){// Validate the session
            System.out.println("Session expired, please login again");
            return false;
        } 
        return true;
    }

}
