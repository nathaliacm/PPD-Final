package Server;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote{
	void createQueue(String userName) throws RemoteException;
	void saveMessage(String userName, String message) throws RemoteException;
	public List<String> getMessagesFromQueue(String queueName) throws RemoteException;
}
