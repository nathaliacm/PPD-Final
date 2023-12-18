package Client;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface  extends Remote {
	void sendMessage(String message) throws RemoteException;
}