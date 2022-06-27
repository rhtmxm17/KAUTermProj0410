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
	ArrayList<PlayerInterface> players;	// 플레이어 객체 목록
	ArrayList<String> names;
	ArrayList<cardInfo> cards = null;	// 카드 정보
	ArrayList<WinRecord> records = null;
	int[] score;						// 플레이어 점수 목록 
	int kindOfPattern = 0;				// 패턴 종류
	int cardNum = 0;					// 카드 갯수
	int turnplayer = 0;					// 현재 턴 플레이어
	int play_count = 0;					// 턴 플레이어의 플레이 횟수
	int[] pick = new int[2];			// 턴 플레이어가 뽑은 카드
	boolean game_state = true;			// 게임 진행 상태
	
	NetworkingManager network = null;	// 시스템 메시지용
	Thread notify = null;
	
	// 플레이어 받을 준비
	public GameManager() {
		players = new ArrayList<PlayerInterface>();
		names = new ArrayList<String>();
		records = new ArrayList<WinRecord>();
		
		/*
		// 우승 기록 더미데이터 생성
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
	
	// 플레이어 추가 후 번호를 반환
	public int addPlayer(PlayerInterface player, String Id) {
		
		players.add(player);
		names.add(Id);
		
		if(null != network)
			network.systemMessageAll("새로운 참여자" + players.indexOf(player) + ": " + Id);
		
		for (WinRecord rec : records) {
			if(0 == rec.Id.compareTo(Id))
			{
				String joinMsg = Id +"의 우승 횟수: " + rec.vic;
				System.out.println(joinMsg);
				if(null != network)
					network.systemMessageAll(joinMsg);
			}
		}

		return players.indexOf(player);
		// 게임 실행 여부에 따라 추가 입장을 막는다.
	}
	
	public boolean removePlayer(PlayerInterface player) {
		return players.remove(player);
	}
	
	public void setNetwork(NetworkingManager network) {
		this.network = network;
	}
	
	// 게임 셋업
	public int setupGame(int kindOfPattern) {
		// 게임 진행 상태
		this.game_state = true;
		// 카드 패턴 수
		this.kindOfPattern = kindOfPattern;
		// 카드 수
		this.cardNum = kindOfPattern * 2;
		// 점수 목록 초기화
		score = new int[players.size()];
		// 카드 수만큼 카드 생성
		this.cards = new ArrayList<cardInfo>(cardNum);

		// 카드 랜덤으로 중복없이 설정
		for (int i = 0; i < kindOfPattern; i++) {
			cards.add(new cardInfo(i));
			cards.add(new cardInfo(i));
		}
		Collections.shuffle(cards);

		// 카드 내부(확인용)
		for (int i = 0; i < cards.size(); i++) {
			System.out.print(cards.get(i).patternNumber + " ");
		}
		System.out.println("");

		return 0;
	}
	
	// 게임 진행 상태 체크
	public void gamecheck()
	{
		game_state = false;
		// 카드 상태 확인
		for(int i =0;i<cards.size();i++)
		{	// 카드가 아직 안뒤집힌게 있다면 진행 상태를 true로 변경
			if(cards.get(i).face == Face.BACK)
			{
				game_state = true;
				break;
			}
		}
	}

	// score 배열 반환
	public int[] all_score() 
	{
		return this.score;
	}
	
	// score 비교해서 우승자 고르기(무승부도 존재)
	public void victory()
	{
		ArrayList<Integer> player = new ArrayList<Integer>();
		int temp = score[0];
		int num = 0;
		// 제일 점수가 높은 사람 찾기
		for(int i =0; i<players.size();i++)
		{
			if(temp < score[i])
			{
				temp = score[i];
				num = i;
			}
		}
		player.add(num);
		
		// 동점자 있는지 확인하기
		for(int i =0; i<players.size();i++)
		{
			if((score[player.get(0)] == score[i]) && (player.get(0) != i))
			{
				player.add(i);
			}
		}
		System.out.println(player.size());
		
		String victoyMessage = "우승자는 ";
		// 우승자가 한 명일 경우
		if(player.size() == 1)
		{
			victoyMessage += (player + "번 플레이어입니다.");
		}
		else // 우승자가 여러 명일 경우
		{
			for(int i =0; i<player.size();i++)
			{
				victoyMessage += (player.get(i)+ "번 ");
			}
			victoyMessage += "입니다.";
		}
		
		if(null == network)
			System.out.println(victoyMessage);
		else
			network.systemMessageAll(victoyMessage);
		
		
		// 기존의 결과 수정하기
		for(Integer winnerIndex : player)
		{
			boolean done = false;
			for (int i = 0; i < records.size(); i++) {
				if(0 == records.get(i).Id.compareTo(names.get(winnerIndex)))
				{
					int winCnt = records.get(i).vic++;
					network.systemMessageAll("우승 횟수: " + winCnt);
					done = true;
				}
			}
			
			// 기존 우승자 목록에 없을 경우
			if(false == done)
			{
				records.add(new WinRecord(names.get(winnerIndex), 1));
			}
		}
		
		try {
			// 수정된 결과 저장하기
			FileOutputStream f = new FileOutputStream("stfile");
			ObjectOutputStream of = new ObjectOutputStream(f);
			of.writeInt(records.size());
			for (int i =0; i <records.size(); i++)
			{
				of.writeObject(records.get(i));	
			}
			of.close();
			
			// 1,2,3 등 출력
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
				
				String rankMsg = "누적 우승 " + (i+1) + "위" + records.get(loc).Id+" : "+records.get(loc).vic;
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

	// 현재 턴플레이어 출력
	public int nowturnplayer() 
	{
		return turnplayer;
	}

	public int playerCardSelect(int playerNumber, int selectedCardIndex) {
		if(game_state)
		{
			if (selectedCardIndex >= cardNum) // 없는 카드 번호를 입력 시
			{
				String notice = "주어진 카드 번호를 입력하세요.";
				if(null == network)
					System.out.println(notice);
				else
					network.systemMessageTo(playerNumber, notice);
				return 1;
			} else if (cards.get(selectedCardIndex).face == Face.REMOVED) // 제거된 카드를 입력 시
			{
				String notice = "이미 맞춘 카드입니다.";
				if(null == network)
					System.out.println(notice);
				else
					network.systemMessageTo(playerNumber, notice);
				return 1;
			} else if (turnplayer != playerNumber) { // 턴플레이어와 입력된 플레이 넘버가 다를 경우 작동x
				String notice = "현재 턴플레이어는 " + turnplayer + "입니다.";
				if(null == network)
					System.out.println(notice);
				else
					network.systemMessageTo(playerNumber, notice);
				return 1;
			} else {
				if (play_count == 0) // 턴플레이어의 플레이 횟수가 1회일 때
				{
					cards.get(selectedCardIndex).face = Face.FRONT;
					pick[play_count] = selectedCardIndex;
					play_count++;
					notifyCardInfoToPlayers();
				} else if (play_count == 1) { // 턴플레이어의 플레이 횟수가 2회일 때
					if (pick[0] == selectedCardIndex) // 1회에서 고른 카드와 같은 카드를 선택 시
					{
						String notice = "같은 카드를 선택했습니다.";
						if(null == network)
							System.out.println(notice);
						else
							network.systemMessageTo(playerNumber, notice);
						return 1;
					} else // 제대로 입력 시
					{
						cards.get(selectedCardIndex).face = Face.FRONT;
						notifyCardInfoToPlayers();
						pick[play_count] = selectedCardIndex;
						if (cards.get(pick[0]).getNumber() == cards.get(pick[1]).getNumber()) { // 카드를 맞췄을 경우
							cards.get(pick[0]).face = Face.REMOVED;
							cards.get(pick[1]).face = Face.REMOVED;
							System.out.println("카드를 맞추셨습니다.");
							
							score[turnplayer]++;
							
							String notice = "플레이어 " + turnplayer + "의 점수는 :" + score[turnplayer];
							if(null == network)
								System.out.println(notice);
							else
								network.systemMessageAll(notice);
							
							gamecheck(); // 카드가 다 뒤집히면 게임을 종료해야하므로
							if(!game_state)
							{
								victory(); // 우승자 확인
							}
							
							turnplayer--;
							play_count = 0;
						} else { // 카드를 틀렸을 경우
							cards.get(pick[0]).face = Face.BACK;
							cards.get(pick[1]).face = Face.BACK;
						}
						notify = new reserveNotify(1000);
						notify.start();

						turnplayer++;
						play_count = 0;

						// 턴플레이어가 총 플레이어 수와 같을때 첫 번째 플레이어 순서로 돌아감
						// 총 플레이어수는 1부터 시작하고, turnplayer는 0부터 시작한다.
						if (turnplayer == players.size()) {
							turnplayer = 0;
						}
					}
				}
			}
		}else
		{
			System.out.println("게임을 먼저 실행 하세요");
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
				System.out.println("정의되지 않은 카드정보");
				new Exception().printStackTrace();
				break;
			}
		}
		
		for(PlayerInterface player : players) {
			player.setCardInfo(cardNum, openedCardInfo);
		}
	}
}
