import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.util.Arrays;

public class PlayerUI extends JPanel implements PlayerInterface {
	GamelogicInterface game = null;
	int myPlayerNumber = -1;
	JButton[] button = new JButton[16];
	
	//�̹���(0~7:����, 8:�޸�)
	ImageIcon[] image = new ImageIcon[9];
	
	public PlayerUI() {
		this.setLayout(new GridLayout(4,4));
		
		//��ư ����
		for(int i=0;i<16;i++)
		{
			button[i]=new JButton();
			button[i].setPreferredSize(new Dimension(120,150));
			button[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					int selectedNum = Arrays.asList(button).indexOf(e.getSource());
					game.playerCardSelect(myPlayerNumber, selectedNum);
					System.out.println(selectedNum + "�� ī�� ���õ�");
				}
			});
			button[i].setVisible(true);
			this.add(button[i]);
		}
		
		//�̹��� �ҷ�����
		for (int i = 0; i < image.length; i++)
			image[i] = new ImageIcon("./image/" + i + ".png");
		
		/*
		image[0] = new ImageIcon("./image/0.png");
		image[1] = new ImageIcon("./image/1.png");
		image[2] = new ImageIcon("./image/2.png");
		image[3] = new ImageIcon("./image/3.png");
		image[4] = new ImageIcon("./image/4.png");
		image[5] = new ImageIcon("./image/5.png");
		image[6] = new ImageIcon("./image/6.png");
		image[7] = new ImageIcon("./image/7.png");
		image[8] = new ImageIcon("./image/8.png"); //ī�� �޸� (-2)
		 */
		
		//ī�� ������ ����� �迭 (�ӽ�)
		int arr[] = new int[16];
		for (int i = 0; i < arr.length; i++)
			arr[i] = -2;
		this.setCardInfo(16, arr);
	}
	
	@Override
	public void setGamelogic(GamelogicInterface game, int playerNumber) {
		this.game = game;
		this.myPlayerNumber = playerNumber;
	}

	@Override
	public void setCardInfo(int cardNum, int[] arrCardInfo) {
		for (int i=0; i<16; i++) {
			switch(arrCardInfo[i]) {
			case cardInfo.PATTERNNUMBER_BACK:
				button[i].setIcon(image[8]);
				break;
			case cardInfo.PATTERNNUMBER_REMOVED:
				button[i].setVisible(false);
				break;
			case cardInfo.PATTERNNUMBER_UNDEFINED:
				System.out.println("����: �������� ���� ī�� ����");
				new Exception().printStackTrace();
				break;
			default:
				button[i].setIcon(image[arrCardInfo[i]]);
			}
			//if (arrCardInfo[i]!=-2)
			//	button[i].setIcon(image[arrCardInfo[i]]);
		}
		repaint();
	}
}
