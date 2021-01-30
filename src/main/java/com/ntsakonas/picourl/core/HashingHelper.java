package com.ntsakonas.picourl.core;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;

public class HashingHelper {

    public static byte[] stringToBytes(String message) {
        return message.getBytes(Charset.forName("UTF-8"));
    }

    // public static String bytesToString(byte[] message) {
    //     return new String(message, Charset.forName("UTF-8"));
    // }
    //
    // public static byte[] hexStringToBytes(String message) {
    //     try {
    //         return Hex.decodeHex(message);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw new RuntimeException(e.getMessage());
    //     }
    // }

    public static String bytesToHexString(byte[] message) {
        return Hex.encodeHexString(message);
    }


}
