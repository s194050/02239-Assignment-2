import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientToPrinter extends Remote{
    public String echo(String input) throws RemoteException;

    
    
}


