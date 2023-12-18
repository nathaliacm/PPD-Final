package Service;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;

public class MOMService {
	ConnectionFactory connectionFactory;
	Connection connection;
	Session session;

	public void start() {
        try {
            String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    		connectionFactory = new ActiveMQConnectionFactory(url);
    		connection = connectionFactory.createConnection();
    		connection.start();
    		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
	
	// Producer
	
	public MessageProducer createProducer(String queueName) throws JMSException {
		Destination destination = session.createQueue(queueName);
		MessageProducer producer = session.createProducer(destination);
		return producer;
	}
	
	public void deleteProducer(MessageProducer producerOrPublisher) throws JMSException {
		producerOrPublisher.close();
	}
	
	// Consumer
	
	public MessageConsumer createConsumer(String queueName) throws JMSException {
		Destination destination = session.createQueue(queueName);
		MessageConsumer client = session.createConsumer(destination);
		return client;
	}
	
	public void deleteConsumer(MessageConsumer client) throws JMSException {
		client.close();
	}
	
	// Queue
	
	public Destination createQueue(String queueName) throws JMSException {
    	Destination destination = session.createQueue(queueName);      
	    MessageProducer producer = session.createProducer(destination);
	    producer.close();
	    return destination;
    }
    
	public List<String> getMessagesFromQueue(String queueName, MessageConsumer consumer) throws JMSException {
		ActiveMQQueue queue = (ActiveMQQueue) session.createQueue(queueName);
		QueueBrowser browser = session.createBrowser((ActiveMQQueue) queue);
		Enumeration<?> enumeration = browser.getEnumeration();
		List<String> messages = new ArrayList<>();

		while (enumeration.hasMoreElements()) {
			Message message = (Message) consumer.receive();

			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				messages.add(textMessage.getText());
			}
			enumeration.nextElement();
		}
		return messages;
	}
	
	public List<String> getQueuesNames() throws JMSException {
		Set<ActiveMQQueue> queues = ((ActiveMQConnection) connection).getDestinationSource().getQueues();
		List<String> queueNames = new ArrayList<>();
		queues.forEach(fila -> {
			String nome = fila.getPhysicalName();
			queueNames.add(nome);

		});
		return queueNames;
	}

    public List<String> getMessages(String queueName, MessageConsumer consumer) throws JMSException {
		ActiveMQQueue queue = (ActiveMQQueue) session.createQueue(queueName);
		QueueBrowser browser = session.createBrowser((ActiveMQQueue) queue);
		Enumeration<?> enumeration = browser.getEnumeration();
		List<String> messages = new ArrayList<>();

		while (enumeration.hasMoreElements()) {
			Message message = (Message) consumer.receive();

			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				messages.add(textMessage.getText());
			}
			enumeration.nextElement();
		}
		return messages;
	}

    
    public void deleteQueue(String queueName) throws JMSException {
        Destination destination = session.createQueue(queueName);
        ((ActiveMQConnection) connection).destroyDestination((ActiveMQDestination) destination);
    }
    
    // Message
	
 	public TextMessage createTextMessage(String message) throws JMSException {
 		TextMessage textMessage = session.createTextMessage(message);
 		return textMessage;
 	}
}
