package server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.gson.Gson;

import dao.MyDatabase;
import dao.NguoiDungDAO;

public class SimpleChatServer {
	
	private static final String ACCESSKEY = "AKIA3LBQEI65LVPVS2PR";
	private static final String SECRETKEY = "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";
	
	private static final int PORT_SERVER_SIGNIN = 9000;
	private static final int PORT_SERVER_LOGIN = 9001;
	private static final int PORT_SERVER_FRIEND_MANAGER = 9002;

	private DynamoDBMapper mapper;
	private NguoiDungDAO nguoiDungDAO;
	private InetAddress address;
	private Gson gson;
	
	public SimpleChatServer() throws Exception {
		mapper = MyDatabase.getInstance(ACCESSKEY, SECRETKEY).getMapper();
		nguoiDungDAO = NguoiDungDAO.getInstance(mapper);
		address = InetAddress.getLocalHost();
		gson = new Gson();
	}

	public void StartServer() throws Exception {
		System.out.println("Server's IP: "+address.getHostAddress());
		Thread threadServerLogin = new Thread(() -> {
			try {
				ServerLogin();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Thread threadServerSignIn = new Thread(() -> {
			try {
				ServerSignIn();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Thread threadServerFriendManager = new Thread(() -> {
			try {
				ServerFriendManager();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		threadServerLogin.start();
		threadServerSignIn.start();
		threadServerFriendManager.start();
	}

	@SuppressWarnings("resource")
	private void ServerFriendManager() throws Exception {
		ServerSocket serverSocket = new ServerSocket(PORT_SERVER_FRIEND_MANAGER); 
		System.out.println("ServerFriendManager's port: "+PORT_SERVER_FRIEND_MANAGER);
		while(true) {
			Socket socket = serverSocket.accept();
			System.out.println("ServerFriendManager's client is connected: "+socket.getInetAddress().getHostAddress());
			new Thread(new ServerFriendManagerHandler(nguoiDungDAO, socket, gson)).start();
		}
	}
	
	@SuppressWarnings("resource")
	private void ServerSignIn() throws Exception {
		ServerSocket serverSocket = new ServerSocket(PORT_SERVER_SIGNIN); 
		System.out.println("ServerSignUp's port: "+PORT_SERVER_SIGNIN);
		while(true) {
			Socket socket = serverSocket.accept();
			System.out.println("ServerSignUp's client is connected: "+socket.getInetAddress().getHostAddress());
			new Thread(new ServerSignUpHandler(nguoiDungDAO, socket, gson)).start();
		}
	}

	@SuppressWarnings("resource")
	private void ServerLogin() throws Exception{
		ServerSocket serverSocket = new ServerSocket(PORT_SERVER_LOGIN); 
		System.out.println("ServerLogin's port : "+PORT_SERVER_LOGIN);
		while(true) {
			Socket socket = serverSocket.accept();
			System.out.println("ServerLogin's client is connected: "+socket.getInetAddress().getHostAddress());
			new Thread(new ServerLoginHandler(nguoiDungDAO, socket, gson)).start();
		}
	}
	
	public static void main(String[] args) throws Exception{
		new SimpleChatServer().StartServer();
	}
	
}
