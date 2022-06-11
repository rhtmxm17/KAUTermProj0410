
public interface PlayerInterface {
	
	/**
	 *  사용할 게임로직 인터페이스와 플레이어 번호를 입력
	 *  
	 * */
	public void setGamelogic(GamelogicInterface game, int playerNumber);
	
	/**
	 * 게임로직 클래스쪽에서 이 함수를 호출해서 카드 정보를 갱신함
	 * 
	 * @param cardNum
	 *  카드가 총 몇장인지
	 * @param arrCardInfo
	 *  카드 상태 및 문양타입이 담긴 배열 (cardInfo 클래스 참조)
	 * 
	 * */
	public void setCardInfo(int cardNum, int[] arrCardInfo);
}
