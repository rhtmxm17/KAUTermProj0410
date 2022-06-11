
enum Face { FRONT, BACK, REMOVED };

class cardInfo{
	public final static int PATTERNNUMBER_UNDEFINED = -1;
	public final static int PATTERNNUMBER_BACK = -2;
	public final static int PATTERNNUMBER_REMOVED = -3;
	
	public Face face;
	int patternNumber;
	
	public cardInfo() {
		this.patternNumber = PATTERNNUMBER_UNDEFINED;
		this.face = Face.BACK;
	}
	public cardInfo(int PatternNum) {
		this.patternNumber = PatternNum;
		this.face = Face.BACK;
	}
	
	public int getFacedInfo() {
		if(Face.FRONT == this.face)
			return this.patternNumber;
		else
			return PATTERNNUMBER_BACK;
	}
}

public interface GamelogicInterface {
	
	/**
	 * 게임을 준비하는 함수
	 * 	이미 게임이 진행중이라면 실패해야 함
	 * 
	 * @param pairNum
	 *  같은 문양의 카드를 몇 쌍 배치할 것인지 지정
	 * @param kindOfPattern
	 *  문양을 몇 종류 사용할 것인지 지정
	 */
	public int setupGame(int pairNum, int kindOfPattern);
	
	/**
	 * 플레이어의 카드 선택 입력을 지정하는 함수
	 * 
	 * @param playerNumber
	 * 	어느 플레이어의 입력인지를 지정 /
	 * 	호스트는 인자로 0을, 참여자는 0 이외(default: 1)을 사용
	 * @param selectedCardIndex
	 * 	어느 카드를 선택하는지 지정 /
	 *  기본적으로 0 ~ (최대 카드 수 -1)의 입력을 허용
	 * */
	public int playerCardSelect(int playerNumber, int selectedCardIndex);
	
}
