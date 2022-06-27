import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameManager implements GamelogicInterface {
	ArrayList<PlayerInterface> players;	// �÷��̾� ��ü ���
	ArrayList<cardInfo> cards = null;	// ī�� ����
	int[] score;						// �÷��̾� ���� ��� 
	int kindOfPattern = 0;				// ���� ����
	int cardNum = 0;					// ī�� ����
	int turnplayer = 0;					// ���� �� �÷��̾�
	int play_count = 0;					// �� �÷��̾��� �÷��� Ƚ��
	int[] pick = new int[2];			// �� �÷��̾ ���� ī��
	boolean game_state = true;			// ���� ���� ����
	
	NetworkingManager network = null;	// �ý��� �޽�����
	Thread notify = null;
	
	// �÷��̾� ���� �غ�
	public GameManager() {
		players = new ArrayList<PlayerInterface>();
	}
	
	// �÷��̾� �߰� �� ��ȣ�� ��ȯ
	public int addPlayer(PlayerInterface player) {
		players.add(player);
		return players.indexOf(player);
		// ���� ���� ���ο� ���� �߰� ������ ���´�.
	}
	
	public boolean removePlayer(PlayerInterface player) {
		return players.remove(player);
	}
	
	public void setNetwork(NetworkingManager network) {
		this.network = network;
	}
	
	// ���� �¾�
	public int setupGame(int kindOfPattern) {
		// ���� ���� ����
		this.game_state = true;
		// ī�� ���� ��
		this.kindOfPattern = kindOfPattern;
		// ī�� ��
		this.cardNum = kindOfPattern * 2;
		// ���� ��� �ʱ�ȭ
		score = new int[players.size()];
		// ī�� ����ŭ ī�� ����
		this.cards = new ArrayList<cardInfo>(cardNum);

		// ī�� �������� �ߺ����� ����
		for (int i = 0; i < kindOfPattern; i++) {
			cards.add(new cardInfo(i));
			cards.add(new cardInfo(i));
		}
		Collections.shuffle(cards);

		// ī�� ����(Ȯ�ο�)
		for (int i = 0; i < cards.size(); i++) {
			System.out.print(cards.get(i).patternNumber + " ");
		}
		System.out.println("");

		return 0;
	}
	
	// ���� ���� ���� üũ
	public void gamecheck()
	{
		game_state = false;
		// ī�� ���� Ȯ��
		for(int i =0;i<cards.size();i++)
		{	// ī�尡 ���� �ȵ������� �ִٸ� ���� ���¸� true�� ����
			if(cards.get(i).face == Face.BACK)
			{
				game_state = true;
				break;
			}
		}
	}

	// score �迭 ��ȯ
	public int[] all_score() 
	{
		return this.score;
	}
	
	// score ���ؼ� ����� ����(���ºε� ����)
	public void victory()
	{
		ArrayList<Integer> player = new ArrayList<Integer>();
		int temp = score[0];
		int num = 0;
		// ���� ������ ���� ��� ã��
		for(int i =0; i<players.size();i++)
		{
			if(temp < score[i])
			{
				temp = score[i];
				num = i;
			}
		}
		player.add(num);
		
		// ������ �ִ��� Ȯ���ϱ�
		for(int i =0; i<players.size();i++)
		{
			if((score[player.get(0)] == score[i]) && (player.get(0) != i))
			{
				player.add(i);
			}
		}
		System.out.println(player.size());
		
		String victoyMessage = "����ڴ� ";
		// ����ڰ� �� ���� ���
		if(player.size() == 1)
		{
			victoyMessage += (player + "�� �÷��̾��Դϴ�.");
		}
		else // ����ڰ� ���� ���� ���
		{
			for(int i =0; i<player.size();i++)
			{
				victoyMessage += (player.get(i)+ "�� ");
			}
			victoyMessage += "�Դϴ�.";
		}
		
		if(null == network)
			System.out.println(victoyMessage);
		else
			network.systemMessageAll(victoyMessage);
	}

	// ���� ���÷��̾� ���
	public int nowturnplayer() 
	{
		return turnplayer;
	}

	public int playerCardSelect(int playerNumber, int selectedCardIndex) {
		if(game_state)
		{
			if (selectedCardIndex >= cardNum) // ���� ī�� ��ȣ�� �Է� ��
			{
				String notice = "�־��� ī�� ��ȣ�� �Է��ϼ���.";
				if(null == network)
					System.out.println(notice);
				else
					network.systemMessageTo(playerNumber, notice);
				return 1;
			} else if (cards.get(selectedCardIndex).face == Face.REMOVED) // ���ŵ� ī�带 �Է� ��
			{
				String notice = "�̹� ���� ī���Դϴ�.";
				if(null == network)
					System.out.println(notice);
				else
					network.systemMessageTo(playerNumber, notice);
				return 1;
			} else if (turnplayer != playerNumber) { // ���÷��̾�� �Էµ� �÷��� �ѹ��� �ٸ� ��� �۵�x
				String notice = "���� ���÷��̾�� " + turnplayer + "�Դϴ�.";
				if(null == network)
					System.out.println(notice);
				else
					network.systemMessageTo(playerNumber, notice);
				return 1;
			} else {
				if (play_count == 0) // ���÷��̾��� �÷��� Ƚ���� 1ȸ�� ��
				{
					cards.get(selectedCardIndex).face = Face.FRONT;
					pick[play_count] = selectedCardIndex;
					play_count++;
					notifyCardInfoToPlayers();
				} else if (play_count == 1) { // ���÷��̾��� �÷��� Ƚ���� 2ȸ�� ��
					if (pick[0] == selectedCardIndex) // 1ȸ���� �� ī��� ���� ī�带 ���� ��
					{
						String notice = "���� ī�带 �����߽��ϴ�.";
						if(null == network)
							System.out.println(notice);
						else
							network.systemMessageTo(playerNumber, notice);
						return 1;
					} else // ����� �Է� ��
					{
						cards.get(selectedCardIndex).face = Face.FRONT;
						notifyCardInfoToPlayers();
						pick[play_count] = selectedCardIndex;
						if (cards.get(pick[0]).getNumber() == cards.get(pick[1]).getNumber()) { // ī�带 ������ ���
							cards.get(pick[0]).face = Face.REMOVED;
							cards.get(pick[1]).face = Face.REMOVED;
							System.out.println("ī�带 ���߼̽��ϴ�.");
							
							score[turnplayer]++;
							
							String notice = "�÷��̾� " + turnplayer + "�� ������ :" + score[turnplayer];
							if(null == network)
								System.out.println(notice);
							else
								network.systemMessageAll(notice);
							
							gamecheck(); // ī�尡 �� �������� ������ �����ؾ��ϹǷ�
							if(!game_state)
							{
								victory(); // ����� Ȯ��
							}
							
							turnplayer--;
							play_count = 0;
						} else { // ī�带 Ʋ���� ���
							cards.get(pick[0]).face = Face.BACK;
							cards.get(pick[1]).face = Face.BACK;
						}
						notify = new reserveNotify(1000);
						notify.start();

						turnplayer++;
						play_count = 0;

						// ���÷��̾ �� �÷��̾� ���� ������ ù ��° �÷��̾� ������ ���ư�
						// �� �÷��̾���� 1���� �����ϰ�, turnplayer�� 0���� �����Ѵ�.
						if (turnplayer == players.size()) {
							turnplayer = 0;
						}
					}
				}
			}
		}else
		{
			System.out.println("������ ���� ���� �ϼ���");
		}
		return 0;
	}

	class reserveNotify extends Thread {
		int time;
		reserveNotify(int time){
			this.time = time;
		}
		
		@Override
		public void run() {
			try {
				sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			notifyCardInfoToPlayers();
		}
	}
	
	void notifyCardInfoToPlayers() {
		int[] openedCardInfo = new int[cardNum];
		for(int i = 0; i < cards.size(); ++i)
		{
			cardInfo card = cards.get(i);
			switch(card.face)
			{
			case BACK:
				openedCardInfo[i] = cardInfo.PATTERNNUMBER_BACK;
				break;
			case FRONT:
				openedCardInfo[i] = card.patternNumber;
				break;
			case REMOVED:
				openedCardInfo[i] = cardInfo.PATTERNNUMBER_REMOVED;
				break;
			default:
				openedCardInfo[i] = cardInfo.PATTERNNUMBER_UNDEFINED;
				System.out.println("���ǵ��� ���� ī������");
				new Exception().printStackTrace();
				break;
			}
		}
		
		for(PlayerInterface player : players) {
			player.setCardInfo(cardNum, openedCardInfo);
		}
	}
}
