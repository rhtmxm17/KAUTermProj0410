
import java.util.Scanner;

public class PlayerTester implements GamelogicInterface {

	public static void main(String[] args) {
		PlayerTester tester = new PlayerTester();
		PlayerInterface player = null;	// TODO : 구현한 플레이어 UI를 생성해서 넣기
		player.setGamelogic(tester);
		int input;
		
		Scanner scan = new Scanner(System.in);
		boolean end = false;
		while(false == end)
		{
			System.out.print("몇장의 카드를 그리겠습니까(0 입력시 종료): ");
			input = scan.nextInt();
			if(0 >= input)
			{
				end = true;
				continue;
			}
			System.out.println("-2: 뒷면");
			System.out.println("-3: 빈 칸");
			System.out.println("0 ~ n: 문양 번호");
			int[] cardData = new int[input];
			for(int i = 0; i < input; ++i) {
				System.out.println((i + 1) + "번째 카드 문양: ");
				cardData[i] = scan.nextInt();
			}
			player.setCardInfo(input, cardData);
		}
		scan.close();
	}

	@Override
	public int setupGame(int pairNum, int kindOfPattern) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int playerCardSelect(int playerNumber, int selectedCardIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

}
