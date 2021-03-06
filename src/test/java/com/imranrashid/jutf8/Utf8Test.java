package com.imranrashid.jutf8;

import static org.junit.Assert.*;
import static com.imranrashid.jutf8.Utf8.bs;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testAscii() {
        for (String s: new String[]{"abc", "oogabooga", "aaAFKANdafan"}){
            stringTest(s);
        }
    }

    @Test
    public void testFullUnicode() {
        // characters pulled from http://stackoverflow.com/a/6066442/1442961
        String[] ss = new String[]{
                "\u0021 blah blah",	//normally ascii
                "ooga \u00b6 booga \u00b6\u00b6 \u00b6", // 2bytes in utf8, one byte in utf16
                "\u00161",  //2 bytes in utf8 & 2 bytes in utf16
                "foo\u2031bar" //3bytes
                //TODO better 3 byte test.  current implementation should be failing, we're not taking 2nd byte correctly
                //TODO 4-6 bytes, which are surrogate pairs, and a mess in java's api
        };
        for (String s: ss) {
            stringTest(s);
        }
    }

    @Test
    public void testFailOnInvalidBits() {
        fail("pending");
    }

    public void stringTest(String s) {
        byte[] bytes = s.getBytes(Utf8.utf8Charset);
        Utf8 u = new Utf8(bytes);
        assertEquals("wrong length for " + s, s.length(), u.length());
        for (int i = 0; i < s.length(); i++) {
            assertEquals("wrong char at " + i + " in " + s, s.charAt(i), u.charAt(i));
        }
    }

    @Test
    public void testSubstring() {
        fail("pending");
    }

    @Test
    public void testIsContinutation() {
        assertEquals(false, Utf8.isContinutation((byte) 0));
        assertEquals(false, Utf8.isContinutation((byte) 127));
        assertEquals(true, Utf8.isContinutation((byte) bs("10000000")));
        assertEquals(true, Utf8.isContinutation((byte) bs("10101111")));
        assertEquals(false, Utf8.isContinutation((byte) bs("11000000")));
    }

    @Test
    public void testNBytes() {
        checkNBytes(1, (byte) 0);
        checkNBytes(1, (byte) 127);
        checkNBytes(2, (byte) bs("11000000"));
        checkNBytes(2, (byte) bs("11011010"));
        checkNBytes(3, (byte) bs("11100000"));
        checkNBytes(3, (byte) bs("11101010"));
        checkNBytes(4, (byte) bs("11110000"));
        checkNBytes(4, (byte) bs("11110010"));
        checkNBytes(5, (byte) bs("11111000"));
        checkNBytes(5, (byte) bs("11111010"));
        checkNBytes(6, (byte) bs("11111100"));
        checkNBytes(6, (byte) bs("11111100"));
    }
    
    void checkNBytes(int exp, byte b) {
        assertEquals(exp, Utf8.nBytes(b));
        assertEquals(exp, Utf8.tableNbytes(b));
    }

}
