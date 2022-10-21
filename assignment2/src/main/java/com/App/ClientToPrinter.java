package com.App;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientToPrinter extends Remote{ // Client to Printer interface
    public String echo(String input) throws RemoteException;
    public String print(String filename, String printer) throws RemoteException;
    public String queue(String printer) throws RemoteException;
    public String topQueue(String printer, int job) throws RemoteException;
    public void Start() throws RemoteException;
    public void Stop() throws RemoteException;
    public void Restart() throws RemoteException;
    public String status(String printer) throws RemoteException;
    public String readConfig(String parameter) throws RemoteException;
    public String setConfig(String parameter, String value) throws RemoteException;
    
}


