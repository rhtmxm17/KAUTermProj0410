
import java.awt.BorderLayout;
import java.util.Scanner;
import javax.swing.JFrame;

public class PlayerTester implements GamelogicInterface {

	public static void main(String[] args) {
		PlayerTester tester = new PlayerTester();
		PlayerUI player = new PlayerUI();	// TODO : �߰��� �ʿ��� �ʱ�ȭ ó���� �ִٸ� �ֱ�
		player.setGamelogic(tester, 0);
		
		JFrame frame = new JFrame ("Cardgame Test"); 
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); 
		frame.getContentPane().add(player, BorderLayout.CENTER); 
		frame.pack(); 
		frame.setVisible(true);
		
		Scanner scan = new Scanner(System.in);
		boolean end = false;
		while(false == end)
		{
			System.out.println("-2: �޸�");
			System.out.println("-3: �� ĭ");
			System.out.println("0 ~ n: ���� ��ȣ");
			int[] cardData = new int[16];
			for(int i = 0; i < 16; ++i) {
				System.out.println((i + 1) + "��° ī�� ����: ");
				cardData[i] = scan.nextInt();
			}
			player.setCardInfo(16, cardData);
		}
		scan.close();
	}

	@Override
	public int setupGame(int pairNum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int playerCardSelect(int playerNumber, int selectedCardIndex) {
		// TODO Auto-generated method stub
		
		
		return 0;
	}

}
