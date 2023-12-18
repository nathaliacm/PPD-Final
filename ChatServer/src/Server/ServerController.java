package Server;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import Service.MOMService;

public class ServerController implements ServerInterface {
	private Registry registry = null;
	private Remote remoteObject = null;
	MOMService service = new MOMService();

	public ServerController() {
		try {
			service.start();

			registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			remoteObject = UnicastRemoteObject.exportObject(this, 0);
			registry.bind("Server", remoteObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void createQueue(String queueName) throws RemoteException {
		try {
			service.createQueue(queueName);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void saveMessage(String queueName, String message) throws RemoteException {
		try {
			MessageProducer producer = service.createProducer(queueName);
			TextMessage textMessage = service.createTextMessage(message);
			producer.send(textMessage);
			service.deleteProducer(producer);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public List<String> getMessagesFromQueue(String queueName) throws RemoteException {
		List<String> messages = new ArrayList<>();
		try {
			MessageConsumer consumer = service.createConsumer(queueName);
			messages = service.getMessagesFromQueue(queueName, consumer);
			service.deleteConsumer(consumer);

		} catch (JMSException e) {
			e.printStackTrace();
		}
		return messages;
	}

	public String getQueuesNames() {
		try {
			List<String> queuesNames = service.getQueuesNames();
			StringBuilder names = new StringBuilder();
			queuesNames.forEach(queue -> {
				names.append(queue + "\n");
			});
			return names.toString();

		} catch (JMSException e) {
			e.printStackTrace();
			return "";
		}
	}
}
