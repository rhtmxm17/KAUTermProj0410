import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * ObjectStream ����� ���� �԰�
 */
class MessageProtocol implements Serializable {
	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int MESSAGETYPE_INIT = 0;
	public static final int MESSAGETYPE_CHATTING = 1;
	public static final int MESSAGETYPE_CARDINFO = 2;
	public static final int MESSAGETYPE_CARDSELECT = 3;
	
	public int messageType;
	public byte[] data;
	
	private MessageProtocol() {}
	
	/**
	 * ī������ �޼��� ����
	 * @param arrCardInfo ī�� ���� �迭
	 */
	public MessageProtocol(int[] arrCardInfo) {
		this.messageType = MESSAGETYPE_CARDINFO;
		for(int i = 0; i < arrCardInfo.length; ++i)
			data[i] = (byte)arrCardInfo[i];
	}
	
	public MessageProtocol(String ChattingMessage) {
		this.messageType = MESSAGETYPE_CHATTING;
		this.data = ChattingMessage.getBytes();
	}
	
	public static MessageProtocol CardSelectMessage(int cardNumber) {
		MessageProtocol out = new MessageProtocol();
		out.messageType = MESSAGETYPE_CARDSELECT;
		out.data = new byte[1];
		out.data[0] = (byte)cardNumber;
		return out;
	}

	public int[] getData_Int() {
		int[] out = new int[data.length];
		for(int i = 0; i < data.length; ++i)
			out[i] = data[i];
		return out;
	}
	
	public String getData_String() {
		return data.toString();
	}
}

public class NetworkingManager extends Thread implements GamelogicInterface {
	private ChattingUI myChatting = null;
	private boolean isMaster;
	private GameManager game = null;	//ȣ��Ʈ�� ���ӷ����� ���´�
	private PlayerInterface GuestUI = null; //�Խ�Ʈ�� UI������ ���´�
	
	/**
	 * �����Ͷ�� �ٸ� ��� �÷��̾, �Խ�Ʈ��� �����Ͱ� ���´�
	 */
	private ArrayList<NetworkPlayer> otherPlayers = new ArrayList<NetworkPlayer>();
	
	private ServerSocket socket = null;
	private boolean incoming = false;
	
	/**
	 * ������ ������ �� �������� ������ ����
	 */
	class NetworkPlayer extends Thread implements PlayerInterface{
		//private Socket socket;
		private int networkIndex;
		private ObjectInputStream inputStream;
		private ObjectOutputStream outputStream;
		
		private int playerIndex = -1;
		
		/**
		 * ���� �Ϸ�� TCP������ �ʿ�
		 * @param socket �غ�� ����
		 * @param networkIndex ��Ʈ��ŷ ���� ��ȣ
		 */
		NetworkPlayer(Socket socket, int networkIndex) {
			//this.socket = socket;
			this.networkIndex = networkIndex;
			
			try {
				inputStream = new ObjectInputStream(socket.getInputStream());
				outputStream = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				System.out.println("��� ���� ����");
				e.printStackTrace();
			}
		}
		
		public void setPlayerIndex(int playerIndex) {
			this.playerIndex = playerIndex;
		}
		
		/**
		 * �������� �԰ݿ� ���� �޼����� �ش� �÷��̾�� ����
		 * @param msg �޼���
		 */
		public void sendMessage(MessageProtocol msg) {
			try {
				outputStream.writeObject(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		@Override
		public void setGamelogic(GamelogicInterface game, int playerNumber) {
			System.out.println("����: ���ǵ��� ���� ����");
			new Exception().printStackTrace();
		}
		
		/**
		 * ī�� ������ �ش� �÷��̾�� ����
		 */
		@Override
		public void setCardInfo(int cardNum, int[] arrCardInfo) {
			try {
			outputStream.writeObject(new MessageProtocol(arrCardInfo));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * �޼��� ���� ����
		 */
		@Override
		public void run() {
			while(true) {
				MessageProtocol message;
				try {
					message = (MessageProtocol)inputStream.readObject();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					continue;
				}
				
				// TODO: ���� ���뿡 ���� ó��
				switch(message.messageType) {
				case MessageProtocol.MESSAGETYPE_CHATTING:	// ä�� ����
					myChatting.receiveMessage(message.getData_String());
					if(isMaster)	// ȣ��Ʈ��� �ٸ� �Խ�Ʈ���� ä�� ����
					{
						for (int i = 0; i < otherPlayers.size(); i++) {
							if(i == this.networkIndex)
								continue;	// �޼����� �������� �ʵ��� �ǳ� ��
							
							otherPlayers.get(i).sendMessage(message);
						}
					}
					break;
				case MessageProtocol.MESSAGETYPE_CARDINFO:	// ȣ��Ʈ�� ������ ī�� ����
					if(isMaster)
					{
						System.out.println("����: �Խ�Ʈ�� ī�� ������ ����");
						new Exception().printStackTrace();
					}
					int[] cardInfo = message.getData_Int();
					GuestUI.setCardInfo(cardInfo.length, cardInfo);
					break;
				case MessageProtocol.MESSAGETYPE_CARDSELECT:	// �Խ�Ʈ�� ī�� ���� ����
					game.playerCardSelect(playerIndex, message.data[0]);
					break;
				}
			}
		}
	}

	/**
	 * ������. ȣ��Ʈ��� ���ӸŴ����� ��� 
	 * @param game ȣ��Ʈ�� ��� ���Ӱ����ڸ� �����ڷ�, ȣ��Ʈ�� �ƴ϶�� null
	 */
	public NetworkingManager(GameManager game) {
		this.game = game;
		this.isMaster = (null != this.game);
	}
	
	/**
	 * �ʱ�ȭ �Լ�. ä��UI���
	 * @param chat ä��UI
	 */
	public void setChattingUI(ChattingUI chat) {
		this.myChatting = chat;
	}
	
	/**
	 * �Խ�Ʈ�� �ʱ�ȭ �Լ�
	 * @param playerUI �÷��̾�UI
	 */
	public void setPlayerUIforGuest(PlayerInterface playerUI) {
		if(this.isMaster)
		{
			new Exception().printStackTrace();
			return;
		}
		this.GuestUI = playerUI;
	}
	
	/**
	 * �����Ϳ����� ���� ����, ���� ������
	 * @param portNum ����� ��Ʈ ��ȣ
	 * @return ���� ��Ʈ ���� ���� ����
	 */
	public boolean IncomingThreadBegin(int portNum) {
		if(false == isMaster)
			return false;	// ȣ��Ʈ�� �Լ�
		
		try {
			socket = new ServerSocket(portNum);
		} catch (IOException e) {
			return false;
		}
		
		System.out.println("InetAddress:" + socket.getInetAddress());
		System.out.println("LocalPort:" + socket.getLocalPort());
		this.incoming = true;
		this.start();
		return true;
	}
	
	@Override
	public void run() {
		if(false == isMaster)
			return;

		while(incoming) {
			Socket incoming;
			try {
				incoming = socket.accept( );
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			NetworkPlayer newPlayer = new NetworkPlayer(incoming, otherPlayers.size());
			this.otherPlayers.add(newPlayer);
			int playerNum = game.addPlayer(newPlayer);
			newPlayer.setPlayerIndex(playerNum);
			System.out.println("���ο� ������: " + playerNum);
		}
	}
	
	public void IncomingThreadEnd() {
		incoming = false;
	}
	
	public boolean createConnection(String hostAddress, int portNum) {
		Socket host;
		try {
			host = new Socket(hostAddress, portNum);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		this.otherPlayers.add(new NetworkPlayer(host, 0));
		
		return true;
	}
	
	/**
	 * ä�� �޼����� ����
	 * @param msg �޼��� ����
	 */
	public void sendMessage(String msg) {
		MessageProtocol message = new MessageProtocol(msg);

		for (NetworkPlayer networkPlayer : otherPlayers)
			networkPlayer.sendMessage(message);
	}

	@Override
	public int setupGame(int pairNum, int kindOfPattern) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int playerCardSelect(int playerNumber, int selectedCardIndex) {
		if(isMaster)
		{
			new Exception().printStackTrace();
			return -1;
		}
		
		otherPlayers.get(0).sendMessage(MessageProtocol.CardSelectMessage(selectedCardIndex));
		
		return 0;
	}
	
}
