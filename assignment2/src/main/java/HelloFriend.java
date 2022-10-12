import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloFriend extends UnicastRemoteObject implements HelloPrinter {
    private static final long serialVersionUID = 1L;
    private String name;
    public HelloFriend(String name) throws RemoteException {
        super();
        this.name = name;
    }
    public String echo(String input) throws RemoteException {
        return "Hello " + input + " from " + name;
    }
}
    

