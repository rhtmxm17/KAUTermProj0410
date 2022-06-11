import java.util.Scanner;

class DummyPlayer implements PlayerInterface{
	
	@Override
	public void setCardInfo(int cardNum, int[] arrCardInfo) {
		System.out.println(arrCardInfo);
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
		int cardNum;
		
		System.out.print("�÷��̾� �� : ");
		playerNum = scan.nextInt();
		System.out.print("ī��� �� : ");
		cardNum = scan.nextInt();
		
		game.setupGame(cardNum * 2, cardNum);
		
		for(int i  = 0; i < playerNum; ++i)
			game.addPlayer(new DummyPlayer());
		
		boolean finish = false;
		while(false == finish) {
			System.out.print("�÷��̾� ��ȣ(������ ����) : ");
			playerNum = scan.nextInt();
			if(0 > playerNum)
			{
				finish = true;
				continue;
			}
			System.out.print("ī�� ��ȣ : ");
			cardNum = scan.nextInt();
			game.playerCardSelect(playerNum, cardNum);
		}
		scan.close();
	}
}
