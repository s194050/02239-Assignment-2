import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Hello world!
 *
 */
public class Client
{
    public static void main( String[] args ) throws MalformedURLException, RemoteException, NotBoundException
    {
        HelloPrinter service = (HelloPrinter) Naming.lookup("rmi://localhost:1099/HelloPrinter");
        System.out.println("----" + service.echo(" Server") + " " + service.getClass().getName());
    }
}
