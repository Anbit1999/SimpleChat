package com.dmt.tuan.simplechat.khoa.client;

import android.os.AsyncTask;
import android.util.Log;

import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerLogin extends AsyncTask<Void, Void, Void> {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Nguoidung nguoiDung;

    private Gson gson;

    private String result;

    public ServerLogin(Nguoidung nguoiDung) {
        this.nguoiDung = nguoiDung;
        this.gson = new Gson();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            socket = new Socket(ServerInfo.IP_SERVER, ServerInfo.PORT_SERVER_LOGIN);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(gson.toJson(nguoiDung));
            if(in.hasNext())
                this.result = in.nextLine();
            socket.close();
            Log.d("client", "thanh cong");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("client", "loi");
        }
        return null;
    }

    public String getResult() {
        return result;
    }

}
