package server;

import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.NguoiDungDAO;
import entities.NguoiDung;

public class ServerFriendManagerHandler implements Runnable{
	
	private NguoiDungDAO dao;
	private Socket socket;
	private Gson gson;
	
	private Scanner in;
	
	public ServerFriendManagerHandler(NguoiDungDAO dao, Socket socket, Gson gson) {
		super();
		this.dao = dao;
		this.socket = socket;
		this.gson = gson;
	}

	/**
	 * client phải gửi: email, friends
	 */
	@Override
	public void run() {
		try {
			in = new Scanner(socket.getInputStream());
			String json;
			NguoiDung nguoiDung;
			if(in.hasNext()) {
				json = in.nextLine();
				System.out.println("received: "+json);
				nguoiDung = gson.fromJson(json, NguoiDung.class);				
				dao.updateFriends(nguoiDung);
				System.out.println("server: friends - "+dao.updateFriends(nguoiDung));
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	
	
	
	
}
