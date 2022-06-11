
public interface PlayerInterface {
	
	/**
	 *  ����� ���ӷ��� �������̽��� �÷��̾� ��ȣ�� �Է�
	 *  
	 * */
	public void setGamelogic(GamelogicInterface game, int playerNumber);
	
	/**
	 * ���ӷ��� Ŭ�����ʿ��� �� �Լ��� ȣ���ؼ� ī�� ������ ������
	 * 
	 * @param cardNum
	 *  ī�尡 �� ��������
	 * @param arrCardInfo
	 *  ī�� ���� �� ����Ÿ���� ��� �迭 (cardInfo Ŭ���� ����)
	 * 
	 * */
	public void setCardInfo(int cardNum, int[] arrCardInfo);
}
