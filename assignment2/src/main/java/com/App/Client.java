package com.App;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.Domain.Printer;

public class Client
{
    public static void main( String[] args ) throws MalformedURLException, RemoteException, NotBoundException
    {
        String[] printers = {"Printer1", "Printer2", "Printer3", "Printer4", "Printer5"};
        Scanner scanner = new Scanner(System.in);
        ClientToPrinter client1 = (ClientToPrinter) Naming.lookup("rmi://localhost:1099/ClientToPrinter"); 
        Boolean run = true;
        int selection;
        int job = 0;

        for(int i = 0; i < printers.length; i++){ // Add printers
            client1.addPrinter(printers[i]);
        }
        
        System.out.println("Welcome to the print server \n");
        while(run){
            System.out.print("Server Options: \n \t\t 1: Start Server \t\t\t 2: Stop Server \t\t\t 3: Restart Server \n" +
                    "Printer Functions: \n \t\t 4: Print file \n \t\t 5: Print the job queue of a specific printer \n" +
                    "\t\t 6: Move a job on a specfic printer to the top of the queue \nConfig Options: \n" +
                    "\t\t 7: Read a config parameter \n \t\t 8: Set a config parameter \n9: Exit Server GUI \n");


            selection = Integer.parseInt(scanner.next() + scanner.nextLine());
            
            switch (selection){
                case 1:
                    client1.Start();
                    break;
                case 2:
                    client1.Stop();
                    break;
                case 3:
                    client1.Restart();
                    break;
                case 4:
                    getAvailablePrinters(client1, scanner); // Get available printers
                    System.out.println("Enter the name of the printer you want to print on: ");


                    String printer = scanner.next() + scanner.nextLine();
                    if (client1.getPrinter(printer) == null){
                        System.out.println("Printer does not exist, try again");
                        break;
                    }

                    System.out.println("Enter the name of the file you want to print: ");
                    
                    String filename = scanner.next() + scanner.nextLine();

                    System.out.println(client1.print(filename, printer));
                    break;
                case 5:
                    getAvailablePrinters(client1, scanner); // Get available printers
                    System.out.println("Enter the name of the printer you want to see the job queue of: ");
                    printer = scanner.next() + scanner.nextLine();

                    if (client1.getPrinter(printer) == null){
                        System.out.println("Printer does not exist, try again");
                        break;
                    }
                    
                    System.out.println(client1.queue(printer));
                    break;
                case 6:
                    getAvailablePrinters(client1, scanner); // Get available printers
                    System.out.println("Enter the name of the printer you want to change the job queue of: ");
                    printer = scanner.next() + scanner.nextLine();
                    if (client1.getPrinter(printer) == null){
                        System.out.println("Printer does not exist, try again");
                        break;
                    }

                    getAvailableJobs(client1, scanner, printer); // Get available jobs
                    System.out.println("Enter the job number you want to move to the top of the queue: ");
                    try {job = Integer.parseInt(scanner.next() + scanner.nextLine());} catch (NumberFormatException e) {
                        System.out.println("Invalid input, try again");
                        break;
                    }
                    
                    if(client1.getJobID(job, printer) == -1){
                        System.out.println("Job does not exist, try again");
                        break;
                    }


                    System.out.println(client1.topQueue(printer, job));
                    break;
                case 7:
                    System.out.println("Enter the name of the config parameter you want to read: ");
                    String parameter = scanner.next() + scanner.nextLine();
                    System.out.println(client1.readConfig(parameter));
                    break;
                case 8:
                    System.out.println("Enter the name of the config parameter you want to set: ");
                    parameter = scanner.next() + scanner.nextLine();
                    System.out.println("Enter the value you want to set the config parameter to: ");
                    String value = scanner.next() + scanner.nextLine();
                    System.out.println(client1.setConfig(parameter, value));
                    break;
                case 9:
                    run = false;
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

}
