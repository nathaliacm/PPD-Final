import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import Client.ClientView;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main {

	private JFrame frame;
	private JTextField textField;
	private JButton connectButton;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(52, 68, 343, 32);
		frame.getContentPane().add(textField);
		
		connectButton = new JButton("Conectar");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String usuario = textField.getText();
				ClientView client = new ClientView(usuario);
				client.start();
				frame.dispose();
			}
		});
		connectButton.setBounds(179, 130, 89, 23);
		frame.getContentPane().add(connectButton);
	}

}
