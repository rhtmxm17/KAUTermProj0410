
import java.awt.BorderLayout;
import java.util.Scanner;
import javax.swing.JFrame;

public class PlayerTester implements GamelogicInterface {

	public static void main(String[] args) {
		PlayerTester tester = new PlayerTester();
		PlayerUI player = new PlayerUI();	// TODO : 추가로 필요한 초기화 처리가 있다면 넣기
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
			System.out.println("-2: 뒷면");
			System.out.println("-3: 빈 칸");
			System.out.println("0 ~ n: 문양 번호");
			int[] cardData = new int[16];
			for(int i = 0; i < 16; ++i) {
				System.out.println((i + 1) + "번째 카드 문양: ");
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
