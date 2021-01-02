package server;

import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

import dao.NguoiDungDAO;
import entities.NguoiDung;

public class ServerSignUpHandler implements Runnable{

	private NguoiDungDAO dao;
	private Socket socket;
	private Gson gson;
	
	private Scanner in;
	
		
	public ServerSignUpHandler(NguoiDungDAO dao, Socket socket, Gson gson) {
		super();
		this.dao = dao;
		this.socket = socket;
		this.gson = gson;
	}

	public void run() {
		try {
			in = new Scanner(socket.getInputStream());
			if(in.hasNext()) {
				NguoiDung nguoiDung = gson.fromJson(in.nextLine(), NguoiDung.class);
				dao.addNguoiDung(nguoiDung);					
				System.out.println("added:"+nguoiDung);
				System.out.println("-----------------------------------------------");
			}
			socket.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
}
