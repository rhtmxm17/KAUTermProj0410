import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;

public class HostClient {
	public static final int DEFAULT_PORTNUM = 5100;
	public static final int PORT_TRY_MAX = 10;
	
	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		System.out.print("이름을 입력: ");
		String name = scan.nextLine();
		
		GameManager game = new GameManager();
		PlayerUI ui = new PlayerUI();
		ui.setGamelogic(game, 0);
		game.addPlayer(ui, name);
		
		NetworkingManager network = new NetworkingManager(game);
		
		ChattingUI chat = new ChattingUI();
		chat.setNetworking(network);
		network.setChattingUI(chat);
		game.setNetwork(network);
		
		boolean result = false;
		for(int i = 0; i < PORT_TRY_MAX; ++i)
		{
			result = network.IncomingThreadBegin(DEFAULT_PORTNUM + i);
			if(result)
				break;
		}
		
		if(!result)
		{
			System.out.println("서버 실행 실패");
			scan.close();
			return;
		}
		
		try {
			System.out.println("LocalHost: " + InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		JFrame frame = new JFrame ("Cardgame Host"); 
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); 
		frame.getContentPane().add(ui, BorderLayout.CENTER);
		frame.getContentPane().add(chat, BorderLayout.EAST);
		frame.pack(); 
		frame.setVisible(true);
		
		
		System.out.println("아무 키나 눌러서 참가자 참여 완료");
		scan.nextLine();
		scan.close();
		
		network.IncomingThreadEnd();

		System.out.println("시작");
		game.setupGame(8);
	}

}

/*
 * save
 * 연결시 본인의 '이름' 등록
 * 서버에서 우승 기록 파일로 저장
 * 참여 메시지 대신 해당 참여자의 우승 기록 출력
 * 게임 종료시 전체 참여자 top3
 * 
 * 
 * 
 * 
 */

