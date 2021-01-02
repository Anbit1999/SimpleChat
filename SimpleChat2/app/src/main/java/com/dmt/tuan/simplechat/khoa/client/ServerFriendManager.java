package com.dmt.tuan.simplechat.khoa.client;

import android.os.AsyncTask;
import android.util.Log;

import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.net.Socket;

public class ServerFriendManager extends AsyncTask<Void, Void, Void> {

    private Socket socket;
    private PrintWriter out;
    private Nguoidung nguoiDung;

    private Gson gson;

    public ServerFriendManager(Nguoidung nguoiDung) {
        this.nguoiDung = nguoiDung;
        this.gson = new Gson();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            socket = new Socket(ServerInfo.IP_SERVER, ServerInfo.PORT_SERVER_FRIEND_MANAGER);
            out = new PrintWriter(socket.getOutputStream(), true);
            Log.d("messs", "da ket noi");

            Nguoidung nguoidung = new Nguoidung(ServerInfo.nguoidung.getEmail());
            nguoidung.setFriends(ServerInfo.nguoidung.getFriends());

            out.println(gson.toJson(nguoidung));
            socket.close();
            Log.d("messs", "thanh cong");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("messs", "loi");
        }

        return null;
    }
}
