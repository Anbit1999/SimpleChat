package com.dmt.tuan.simplechat.khoa.client;

import android.os.AsyncTask;
import android.util.Log;

import com.dmt.tuan.simplechat.model.Nguoidung;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.net.Socket;

public class ServerSignUp extends AsyncTask<Void, Void, Void> {

    private Socket socket;
    private PrintWriter out;
    private Nguoidung nguoiDung;

    private Gson gson;

    public ServerSignUp(Nguoidung nguoiDung) {
        this.nguoiDung = nguoiDung;
        this.gson = new Gson();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            socket = new Socket(ServerInfo.IP_SERVER, ServerInfo.PORT_SERVER_SIGNUP);
            Log.d("messs", "da ket noi");
            out = new PrintWriter(socket.getOutputStream(), true);
            String json = gson.toJson(nguoiDung);
            out.println(json);
            socket.close();
            Log.d("client", "thanh cong");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("client", "loi");
        }
        return null;
    }
}
