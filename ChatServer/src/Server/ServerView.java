package Server;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;

import java.awt.Font;

public class ServerView {
	ServerController controller = new ServerController();

	private JFrame frame;
	private JTextPane textPane;
	
	public void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					frame.setVisible(true);
					threadd();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel queuesLabel = new JLabel("Filas");
		queuesLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		queuesLabel.setBounds(76, 60, 180, 18);
		frame.getContentPane().add(queuesLabel);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(76, 110, 330, 300);
		frame.getContentPane().add(textPane);
	}
	
	public void threadd() {
		(new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				while (true) {
					try {
						Thread.sleep(7000);
						String panelContentQ = controller.getQueuesNames();
						textPane.setText(panelContentQ);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		}).execute();
	}
}
