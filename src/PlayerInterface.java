
public interface PlayerInterface {
	
	/**
	 *  ����� ���ӷ��� �������̽��� �Է�
	 *  
	 * */
	public void setGamelogic(GamelogicInterface game);
	
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
