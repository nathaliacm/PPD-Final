package Client;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import Server.ServerInterface;

public class ClientController implements ClientInterface {
	
	public String queueName = "";
	public boolean isOn = false;
	
	private Registry registry;
	private Remote remoteObject;
	
	private ServerInterface server;
	public Consumer<String> consumer;
	
	public List<String> contactsList = new ArrayList<>();
	
	public ClientController() {
		try {
			registry = LocateRegistry.getRegistry();
			remoteObject = UnicastRemoteObject.exportObject(this, 0);
			server = (ServerInterface) registry.lookup("Server");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendMessage(String message) throws RemoteException {
		consumer.accept(message + "\n");
	}

	public void changeStatusUser() {
		if (isOn) {
			try {
				registry.unbind(queueName);
				isOn = false;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				registry.rebind(queueName, remoteObject);
				isOn = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	

	public void sendMessageToUser(String user, String message) throws RemoteException {
		try {
			ClientInterface client = (ClientInterface) registry.lookup(user);
			client.sendMessage(queueName + ": " + message);

		} catch (Exception e) {
			sendMessageToServer(user, (queueName + ": " + message));

		}

	}

	public void sendMessageToServer(String user, String message) {
		try {
			server.saveMessage(user, message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void createUserQueueAtServer() {
		try {
			server.createQueue(queueName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public List<String> getUserMessagesAtServer() {
		try {
			return server.getMessagesFromQueue(queueName);
		} catch (RemoteException e) {
			List<String> messagesError = new ArrayList<>();
			return messagesError;
		}
	}

}
