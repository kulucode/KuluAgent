package com.tpson.kuluagent.netty.server.car;

import com.tpson.kuluagent.util.ConvertUtils;

/**
 * Created by Zhangka in 2018/04/24
 */
public class CarCodec {
    public static byte[] decode(byte[] src) {
        if (src == null)
            return null;

        byte[] output = new byte[src.length];

        int j = 0;
        for (int i = 0; i < src.length; i++) {
            if (i == src.length - 1) {
                output[j++] = src[i];
            } else {
                if (src[i] == 0x7d && src[i + 1] == 0x01) {
                    output[j++] = 0x7d;
                    i++;
                } else if (src[i] == 0x7d && src[i + 1] == 0x02) {
                    output[j++] = 0x7e;
                    i++;
                } else
                    output[j++] = src[i];
            }
        }

        byte[] ret = ConvertUtils.subBytes(output, 0, j);
        return ret;
    }

    public static byte[] encode(byte[] src) {
        if (src == null)
            return null;

        byte[] output = new byte[src.length * 2 + 2];

        int j = 0;
        output[j++] = 0x7e;
        for (int i = 0; i < src.length; i++) {
            if (src[i] == 0x7e) {
                output[j++] = 0x7d;
                output[j++] = 0x02;
            } else if (src[i] == 0x7d) {
                output[j++] = 0x7d;
                output[j++] = 0x01;
            } else
                output[j++] = src[i];
        }
        output[j++] = 0x7e;

        byte[] ret = ConvertUtils.subBytes(output, 0, j);
        return ret;
    }
}
