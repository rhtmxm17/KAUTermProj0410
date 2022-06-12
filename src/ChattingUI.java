import javax.swing.JPanel;


// TODO:사용자의 메세지를 입력받아 그 내용을 NetworkingManager.sendMessage()로 던지고
//		사용자가 입력한 메세지와 receiveMessage()로 들어온 메세지가 전부 올라오는 채팅 로그를 만든다

public class ChattingUI extends JPanel {
	private NetworkingManager network = null;
	
	public void setNetworking(NetworkingManager manager) {
		this.network = manager;
	}
	
	/*
	 * 외부에서 받은 메세지의 내용이 이 함수의 인자를 통해 전달될 예정
	 * */
	public void receiveMessage(String message) {
		
	}
}
