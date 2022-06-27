
import java.awt.BorderLayout;
import java.util.Scanner;

import javax.swing.JFrame;

class DummyPlayer implements PlayerInterface{
	
	@Override
	public void setCardInfo(int cardNum, int[] arrCardInfo) {
		for(int i : arrCardInfo)
			System.out.print(i + " ");
		System.out.println();
	}

	@Override
	public void setGamelogic(GamelogicInterface game, int playerNumber) {
	}
	
}

public class GameManagerTester {

	public static void main(String[] args) {
		GameManager game = new GameManager();
		Scanner scan = new Scanner(System.in);
		int playerNum;
		int cardNum = 8;
		
		System.out.print("�÷��̾� �� : ");
		playerNum = scan.nextInt();

		
		for(int i  = 0; i < playerNum; ++i)
		{
			PlayerUI ui = new PlayerUI();
			int playerNumber = game.addPlayer(ui);
			ui.setGamelogic(game, playerNumber);
			
			ChattingUI chat = new ChattingUI();
			
			
			JFrame frame = new JFrame ("Cardgame Player" + (i + 1)); 
			frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); 
			frame.getContentPane().add(ui, BorderLayout.CENTER);
			frame.getContentPane().add(chat, BorderLayout.EAST);
			
	
			
			frame.pack(); 
			frame.setVisible(true);
		}
		
		game.setupGame(cardNum);
		
		
		boolean finish = false;
		while(false == finish) {
			System.out.print("�÷��̾� ��ȣ(���� ���÷��̾�:"+game.nowturnplayer()+",������ ����)");
			playerNum = scan.nextInt();
			if(0 > playerNum)
			{
				finish = true;
				continue;
			}
			//game.notifyCardInfoToPlayers();
			System.out.print("ī�� ��ȣ : ");
			cardNum = scan.nextInt();
			game.playerCardSelect(playerNum, cardNum);
		}
		scan.close();
	}
}
