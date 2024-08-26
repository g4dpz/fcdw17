package uk.me.g4dpz.fcdwrawdata.utils;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServiceUtility {

    private static final int HEX_0X0F = 0x0F;



    public static String convertHexBytePairToBinary(final String hexString) {
        final StringBuffer sb = new StringBuffer();

        for (int i = 0; i < hexString.length(); i += 2) {
            final String hexByte = hexString.substring(i, i + 2);
            final int hexValue = Integer.parseInt(hexByte, 16);
            sb.append(StringUtils.leftPad(Integer.toBinaryString(hexValue), 8,
                    "0"));
        }
        return sb.toString();
    }

    public static String calculateDigest(final String hexString,
                                         final String authCode, final Integer utf)
            throws NoSuchAlgorithmException {

        String digest;

        final MessageDigest md5 = MessageDigest.getInstance("MD5");

        if (utf == null) {

            md5.update(hexString.getBytes());
            md5.update(":".getBytes());
            digest = convertToHex(md5.digest(authCode.getBytes()));
        }
        else if (utf.intValue() == 8) {
            md5.update(hexString.getBytes(Charset.forName("UTF8")));
            md5.update(":".getBytes(Charset.forName("UTF8")));
            digest = convertToHex(md5.digest(authCode.getBytes(Charset
                    .forName("UTF8"))));
        }
        else {
            md5.update(hexString.getBytes(Charset.forName("UTF16")));
            md5.update(":".getBytes(Charset.forName("UTF16")));
            digest = convertToHex(md5.digest(authCode.getBytes(Charset
                    .forName("UTF16"))));
        }

        return digest;
    }

    private static String convertToHex(final byte[] data) {
        final StringBuffer buf = new StringBuffer();
        for (final byte element : data) {
            int halfbyte = (element >>> 4) & HEX_0X0F;
            int twoHalfs = 0;
            do {
                if (0 <= halfbyte && halfbyte <= 9) {
                    buf.append((char)('0' + halfbyte));
                }
                else {
                    buf.append((char)('a' + (halfbyte - 10)));
                }
                halfbyte = element & HEX_0X0F;
            }
            while (twoHalfs++ < 1);
        }
        return buf.toString();
    }
}
