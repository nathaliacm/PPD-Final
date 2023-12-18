import javax.jms.JMSException;

import Server.ServerView;

public class Main {
	public static void main(String[] args) throws JMSException {	
		ServerView view = new ServerView();
		view.start();	
	}
}
