package com.ntsakonas.picourl.core;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;

public class HashingHelper {

    public static byte[] stringToBytes(String message) {
        return message.getBytes(Charset.forName("UTF-8"));
    }

    public static String bytesToHexString(byte[] message) {
        return Hex.encodeHexString(message);
    }

}
