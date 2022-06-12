

// 모든 네트워킹 관리 담당
public class NetworkingManager implements PlayerInterface, GamelogicInterface {
	private ChattingUI myChatting = null;
	private boolean isMaster;
	
	public NetworkingManager(boolean isMaster) {
		this.isMaster = isMaster;
	}
	
	public void setChattingUI(ChattingUI chat) {
		this.myChatting = chat;
	}
	
	public void sendMessage(String msg) {
		
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

	@Override
	public void setGamelogic(GamelogicInterface game, int playerNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCardInfo(int cardNum, int[] arrCardInfo) {
		// TODO Auto-generated method stub

	}

}
