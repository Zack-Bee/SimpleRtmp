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
            //mClient = new RtmpConnection("livestreaming.itworkscdn.net", 1935, "smc4sportslive", "smc4tv_360p");
            mClient = new SyncRtmpClient("live.hkstv.hk.lxdns.com", 1935, "live", "hks");
            //mClient = new SyncRtmpClient("ftv.sun0769.com", 1935, "dgrtv1", "mp4:b1");
            L.w("connect start");
            mClient.connect();
        } catch (Exception ex) {
            L.w("play quit");
            return;
        }

        File file = new File("/home/jsyan/test.flv");
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
        byte[] buffer = new byte[32 * 1024];
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
