package Client;
import java.awt.EventQueue;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Font;

public class ClientView {
	ClientController controller = new ClientController();

	private JFrame frame;
	private JTextField contactsTextField;
	private JTextField messageTextfield;
	private JTextPane contactScrollPane;
	private JTextPane messageScrollPane;
	private JButton addContactButton;
	private JButton deleteContactButton;
	private JButton sendButton;
	private JLabel userLabel;
	private JLabel statusLabel;
	private JButton statusButton;
	private JLabel lblNewLabel_2;
	private JTextField userTextField;
	private JLabel lblNewLabel_3;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;

	public ClientView(String username) {
		controller.queueName = username;
		controller.changeStatusUser();
	}

	public void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					controller.consumer = messageConsumer;

					initialize();
					frame.setVisible(true);

					controller.createUserQueueAtServer();

					userLabel.setText("Contato: "+controller.queueName);
					statusLabel.setText("[Online]");

					String pendingMessages = getAndShowLastMessages();
					updateChat(pendingMessages);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 850, 610);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		userLabel = new JLabel("Contato:");
		userLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		userLabel.setBounds(37, 11, 166, 14);
		frame.getContentPane().add(userLabel);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(35, 130, 260, 300);
		frame.getContentPane().add(scrollPane_1);

		contactScrollPane = new JTextPane();
		scrollPane_1.setViewportView(contactScrollPane);

		JLabel lblNewLabel = new JLabel("Contatos");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblNewLabel.setBounds(37, 83, 177, 14);
		frame.getContentPane().add(lblNewLabel);

		contactsTextField = new JTextField();
		contactsTextField.setBounds(35, 450, 260, 20);
		frame.getContentPane().add(contactsTextField);
		contactsTextField.setColumns(10);

		addContactButton = new JButton("Adicionar");
		addContactButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.contactsList.add(contactsTextField.getText());
				contactsTextField.setText("");
				contactScrollPane.setText(listingContacts());
			}
		});
		addContactButton.setBounds(37, 485, 129, 23);
		frame.getContentPane().add(addContactButton);

		deleteContactButton = new JButton("Deletar");
		deleteContactButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.contactsList.remove(contactsTextField.getText());
				contactsTextField.setText("");
				contactScrollPane.setText(listingContacts());
			}
		});
		deleteContactButton.setBounds(170, 485, 125, 23);
		frame.getContentPane().add(deleteContactButton);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(392, 130, 399, 300);
		frame.getContentPane().add(scrollPane);

		messageScrollPane = new JTextPane();
		scrollPane.setViewportView(messageScrollPane);

		JLabel lblNewLabel_1 = new JLabel("Mensagens");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblNewLabel_1.setBounds(392, 83, 129, 20);
		frame.getContentPane().add(lblNewLabel_1);

		messageTextfield = new JTextField();
		messageTextfield.setBounds(489, 483, 302, 23);
		frame.getContentPane().add(messageTextfield);
		messageTextfield.setColumns(10);

		sendButton = new JButton("Enviar");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String menssagem = messageTextfield.getText();
				String usuarioDestino = userTextField.getText();
				try {
					controller.sendMessageToUser(usuarioDestino, menssagem);
					updateChat("Eu: " + menssagem + "\n");
				} catch (RemoteException e1) {

					e1.printStackTrace();
				}
				messageTextfield.setText("");

			}
		});
		sendButton.setBounds(702, 518, 89, 23);
		frame.getContentPane().add(sendButton);

		statusLabel = new JLabel("Status:");
		statusLabel.setBounds(231, 11, 64, 14);
		frame.getContentPane().add(statusLabel);

		statusButton = new JButton("Desconectar");
		statusButton.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		statusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.changeStatusUser();
				if (controller.isOn) {
					statusButton.setText("Desconectar");
					statusLabel.setText("[Online]");
					String pendingMessages = getAndShowLastMessages();
					updateChat(pendingMessages);
				} else {
					statusButton.setText("Conectar");
					statusLabel.setText("[Offline]");
				}

			}
		});
		statusButton.setBounds(307, 9, 129, 20);
		frame.getContentPane().add(statusButton);

		lblNewLabel_2 = new JLabel("Destino:");
		lblNewLabel_2.setBounds(392, 450, 79, 20);
		frame.getContentPane().add(lblNewLabel_2);

		userTextField = new JTextField();
		userTextField.setText("");
		userTextField.setBounds(489, 450, 302, 20);
		frame.getContentPane().add(userTextField);
		userTextField.setColumns(10);

		lblNewLabel_3 = new JLabel("Mensagem:");
		lblNewLabel_3.setBounds(392, 490, 90, 14);
		frame.getContentPane().add(lblNewLabel_3);
	}

	public String listingContacts() {
		StringBuilder contactsNames = new StringBuilder();
		controller.contactsList.forEach(contact -> {
			contactsNames.append(contact + "\n");
		});
		return contactsNames.toString();
	}

	public String getAndShowLastMessages() {
		List<String> messages = controller.getUserMessagesAtServer();
		StringBuilder messageBuilder = new StringBuilder();
		messages.forEach(message -> {
			messageBuilder.append(message + "\n");
		});
		return messageBuilder.toString();
	}

	void updateChat(String Text) {
		String previusText = messageScrollPane.getText();
		if (previusText == null) {
			previusText = "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(previusText);
		stringBuilder.append(Text);
		String newText = stringBuilder.toString();
		messageScrollPane.setText(newText);

	}

	Consumer<String> messageConsumer = (message) -> {
		String previusText = messageScrollPane.getText();
		if (previusText == null) {
			previusText = "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(previusText);
		stringBuilder.append(message);
		String newText = stringBuilder.toString();
		messageScrollPane.setText(newText);

	};
	

}
