import java.util.Scanner;

public class HostClient {
	public static final int DEFAULT_PORTNUM = 5100;
	public static final int PORT_TRY_MAX = 10;
	
	public static void main(String[] args) {
		
		GameManager game = new GameManager();
		PlayerUI ui = new PlayerUI();
		ui.setGamelogic(game, 0);
		game.addPlayer(ui);
		
		NetworkingManager network = new NetworkingManager(game);
		
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
			return;
		}
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("아무 키나 눌러서 참가자 참여 완료");
		scan.nextByte();
		scan.close();
		
		network.IncomingThreadEnd();
		
	}

}
