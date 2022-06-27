
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
		switch(this.face) {
		case FRONT:
			return this.patternNumber;
		case BACK:
			return PATTERNNUMBER_BACK;
		case REMOVED:
			return PATTERNNUMBER_REMOVED;
		default:
			System.out.println("ī��� ���� ����");
			new Exception().printStackTrace();
			return PATTERNNUMBER_UNDEFINED;
		}
	}
	
	public int getNumber() {
		return patternNumber;
	}
}

public interface GamelogicInterface {
	
	/**
	 * ������ �غ��ϴ� �Լ�
	 * 	�̹� ������ �������̶�� �����ؾ� ��
	 * 
	 * @param pairNum
	 *  ���� ������ ī�带 �� �� ��ġ�� ������ ����
	 * @param kindOfPattern
	 *  ������ �� ���� ����� ������ ����
	 */
	public int setupGame(int pairNum);
	
	/**
	 * �÷��̾��� ī�� ���� �Է��� �����ϴ� �Լ�
	 * 
	 * @param playerNumber
	 * 	��� �÷��̾��� �Է������� ���� /
	 * 	ȣ��Ʈ�� ���ڷ� 0��, �����ڴ� 0 �̿�(default: 1)�� ���
	 * @param selectedCardIndex
	 * 	��� ī�带 �����ϴ��� ���� /
	 *  �⺻������ 0 ~ (�ִ� ī�� �� -1)�� �Է��� ���
	 * */
	public int playerCardSelect(int playerNumber, int selectedCardIndex);
	
}
