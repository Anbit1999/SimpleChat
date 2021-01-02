package server;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.NguoiDungDAO;
import entities.NguoiDung;

public class ServerLoginHandler implements Runnable{

	private NguoiDungDAO dao;
	private Socket socket;
	private Gson gson;
	
	private Scanner in;
	private PrintWriter out;
	
	public ServerLoginHandler(NguoiDungDAO dao, Socket socket, Gson gson) {
		super();
		this.dao = dao;
		this.socket = socket;
		this.gson = gson;
	}
	
	@Override
	public void run() {
		try {
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
			String json;
			NguoiDung nguoiDung = null;
			if(in.hasNext()) {
				json = in.nextLine();
				nguoiDung = gson.fromJson(json, NguoiDung.class);
				System.out.println("received: "+json);
				String result = dao.isValidLogin(nguoiDung.getEmail(), nguoiDung.getPassword());
				System.out.println("server: "+result);
				out.println(result);
			}
//			if(in.hasNext()) {
//				json = in.nextLine();
//				System.out.println("received: "+json);
//				Type type = new TypeToken<Set<String>>() {}.getType();
//				Set<String> friends = gson.fromJson(json, type);
//				nguoiDung.setFriends(friends);
//				dao.updateFriends(nguoiDung);
//				System.out.println("server: friends - "+dao.updateFriends(nguoiDung));
//			}
			System.out.println("-----------------------------------------------");
			socket.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
	}

}
