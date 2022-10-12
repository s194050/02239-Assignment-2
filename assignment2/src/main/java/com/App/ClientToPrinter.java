import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientToPrinter extends Remote{ // Client to Printer interface
    public String echo(String input) throws RemoteException;

    
    
}


