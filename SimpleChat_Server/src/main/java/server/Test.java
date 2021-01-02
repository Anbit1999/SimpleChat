package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.gson.Gson;

import dao.MyDatabase;
import dao.NguoiDungDAO;
import entities.NguoiDung;
import entities.User;

public class Test {

	public static void main(String[] args) throws Exception {

//		Socket socket = new Socket("localhost", 9001);
//		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//		Scanner in = new Scanner(socket.getInputStream());
//
//		NguoiDung nguoiDung = new NguoiDung("demo@gmail.com", "12345");
//		out.println(new Gson().toJson(nguoiDung));
//		if(in.hasNext())
//			System.out.println(in.nextLine());
		
//		NguoiDung nguoiDung = new NguoiDung("demo@gmail.com", "12345", "demouser");
//		out.println(new Gson().toJson(nguoiDung));
		
		
		
		
		
		
//		NguoiDung nguoiDung = new NguoiDung("anhvbt@gmail.com", "12345", "anhvbt");
//		System.out.println(nguoiDung);
//		out.println(new Gson().toJson(nguoiDung));


		//		DynamoDBMapper mapper = MyDatabase
		//				.getInstance("AKIA3LBQEI65LVPVS2PR", "5dRkDnazP5KmOAsp8ZY2DjXqyx3tIO2nYPSotT+Q")
		//				.getMapper();
		//		NguoiDungDAO dao = NguoiDungDAO.getInstance(mapper);
		//		System.out.println(dao.isValidLogin("user2222@gmail.com", "12345"));
		
		
//		DynamoDBMapper mapper = MyDatabase
//				.getInstance("AKIA3LBQEI65LVPVS2PR", "5dRkDnazP5KmOAsp8ZY2DjXqyx3tIO2nYPSotT+Q")
//				.getMapper();
//		NguoiDungDAO dao = NguoiDungDAO.getInstance(mapper);
//		System.out.println(dao.isValidLogin("anhvbt@gmail.com", "12345"));
//		System.out.println(dao.isValidLogin("anhvbt@gmail.com", "12345"));
		
		
		DynamoDBMapper mapper = MyDatabase
				.getInstance("AKIA3LBQEI65LVPVS2PR", "5dRkDnazP5KmOAsp8ZY2DjXqyx3tIO2nYPSotT+Q")
				.getMapper();
		NguoiDungDAO dao = NguoiDungDAO.getInstance(mapper);
		
		NguoiDung nguoiDung = new NguoiDung("nmhung31051999@gmail.com", "0933871560", "hung");
		dao.addNguoiDung(nguoiDung);
//		Set<String> friends = new HashSet<String>();
//		friends.add("anhvbt12345@gmail.com");
//		friends.add("anhvbt@gmail.com");
//		friends.add("user@gmail.com");
//		nguoiDung.setFriends(friends);
//		dao.addNguoiDung(nguoiDung);
//		System.out.println(dao.getFriends("anhvbt@gmail.com", "12345"));
//		System.out.println(dao.updateFriends(nguoiDung));
		
		
		
	}


}
