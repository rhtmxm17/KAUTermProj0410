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
		// TODO 이 함수가 호출되면 인자값을 토대로 카드 정보를 갱신 및 출력할것

	}

	private class mouse extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO 마우스 클릭시 처리
			
		}

		/*
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		*/
		
	}
}
