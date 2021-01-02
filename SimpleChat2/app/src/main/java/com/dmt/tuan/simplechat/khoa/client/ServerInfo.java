package com.dmt.tuan.simplechat.khoa.client;

import com.dmt.tuan.simplechat.model.Nguoidung;

public class ServerInfo {

    public static final int PORT_SERVER_SIGNUP = 9000;
    public static final int PORT_SERVER_LOGIN = 9001;
    public static final int PORT_SERVER_FRIEND_MANAGER = 9002;

    public static Nguoidung nguoidung = new Nguoidung();

    public static final String IP_SERVER = "13.250.38.182";

    /**
     * up ec2 dùng địa chỉ Public IPv4 address của ec2 đang chạy server
     *
     */
//    public static final String IP_SERVER = "13.250.38.182";

}
