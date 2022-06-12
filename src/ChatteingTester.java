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
		
		
		System.out.println("콘솔 텍스트 입력으로 통신을 통해 들어오는 메세지를 테스트");
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
		System.out.println("전송 메세지: " + msg);
	}
}
