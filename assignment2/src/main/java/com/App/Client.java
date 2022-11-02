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
        String username = "";
        String password = "";
        String printer = "";
        String parameter = "";
        String output = "";

        for (int i = 0; i < printers.length; i++) { // Add printers
            client1.addPrinter(printers[i]);
        }

        while (run) { // Run the main print functions of the server
            try {
            while (!loggedIn) {// Handle the login process
                System.out.println("Welcome to the print server \n 1: Login \n 2: Exit");
                    selection = Integer.parseInt(scanner.next() + scanner.nextLine()); // Get the user input

                    switch(selection){ // Handle the selection
                        case 1:
                            System.out.println("Enter username");
                            username = scanner.next() + scanner.nextLine();
                            System.out.println("Enter password");
                            password = scanner.next() + scanner.nextLine();
                            String outputOfLogin = client1.login(username, password);
                            if (outputOfLogin.equals("Login successful" + "\n")) { // If the login was successful, allow access to the printserver
                                System.out.println(outputOfLogin);
                                loggedIn = true;
                            } // Otherwise break, and allow the user to try again
                            else {
                                System.out.println(outputOfLogin);
                            }
                            break;
                        case 2:
                            // Hard exit the program
                            run = false;
                            System.out.println("Thanks for using the print server");
                            System.exit(0);
                    }
                }
                if (run == false) { // If the user has exited the program, break the loop
                    break;
                }
                
                System.out.print("Server Options: \n \t\t 1: Start Server \t\t\t 2: Stop Server \t\t\t 3: Restart Server \n" +
                        "Printer Functions: \n \t\t 4: Print file \n \t\t 5: Print the job queue of a specific printer \n" +
                        "\t\t 6: Move a job on a specfic printer to the top of the queue\n" +
                        "\t\t 7: Get status of a printer\n Config Options: \n \t\t 8: Read a config parameter\n" +
                        "\t\t 9: Set a config parameter \n10: Exit Server GUI \n");

                if (username.equals("root")) { // If the user is root, allow them to create users
                    System.out.println("11: Create a user");
                }

                selection = Integer.parseInt(scanner.next() + scanner.nextLine()); // Get the user input

                switch (selection) { // Handle the selection
                    case 1:
                        if (!serverStatus) {
                            System.out.println(client1.Start()); // Start the server
                            serverStatus = true;
                            break;
                        } else {
                            System.out.println("Server has already started!\n");
                            break;
                        }
                    case 2:
                        if (serverStatus) {
                            client1.Stop(); // Stop the server
                            serverStatus = false;
                            break;
                        } else {
                            System.out.println("Server is not running\n");
                            break;
                        }
                    case 3:
                        if (serverStatus) {
                            System.out.println("Restarting server\n");
                            System.out.println(client1.Restart()); // Restart the server
                            break;
                        } else {
                            System.out.println("Server is not running\n");
                            break;
                        }
                    case 4:
                        if (serverStatus) {
                            getAvailablePrinters(client1, scanner); // Get available printers
                            System.out.println("Enter the name of the printer you want to print on: ");

                            printer = scanner.next() + scanner.nextLine();
                            if (client1.getPrinter(printer) == null) { // If the printer doesn't exist, break
                                System.out.println("Printer does not exist, try again\n");
                                break;
                            }

                            System.out.println("Enter the name of the file you want to print: ");

                            String filename = scanner.next() + scanner.nextLine();

                            output = client1.print(filename, printer); // Print the file
                            if(output.equals("Session Invalid")){ // If the session is invalid, break
                                System.out.println("Session expired, please login again\n");
                                loggedIn = false;
                                break;
                            }else{
                                System.out.println(output);

                                break;
                            }
                        } else {
                            System.out.println("Server is not running\n");
                            break;
                        }

                    case 5:
                        if (serverStatus) {
                            getAvailablePrinters(client1, scanner); // Get available printers
                            System.out.println("Enter the name of the printer you want to see the job queue of: ");
                            printer = scanner.next() + scanner.nextLine();

                            if (client1.getPrinter(printer) == null) { // Check if the printer exists
                                System.out.println("Printer does not exist, try again\n");
                                break;
                            }

                            output = client1.queue(printer); // Get the job queue
                            if(output.equals("Session Invalid")){ // If the session is invalid, break
                                System.out.println("Session expired, please login again\n");
                                loggedIn = false;
                                break;
                            }else{
                                System.out.println(output);
                                break;
                            }
                        } else {
                            System.out.println("Server is not running\n");
                            break;
                        }
                    case 6:
                        if (serverStatus) {
                            getAvailablePrinters(client1, scanner); // Get available printers
                            System.out.println("Enter the name of the printer you want to change the job queue of: ");
                            printer = scanner.next() + scanner.nextLine();
                            if (client1.getPrinter(printer) == null) { // Check if the printer exists
                                System.out.println("Printer does not exist, try again\n");
                                break;
                            }

                            getAvailableJobs(client1, scanner, printer); // Get available jobs
                            System.out.println("Enter the job number you want to move to the top of the queue: ");
                            try {
                                job = Integer.parseInt(scanner.next() + scanner.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input, try again\n"); // If not an integer allow user to try again
                                break;
                            }

                            if (client1.getJobID(job, printer) == -1) {
                                System.out.println("Job does not exist, try again\n");
                                break;
                            }

                            output = client1.topQueue(printer, job); // Move the job to the top of the queue
                            if(output.equals("Session Invalid")){ // If the session is invalid, break
                                System.out.println("Session expired, please login again\n");
                                loggedIn = false;
                                break;
                            }else{
                                System.out.println(output);
                                break;
                            }
                        } else {
                            System.out.println("Server is not running\n");
                            break;
                        }
                    case 7:
                        if (serverStatus) {
                            getAvailablePrinters(client1, scanner);
                            System.out.println("Enter the name of the printer you want to check status of: ");
                            printer = scanner.next() + scanner.nextLine();
                            output = client1.status(printer); // Get the status of the printer
                            if(output.equals("Session Invalid")){ // If the session is invalid, break
                                System.out.println("Session expired, please login again\n");
                                loggedIn = false;
                                break;
                            }else{
                                System.out.println(output);
                                break;
                            }
                        } else {
                            System.out.println("Server is not running\n");
                            break;
                        }
                    case 8:
                        if (serverStatus) {
                            getAvailableParameters(client1, scanner);
                            System.out.println("Enter the name of the config parameter you want to read: ");
                            parameter = scanner.next() + scanner.nextLine();

                            output = client1.readConfig(parameter); // Read the config parameter
                            if(output.equals("Session Invalid")){ // If the session is invalid, break
                                System.out.println("Session expired, please login again\n");
                                loggedIn = false;
                                break;
                            }else{
                                System.out.println(output);
                                break;
                            }
                        } else {
                            System.out.println("Server is not running\n");
                            break;
                        }
                    case 9:
                        if (serverStatus) {
                            getAvailableParameters(client1, scanner);
                            System.out.println("Enter the name of the config parameter you want to set: \n " +
                                    "If you want to add a new parameter, enter the name of the parameter you want to add");
                            parameter = scanner.next() + scanner.nextLine();
                            System.out.println("Enter the value you want to set the config parameter to: ");

                            String value = scanner.next() + scanner.nextLine();
                            output = client1.setConfig(parameter, value); // Set the config parameter
                            if(output.equals("Session Invalid")){ // If the session is invalid, break
                                System.out.println("Session expired, please login again\n");
                                loggedIn = false;
                                break;
                            }else{
                                System.out.println(output);
                                break;
                            }
                        } else {
                            System.out.println("Server is not running\n");
                            break;
                        }
                    case 10:
                            run = false;
                            System.out.println("Thanks for using the print server");
                            System.out.println("Exiting...");
                            System.exit(0);
                    case 11:
                        if(serverStatus){
                            if (!username.equals("root")) {
                                // this error message can give information to a hacker
                                // System.out.println("You do not have permission to use this command");
                                break;
                            }
                            System.out.println("Enter username");
                            String temp_username = scanner.next() + scanner.nextLine();
                            System.out.println("Enter password");
                            String temp_password = scanner.next() + scanner.nextLine();
                            System.out.println(client1.createUser(temp_username, temp_password)); // Create user
                            break;
                        }else{
                            System.out.println("Server is not running\n");
                            break;
                        }  
                    default:
                        System.out.println("Invalid selection\n");
                        break;
                }
                }catch (NumberFormatException e){
                    System.out.println("Invalid input, try again\n");
                }
        }
    }


    public static void getAvailablePrinters(ClientToPrinter client, Scanner scanner) throws MalformedURLException, RemoteException, NotBoundException {
        // Function to output all available printers
        String available;
        String printers = "";
        System.out.println("Do you want a list of available printers? [y/n]");
        available = scanner.nextLine();
        if (available.equals("y")) {
            printers = client.getPrinters();
        }
        System.out.println(printers);
    }

    public static void getAvailableJobs(ClientToPrinter client, Scanner scanner, String printerName) throws MalformedURLException, RemoteException, NotBoundException {
        // Function to output all available jobs
        String available;
        String jobs = "";
        System.out.println("Do you want a list of available jobs? [y/n]");
        available = scanner.nextLine();
        if (available.equals("y")) {
            jobs = client.queue(printerName);
        }
        System.out.println(jobs);
    }

    public static void getAvailableParameters(ClientToPrinter client, Scanner scanner) throws MalformedURLException, RemoteException, NotBoundException {
        // Function to output all available parameters
        String available;
        String parameters = "";
        System.out.println("Do you want a list of available parameters? [y/n]");
        available = scanner.nextLine();
        if (available.equals("y")) {
            parameters = client.getParameters();
        }
        System.out.println(parameters);
    }
}
