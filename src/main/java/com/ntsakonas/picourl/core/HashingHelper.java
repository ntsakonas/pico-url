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
