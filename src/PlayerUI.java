import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class PlayerUI extends JPanel implements PlayerInterface {
	GamelogicInterface game = null;
	int myPlayerNumber = -1;

	@Override
	public void setGamelogic(GamelogicInterface game, int playerNumber) {
		this.game = game;
		this.myPlayerNumber = playerNumber;
	}

	@Override
	public void setCardInfo(int cardNum, int[] arrCardInfo) {
		// TODO �� �Լ��� ȣ��Ǹ� ���ڰ��� ���� ī�� ������ ���� �� ����Ұ�

	}

	private class mouse extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO ���콺 Ŭ���� ó��
			
		}

		/*
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		*/
		
	}
}
