import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * ObjectStream 통신을 위한 규격
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
	 * 카드정보 메세지 생성
	 * @param arrCardInfo 카드 정보 배열
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
	private GameManager game = null;	//호스트만 게임로직을 갖는다
	private PlayerInterface GuestUI = null; //게스트만 UI정보를 갖는다
	
	/**
	 * 마스터라면 다른 모든 플레이어가, 게스트라면 마스터가 들어온다
	 */
	private ArrayList<NetworkPlayer> otherPlayers = new ArrayList<NetworkPlayer>();
	
	private ServerSocket socket = null;
	private boolean incoming = false;
	
	/**
	 * 마스터 측에서 본 참여자의 동작을 구현
	 */
	class NetworkPlayer extends Thread implements PlayerInterface{
		//private Socket socket;
		private int networkIndex;
		private ObjectInputStream inputStream;
		private ObjectOutputStream outputStream;
		
		private int playerIndex = -1;
		
		/**
		 * 연결 완료된 TCP소켓이 필요
		 * @param socket 준비된 소켓
		 * @param networkIndex 네트워킹 관리 번호
		 */
		NetworkPlayer(Socket socket, int networkIndex) {
			//this.socket = socket;
			this.networkIndex = networkIndex;
			
			try {
				inputStream = new ObjectInputStream(socket.getInputStream());
				outputStream = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				System.out.println("통신 연결 에러");
				e.printStackTrace();
			}
		}
		
		public void setPlayerIndex(int playerIndex) {
			this.playerIndex = playerIndex;
		}
		
		/**
		 * 프로토콜 규격에 맞춘 메세지를 해당 플레이어에게 전송
		 * @param msg 메세지
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
			System.out.println("에러: 정의되지 않은 동작");
			new Exception().printStackTrace();
		}
		
		/**
		 * 카드 정보를 해당 플레이어에게 전송
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
		 * 메세지 수신 동작
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
				
				// TODO: 버퍼 내용에 따른 처리
				switch(message.messageType) {
				case MessageProtocol.MESSAGETYPE_CHATTING:	// 채팅 수신
					myChatting.receiveMessage(message.getData_String());
					if(isMaster)	// 호스트라면 다른 게스트에게 채팅 공유
					{
						for (int i = 0; i < otherPlayers.size(); i++) {
							if(i == this.networkIndex)
								continue;	// 메세지를 돌려주지 않도록 건너 뜀
							
							otherPlayers.get(i).sendMessage(message);
						}
					}
					break;
				case MessageProtocol.MESSAGETYPE_CARDINFO:	// 호스트가 보내온 카드 정보
					if(isMaster)
					{
						System.out.println("에러: 게스트가 카드 정보를 보냄");
						new Exception().printStackTrace();
					}
					int[] cardInfo = message.getData_Int();
					GuestUI.setCardInfo(cardInfo.length, cardInfo);
					break;
				case MessageProtocol.MESSAGETYPE_CARDSELECT:	// 게스트의 카드 선택 동작
					game.playerCardSelect(playerIndex, message.data[0]);
					break;
				}
			}
		}
	}

	/**
	 * 생성자. 호스트라면 게임매니저를 등록 
	 * @param game 호스트일 경우 게임관리자를 생성자로, 호스트가 아니라면 null
	 */
	public NetworkingManager(GameManager game) {
		this.game = game;
		this.isMaster = (null != this.game);
	}
	
	/**
	 * 초기화 함수. 채팅UI등록
	 * @param chat 채팅UI
	 */
	public void setChattingUI(ChattingUI chat) {
		this.myChatting = chat;
	}
	
	/**
	 * 게스트용 초기화 함수
	 * @param playerUI 플레이어UI
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
	 * 마스터에서만 실행 가능, 접속 스레드
	 * @param portNum 사용할 포트 번호
	 * @return 접속 포트 개방 성공 여부
	 */
	public boolean IncomingThreadBegin(int portNum) {
		if(false == isMaster)
			return false;	// 호스트용 함수
		
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
			System.out.println("새로운 참여자: " + playerNum);
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
	 * 채팅 메세지를 전송
	 * @param msg 메세지 내용
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
