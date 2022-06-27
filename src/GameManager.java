import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

class WinRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	String Id;
	int vic;
	
	public WinRecord() {}
	public WinRecord(String Id, int vic)
	{
		this.Id = Id; 
		this.vic = vic;
	}
}

public class GameManager implements GamelogicInterface {
	ArrayList<PlayerInterface> players;	// �÷��̾� ��ü ���
	ArrayList<String> names;
	ArrayList<cardInfo> cards = null;	// ī�� ����
	ArrayList<WinRecord> records = null;
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
		names = new ArrayList<String>();
		records = new ArrayList<WinRecord>();
		
		/*
		// ��� ��� ���̵����� ����
		try {
			ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream("stfile"));
			oout.writeInt(3);
			for (int i =0; i <3; i++)
			{
				oout.writeObject(new WinRecord("Dummy" + i, i + 1));	
			}
			oout.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
		
		try {
			ObjectInputStream inp = new ObjectInputStream(new FileInputStream("stfile"));
			int num = inp.readInt();
			if(num == 0)
			{
				ObjectOutputStream of = new ObjectOutputStream(new FileOutputStream("stfile"));
				of.writeInt(0);
				of.close();
			}else {
				for (int i = 0; i<num; i++) {
					WinRecord s = (WinRecord)inp.readObject();
					records.add(s);
					System.out.println(s.Id + "/" + s.vic);
				}
				
				inp.close();	
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// �÷��̾� �߰� �� ��ȣ�� ��ȯ
	public int addPlayer(PlayerInterface player, String Id) {
		
		players.add(player);
		names.add(Id);
		
		if(null != network)
			network.systemMessageAll("���ο� ������" + players.indexOf(player) + ": " + Id);
		
		for (WinRecord rec : records) {
			if(0 == rec.Id.compareTo(Id))
			{
				String joinMsg = Id +"�� ��� Ƚ��: " + rec.vic;
				System.out.println(joinMsg);
				if(null != network)
					network.systemMessageAll(joinMsg);
			}
		}

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
		
		
		// ������ ��� �����ϱ�
		for(Integer winnerIndex : player)
		{
			boolean done = false;
			for (int i = 0; i < records.size(); i++) {
				if(0 == records.get(i).Id.compareTo(names.get(winnerIndex)))
				{
					int winCnt = records.get(i).vic++;
					network.systemMessageAll("��� Ƚ��: " + winCnt);
					done = true;
				}
			}
			
			// ���� ����� ��Ͽ� ���� ���
			if(false == done)
			{
				records.add(new WinRecord(names.get(winnerIndex), 1));
			}
		}
		
		try {
			// ������ ��� �����ϱ�
			FileOutputStream f = new FileOutputStream("stfile");
			ObjectOutputStream of = new ObjectOutputStream(f);
			of.writeInt(records.size());
			for (int i =0; i <records.size(); i++)
			{
				of.writeObject(records.get(i));	
			}
			of.close();
			
			// 1,2,3 �� ���
			int rankMax = 3;
			if(records.size() < 3)
				rankMax = records.size();
			for(int i =0; i<rankMax;i++)
			{
				WinRecord a = records.get(0);
				int loc=0;
				for (int j =0; j< records.size();j++)
				{
					if(a.vic < records.get(j).vic)
					{
						a = records.get(j);
						loc = j;
					}
				}
				
				String rankMsg = "���� ��� " + (i+1) + "��" + records.get(loc).Id+" : "+records.get(loc).vic;
				if(null == network)
					System.out.println(rankMsg);
				else
					network.systemMessageAll(rankMsg);
				
				records.remove(loc);
				//num--;
			}
		}
		catch (Exception e) {}
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
