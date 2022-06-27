import java.awt.BorderLayout;
import java.util.Scanner;
import javax.swing.JFrame;

public class GuestClient {

	public static void main(String[] args) {
		NetworkingManager network = new NetworkingManager(null);
		PlayerUI ui = new PlayerUI();
		ui.setGamelogic(network, 1);
		network.setPlayerUIforGuest(ui);
		
		ChattingUI chat = new ChattingUI();
		chat.setNetworking(network);
		network.setChattingUI(chat);

		Scanner scan = new Scanner(System.in);
		System.out.print("hostAddress: ");
		String hostAddress = scan.nextLine();
		boolean result = false;
		for(int i = 0; i < HostClient.PORT_TRY_MAX; ++i)
		{
			result = network.createConnection(hostAddress, HostClient.DEFAULT_PORTNUM + i);
			//result = network.createConnection("127.0.0.1", HostClient.DEFAULT_PORTNUM + i);
			if(result)
				break;
		}

		if(false == result)
		{
			System.out.println("연결 실패");
			scan.close();
			return;
		}

		System.out.print("your name: ");
		String name = scan.nextLine();
		scan.close();
		
		network.initMessage(name);
		
		
		JFrame frame = new JFrame ("Cardgame Player"); 
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); 
		frame.getContentPane().add(ui, BorderLayout.CENTER);
		frame.getContentPane().add(chat, BorderLayout.EAST);
		
		frame.pack(); 
		frame.setVisible(true);
	}

}