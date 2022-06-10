import java.util.ArrayList;

public class GameManager implements GamelogicInterface {
	ArrayList<PlayerInterface> players;	// �÷��̾� ��ü ���
	ArrayList<cardInfo> cards = null;	// ī�� ����
	int kindOfPattern = 0;				// ���� ����
	int cardNum = 0;					// ī�� ����
	
	public GameManager() {
		players = new ArrayList<PlayerInterface>(2);
	}
	
	// �÷��̾� �߰� �� ��ȣ�� ��ȯ
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
		
		// TODO ���� �غ�
		
		return 0;
	}

	@Override
	public int playerCardSelect(int playerNumber, int selectedCardIndex) {
		
		// TODO playerNumber��ȣ�� �÷��̾ selectedCardIndexī�� ���ý� ����
		//
		// ¦�� ���缭 ī�� ���Ž� ���� ī�� ��ü�� �������� ����
		// ���¸� Face.REMOVED �� �ٲܰ�
		
		
		
		
		notifyCardInfoToPlayers();	// ���ŵ� ������ ����
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
