package com.ntsakonas.picourl;

import java.nio.charset.Charset;

public class Base62Encoder {

    public static String base62Encode(long hashValue, int length) throws IllegalArgumentException {

        if (hashValue < 0) throw new IllegalArgumentException("Base62 encoder can encode only positive values");

        byte[] buffer = new byte[length];
        final int BASE = 62;

        int index = 0;
        do {
            byte mod = (byte) (hashValue % BASE);
            if (mod < 10) // 0..9 is mapped to '0'-'9'
                mod += 48;
            else if (mod < 36) // 10..35 is mapped to 'A'-'Z'
                mod += 65 - 10;
            else // 36..61 is mapped to 'a'-'z'
                mod += 97 - 36;
            buffer[index++] = mod;
            hashValue /= BASE;
        } while (index < length && hashValue > 0);

        // check if the number of bytes requested could fit the value
        if (index == length && hashValue > 0)
            throw new IllegalArgumentException("Base62 encoder cannot fit value " + hashValue + " in " + length + " digits");

        byte[] result = new byte[index];
        for (int i = 0; i < index; i++) {
            result[i] = buffer[index - 1 - i];
        }

        return new String(result, Charset.forName("ISO-8859-1"));
    }


}
