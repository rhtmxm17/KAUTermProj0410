import java.util.Scanner;

import javax.swing.JFrame;

public class ChatteingTester extends NetworkingManager {

	public ChatteingTester(boolean isMaster) {
		super(isMaster);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChatteingTester dummyNetwork = new ChatteingTester(false);
		ChattingUI chat = new ChattingUI();
		chat.setNetworking(dummyNetwork);
		dummyNetwork.setChattingUI(chat);
		
		
		JFrame frame = new JFrame ("Chatting Test"); 
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); 
		frame.getContentPane().add(chat); 
		frame.pack(); 
		frame.setVisible(true);
		
		
		System.out.println("�ܼ� �ؽ�Ʈ �Է����� ����� ���� ������ �޼����� �׽�Ʈ");
		String input;
		Scanner scan = new Scanner(System.in);
		boolean end = false;
		while(false == end)
		{
			input = scan.nextLine();
			chat.receiveMessage(input);
		}
		scan.close();
	}
	
	@Override
	public void sendMessage(String msg) {
		System.out.println("���� �޼���: " + msg);
	}
}
