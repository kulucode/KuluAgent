/**
 * LiangKun 2017.04.04 created
 * */
package com.tpson.kuluagent.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.List;

public class ConvertUtils {
    public static float getFloat(float ft, int digit) {
        // digit 设置位数
        int roundingMode = 4;// 表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        BigDecimal bigDecimal = new BigDecimal((double) ft);
        bigDecimal = bigDecimal.setScale(digit, roundingMode);
        return bigDecimal.floatValue();
    }

    public static float getFloat2(float ft, int digit) {
        // digit 设置位数
        int tmp = (int) Math.pow(10, digit);
        float b = (float) (Math.round(ft * tmp)) / tmp;
        return b;
    }

    public static String[] getStringArrayFromList(List<String> inFileList) {
        if (inFileList.size() < 1)
            return null;
        String[] inFiles = new String[inFileList.size()];
        for (int i = 0; i < inFileList.size(); i++) {
            inFiles[i] = inFileList.get(i);
        }
        return inFiles;
    }

    public static String getSizeString(long size) {
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size * 100 / 1024;
        }
        return String.valueOf((size / 100)) + "." + ((size % 100) < 10 ? "0" : "")
                + String.valueOf((size % 100)) + "MB";
    }

    public static String byteToString(byte[] bytes) {
        int length = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == '\0') {
                length = i;
                break;
            }
        }
        length = length < 0 ? 0 : length;
        return new String(bytes, 0, length);
    }

    public static byte[] ObjectToByte(Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);

            bytes = byteArrayOutputStream.toByteArray();

            byteArrayOutputStream.close();
            objectOutputStream.close();
        } catch (Exception e) {
            System.out.println("translation " + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    public static Object ByteToObject(byte[] bytes) {

        if (bytes == null)
            return null;
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation " + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] data = new byte[count];
        for (int i = begin; i < begin + count && i < src.length; i++)
            data[i - begin] = src[i];
        return data;
    }

    public static byte[] subBytes(byte[] src, int begin) {
        byte[] data = new byte[src.length - begin];
        for (int i = begin; i < data.length && i < src.length; i++)
            data[i - begin] = src[i];
        return data;
    }

    public static byte[] combineBytes(byte[]... inputs) {
        int size = 0;
        for (int i = 0; i < inputs.length; i++) {
            size += inputs[i].length;
        }
        byte[] output = new byte[size];
        int lastPos = 0;
        for (int i = 0; i < inputs.length; i++) {
            System.arraycopy(inputs[i], 0, output, lastPos, inputs[i].length);
            lastPos += inputs[i].length;
        }

        return output;
    }

    public static byte[] hexToByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String byteToHex(byte[] buf) {
        final String HEX = "0123456789ABCDEF";
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            result.append(HEX.charAt((buf[i] >> 4) & 0x0f)).append(HEX.charAt(buf[i] & 0x0f));
        }
        return result.toString();
    }

    public static String byteToHex0x(byte[] buf) {
        final String HEX = "0123456789ABCDEF";
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            result.append("0x").append(HEX.charAt((buf[i] >> 4) & 0x0f))
                    .append(HEX.charAt(buf[i] & 0x0f)).append(" ");
        }
        return result.toString();
    }

    public static String byteToHex(int[] buf) {
        final String HEX = "0123456789ABCDEF";
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            result.append(HEX.charAt((buf[i] >> 4) & 0x0f)).append(HEX.charAt(buf[i] & 0x0f) + " ");
        }
        return result.toString();
    }

    public static int byteToInt(byte[] b) {
        int ret = 0;
        ret |= (b[0] << 24 & 0xff << 24);
        ret |= (b[1] << 16 & 0xff << 16);
        ret |= (b[2] << 8 & 0xff << 8);
        ret |= (b[3] & 0xff);
        return ret;
    }

    public static int[] double2Int(double d) {
        String s = Double.toString(d);
        String[] a = s.split("[.]");
        int i = Integer.valueOf(a[0]);
        int decimal = Integer.valueOf(a[1]);
        return new int[] { i, decimal };
    }

    public static int[] double2Int(float d) {
        String s = Float.toString(d);
        String[] a = s.split("[.]");
        int i = Integer.valueOf(a[0]);
        int decimal = Integer.valueOf(a[1]);
        return new int[] { i, decimal };
    }

    public static int formInt(byte[] in, int start, int dataLen) {
        if (in == null || in.length - start < dataLen || dataLen > 4)
            return -1;
        int ret = 0;

        int shift = 0;
        while (dataLen >= 1) {
            shift = (dataLen - 1) * 8;
            if (dataLen >= 1) {
                ret = (ret | ((0xff << shift) & (in[start] << shift)));
            }
            start++;
            dataLen--;
        }

        return ret;
    }

    public static int formLong(byte[] in, int start, int dataLen) {
        if (in == null || in.length - start < dataLen || dataLen > 8)
            return -1;
        int ret = 0;

        int shift = 0;
        while (dataLen >= 1) {
            shift = (dataLen - 1) * 8;
            if (dataLen >= 1) {
                ret = (ret | ((0xff << shift) & (in[start] << shift)));
            }
            start++;
            dataLen--;
        }

        return ret;
    }

    public static double int2Double(int intPart, int decimal) {
        String si = String.format("%d.%d", intPart, decimal);
        return Double.valueOf(si);
    }

    public static int formIntForBitsBigEndian(int in, int endBit, int dataLen) {

        if (endBit < dataLen - 1)
            return -1;
        int mask = 0;

        for (int i = 0; i < dataLen; i++) {
            mask = mask << 1;
            mask |= 0x1;
        }

        in = in >> (endBit - dataLen + 1);
        return (in & mask);
    }

    /**
     * example:0x10,110 int ret=fillBitsBigEndian(0, 3, 1, 2);
     * ret=fillBitsBigEndian(ret, 2, 3, 2);
     */
    public static int fillBitsBigEndian(int out, int value, int startBit, int dataLen) {
        int mask = 0;

        for (int i = 0; i < dataLen; i++) {
            mask = mask << 1;
            mask |= 0x1;
        }
        return (out | ((value & mask) << startBit));
    }

    public static byte[] int2BytesBigEndian(int i) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (0xff & i);
        bytes[2] = (byte) ((0xff00 & i) >> 8);
        bytes[1] = (byte) ((0xff0000 & i) >> 16);
        bytes[0] = (byte) ((0xff000000 & i) >> 24);
        return bytes;
    }

    public static byte[] int2BytesBigEndian(int input, int len) {
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[len - i - 1] = (byte) (((0xff << (i * 8)) & input) >> (i * 8));
        }
        return bytes;
    }

    public static byte[] long2BytesBigEndian(long input, int len) {
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[len - i - 1] = (byte) (((0xff << (i * 8)) & input) >> (i * 8));
        }
        return bytes;
    }

    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString();
        /*
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1)
                : temp.toString();*/
    }
    
    public static String int2Hex(int i) {
        return Integer.toHexString(i);
    }

    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }
}
