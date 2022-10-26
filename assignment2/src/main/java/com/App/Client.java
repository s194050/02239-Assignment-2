package com.App;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client
{
    public static void main( String[] args ) throws MalformedURLException, RemoteException, NotBoundException
    {
        ClientToPrinter client1 = (ClientToPrinter) Naming.lookup("rmi://localhost:1099/ClientToPrinter"); 
        System.out.println("----" + client1.echo(" Server") + " " + client1.getClass().getName());

    }


}
