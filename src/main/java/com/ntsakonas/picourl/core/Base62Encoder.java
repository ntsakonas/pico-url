package com.ntsakonas.picourl.core;

import java.nio.charset.Charset;

/*
    Encodes a long value to a base62 number.
    Base62 number is made up of digits 0..9, A..Z, a..z
    The caller must provide the number of digits capable of holding the number representation.
*/
public class Base62Encoder {

    public static String base62Encode(long value, int length) throws IllegalArgumentException {

        if (value < 0) throw new IllegalArgumentException("Base62 encoder can encode only positive values");

        byte[] buffer = new byte[length];
        final int BASE = 62;

        int index = 0;
        do {
            byte mod = (byte) (value % BASE);
            if (mod < 10) // 0..9 is mapped to '0'-'9'
                mod += 48;
            else if (mod < 36) // 10..35 is mapped to 'A'-'Z'
                mod += 65 - 10;
            else // 36..61 is mapped to 'a'-'z'
                mod += 97 - 36;
            buffer[index++] = mod;
            value /= BASE;
        } while (index < length && value > 0);

        // check if the number of bytes requested could fit the value
        if (index == length && value > 0)
            throw new IllegalArgumentException("Base62 encoder cannot fit value " + value + " in " + length + " digits");

        byte[] result = new byte[index];
        for (int i = 0; i < index; i++) {
            result[i] = buffer[index - 1 - i];
        }

        return new String(result, Charset.forName("ISO-8859-1"));
    }


}
