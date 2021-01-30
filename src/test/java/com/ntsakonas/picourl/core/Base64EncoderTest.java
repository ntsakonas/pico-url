/*
    PicoUrl - Practicing on the design of a URL shortener service.

    Copyright (C) 2021, Nick Tsakonas


    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.ntsakonas.picourl.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Base64EncoderTest {

    @Test
    public void encodeFullDigitsSet() {
        for (int i = 0; i < 62; i++) {
            String encoded = Base62Encoder.base62Encode(i, 1);
            assertThat(encoded).isEqualTo(base62digitFor(i));
        }
    }

    private String base62digitFor(int i) {
        final String[] digits = {
                // 0 - 9
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                // 10 - 35
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                // 36 - 61
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        };
        return digits[i];
    }


    @Test
    public void encodeSmallNumbers() {
        // powers of 62
        // 11157 -> 2
        long value = 2 * powerOf62(2) + 55 * powerOf62(1) + 59;
        assertThat(Base62Encoder.base62Encode(value, 8)).isEqualTo("2tx");

        // 4,301,061 -> I2tx
        value = 18 * powerOf62(3) + 2 * powerOf62(2) + 55 * powerOf62(1) + 59;
        assertThat(Base62Encoder.base62Encode(value, 8)).isEqualTo("I2tx");

        // 905,657,557 -> zI2tx
        value = 61 * powerOf62(4) + 18 * powerOf62(3) + 2 * powerOf62(2) + 55 * powerOf62(1) + 59;
        assertThat(Base62Encoder.base62Encode(value, 8)).isEqualTo("zI2tx");
    }


    @Test
    public void encodeBigNumbers() {
        // powers of 62
        // the maximum number we want to encode in 8 bytes
        // 17,592,186,044,415 -> 4ziepp2F
        long value = 0xfffffffffffL;

        assertThat(Base62Encoder.base62Encode(value, 8)).isEqualTo("4ziepp2F");

        // this is the same number constructed piece by piece.
        value = 4 * powerOf62(7) + 61 * powerOf62(6) + 44 * powerOf62(5) + 40 * powerOf62(4) +
                51 * powerOf62(3) + 51 * powerOf62(2) + 2 * powerOf62(1) + 15;

        assertThat(Base62Encoder.base62Encode(value, 8)).isEqualTo("4ziepp2F");
    }

    @Test
    public void encodeBigNumbersInLessSpace() {
        // make sure we raise an error if we cannot fit the number in the requested number of digits
        // this numbert requires 8 digits
        long value = 0xfffffffffffL;

        assertThatThrownBy(() -> Base62Encoder.base62Encode(value, 7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Base62 encoder cannot fit value");

    }

    @Test
    public void verifyWeCannotEncodeNegativeNumbers() {
        assertThatThrownBy(() -> Base62Encoder.base62Encode(-1, 8))
                .isInstanceOf(IllegalArgumentException.class);
    }


    long powerOf62(int exp) {
        return (long) Math.pow(62, exp);
    }
}
