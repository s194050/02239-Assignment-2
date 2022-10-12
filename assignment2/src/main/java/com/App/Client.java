import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client
{
    public static void main( String[] args ) throws MalformedURLException, RemoteException, NotBoundException
    {
        ClientToPrinter printer = (ClientToPrinter) Naming.lookup("rmi://localhost:1099/HelloPrinter"); 
        System.out.println("----" + printer.echo(" Server") + " " + printer.getClass().getName());
    }
}
