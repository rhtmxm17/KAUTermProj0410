import java.util.ArrayList;

public class GameManager implements GamelogicInterface {
	ArrayList<PlayerInterface> players;	// 플레이어 객체 목록
	ArrayList<cardInfo> cards = null;	// 카드 정보
	int kindOfPattern = 0;				// 패턴 종류
	int cardNum = 0;					// 카드 갯수
	
	public GameManager() {
		players = new ArrayList<PlayerInterface>(2);
	}
	
	// 플레이어 추가 후 번호를 반환
	public int addPlayer(PlayerInterface player) {
		players.add(player);
		return players.indexOf(player);
	}
	
	public boolean removePlayer(PlayerInterface player) {
		return players.remove(player);
	}
	
	@Override
	public int setupGame(int pairNum, int kindOfPattern) {
		this.kindOfPattern = kindOfPattern;
		this.cardNum = pairNum * 2;
		this.cards = new ArrayList<cardInfo>(cardNum);
		
		// TODO 게임 준비
		
		return 0;
	}

	@Override
	public int playerCardSelect(int playerNumber, int selectedCardIndex) {
		
		// TODO playerNumber번호의 플레이어가 selectedCardIndex카드 선택시 동작
		//
		// 짝을 맞춰서 카드 제거시 실제 카드 객체를 삭제하지 말고
		// 상태를 Face.REMOVED 로 바꿀것
		
		
		
		
		notifyCardInfoToPlayers();	// 갱신된 정보를 통지
		return 0;
	}

	private void notifyCardInfoToPlayers() {
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
