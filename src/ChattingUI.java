import javax.swing.JPanel;


// TODO:������� �޼����� �Է¹޾� �� ������ NetworkingManager.sendMessage()�� ������
//		����ڰ� �Է��� �޼����� receiveMessage()�� ���� �޼����� ���� �ö���� ä�� �α׸� �����

public class ChattingUI extends JPanel {
	private NetworkingManager network = null;
	
	public void setNetworking(NetworkingManager manager) {
		this.network = manager;
	}
	
	/*
	 * �ܺο��� ���� �޼����� ������ �� �Լ��� ���ڸ� ���� ���޵� ����
	 * */
	public void receiveMessage(String message) {
		
	}
}
