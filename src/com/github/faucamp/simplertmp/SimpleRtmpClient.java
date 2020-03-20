package com.github.faucamp.simplertmp;

import com.github.faucamp.simplertmp.util.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class SimpleRtmpClient {

    public SimpleRtmpClient() {
    }

    public static void main(String args[]) {
        SyncRtmpClient mClient;
        try {
            // mClient = new SyncRtmpClient("58.200.131.2", 1935, "livetv", "hunantv");
            // mClient = new SyncRtmpClient("127.0.0.1", 1935, "abcs", "obs");
            mClient = new SyncRtmpClient("127.0.0.1", 1935, "video", "dump.flv");
            //mClient = new SyncRtmpClient("ftv.sun0769.com", 1935, "dgrtv1", "mp4:b1");
            L.w("connect start");
            mClient.connect();
        } catch (Exception ex) {
            L.w("play quit");
            return;
        }

        File file = new File("./try.flv");
        FileOutputStream fo = null;
        if (file.exists()) {
            file.delete();
        } else {
            try {
                file.createNewFile();
                fo = new FileOutputStream(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] buffer = new byte[258 * 1024]; // 32KB的缓存也会因为数据量过大而丢弃数据
        System.out.println("fo == null?: " + fo == null);
        while (true) {
            try {
                //L.w("read begin");
                int n = mClient.readAvData(buffer);
                L.w("read byte " + n);
                if (n > 0) {
                    fo.write(buffer, 0, n);
                } else if (n < 0) {
                    L.e("read failed !!! need reconnect");
                    break;
                }
            } catch (SocketTimeoutException e) {
                L.d("socket timeout read again");
                continue;
            } catch (Exception ex) {
                ex.printStackTrace();
                L.w("read quit" + ex);
                return;
            }
        }
    }
}
