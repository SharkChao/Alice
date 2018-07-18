package com.alice.core.util;

/**
 * Created by yuzhijun on 2018/4/3.
 */
public class Constants {
//    public static final String BASE_URL =  "http://192.168.43.133:8088";
//    public static final String BASE_URL =  "http://172.16.72.120:8080";
    public static final String BASE_URL =  "http://203.110.176.174:8085";
    public static final String PATH_URL = "/Path";
    public class HttpCode {
        public static final int HTTP_UNAUTHORIZED = 401;
        public static final int HTTP_SERVER_ERROR = 500;
        public static final int HTTP_NOT_HAVE_NETWORK = 600;
        public static final int HTTP_NETWORK_ERROR = 700;
        public static final int HTTP_UNKNOWN_ERROR = 800;
    }
    public class SpKey{
        public static final String SP_USER = "sp_user";
    }
    public class MessageType{
        public static final int warning = 0;
        public static final int repair = 1;
        public static final int error = 2;
        public static final String no_read_messgae = "0";
        public static final String readed_message = "1";
        public static final String close_messgae = "2";
    }
    public class RepairType{
        public static final String repair_new = "0";
        public static final String repair_accept = "1";
        public static final String repair_commmunicate = "2";
        public static final String repair_resolved = "3";
        public static final String repair_normal = "4";
        //角色名
        public static final String jsm_left = "请求人员";
        public static final String jsm_right = "解决人员";
    }
}
