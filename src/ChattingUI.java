import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


// TODO:사용자의 메세지를 입력받아 그 내용을 NetworkingManager.sendMessage()로 던지고
//		사용자가 입력한 메세지와 receiveMessage()로 들어온 메세지가 전부 올라오는 채팅 로그를 만든다

public class ChattingUI extends JPanel {
	private NetworkingManager network = null;
	
	private JTextField textField;
	private JTextArea textArea;
	private JLabel lblStatus;
	
	public ChattingUI() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(360,600));
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);
		this.add(textArea, BorderLayout.CENTER);
		
		JPanel inputField = new JPanel();
		{
			inputField.setLayout(new BorderLayout());
			inputField.setPreferredSize(new Dimension(360,30));
			
			textField = new JTextField();
			textField.setPreferredSize(new Dimension(300,30));
			//textField.setBounds(0, 230, 390, 31);
			inputField.add(textField, BorderLayout.CENTER);
			textField.setColumns(10);
			
			JButton btnInput = new JButton("입력");
			btnInput.setPreferredSize(new Dimension(60,30));
			btnInput.setForeground(Color.BLUE);
			//btnInput.setBounds(387, 230, 97, 31);
			inputField.add(btnInput, BorderLayout.EAST);
			
			
			ActionListener sendMessageAction = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					sendMessage();	
				}
			};
			
			textField.addActionListener(sendMessageAction);
			btnInput.addActionListener(sendMessageAction);
		}
		this.add(inputField, BorderLayout.SOUTH);
		
		
		JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setBounds(0, 23, 484, 208);
		this.add(scrollPane);
		scrollPane.setViewportView(textArea);
		
		
		//lblStatus = new JLabel("Welcome to KAU Game Chatting Room");
		//lblStatus.setForeground(Color.BLACK);
		//panel.add(lblStatus);
		
		
	}
	
	protected void sendMessage() {
		String text = textField.getText();
		textArea.append(text + "\n");
		//초기화 및 커서요청
		textField.setText("");
		textField.requestFocus();

		if(network != null)
			network.sendMessage(text);
		else
			System.out.println("메세지 송신 테스트: " + text);
	}

	public void setNetworking(NetworkingManager manager) {
		this.network = manager;
	}
	
	/**
	 * 외부에서 받은 메세지의 내용이 이 함수의 인자를 통해 전달될 예정
	 * */
	public void receiveMessage(String message) {
		
		textArea.append(message + "\n");
	}
}
