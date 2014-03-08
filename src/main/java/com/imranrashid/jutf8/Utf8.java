package com.imranrashid.jutf8;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Utf8 implements CharSequence {
    final byte[] bytes;
    final int start;
    final int end;
    final int length; 

    private int nextChar = 0;
    private int nextBytePos = 0;

    public static final Charset utf8Charset = Charset.forName("UTF-8");

    public Utf8(byte[] bytes, int start, int end, int length) {
        this.bytes = bytes;
        this.start = start;
        this.end = end;
        this.length = length;
    }

    public Utf8(byte[] bytes, int start, int end) {
        this(bytes, 0, bytes.length, computeLength(bytes, start, end));
    }

    public Utf8(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }
    
    public Utf8(String s) {
        this(s.getBytes(Utf8.utf8Charset));
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        char r = (char) bytes[index];
        return r;
        //this allows fast movement forward through a string, char by char
        // but would be nice to allow backward movement, plus smarter about "nearby" jumps
//        if (index == nextChar) {
//            return decodeNext();
//        } else {
//            return replayDecodingTill(index);
//        }
    }
    
    public char a(int index) {
        char r = (char) bytes[index];
        return r;
    }
    
    public char b(int index) {
        char r = (char) bytes[index];
        if (r <= 500)
            return r;
        else
            return (char) -1;
    }
    
    
    
    public char c(int index) {
        byte b = bytes[index];
        int n = tableNbytes(b);
        if (n <= 500){
            char r = (char) b;
            return r;
        }
        else
            return (char) -1;
    }
    
    public char d(int index) {
        byte b = bytes[index];
        int n = nBytes(b);
        if (n <= 500){
            char r = (char) b;
            return r;
        }
        else
            return (char) -1;
    }


    public char e(int index) {
        byte b = bytes[index];
        int n = tableNbytes(b);
        if (n == 1){
            char r = (char) b;
            return r;
        }
        else
            return (char) -1;
    }

    public char f(int index) {
        if (index == nextChar) {
            char r = (char) bytes[nextBytePos++];
            ++nextChar;
            return r;
        } else if (index == 0){
            nextBytePos = 1;
            nextChar = 1;
            return (char) bytes[0];
        } else {
            throw new RuntimeException("asked for index = " + index + " when nextChar = " + nextChar);
        }
    }
    
    
    public char g(int index) {
        if (index != 17) {
            return (char) bytes[index];
        } else {
            return (char) bytes[17];
        }
    }
    
    public char h(int index) {
        byte b = bytes[index];
        int n = tableNbytes(b);
        nextBytePos++;
        ++nextChar;
        if (n == 1){
            char r = (char) b;
            return r;
        }
        else
            return (char) -1;
    }



    

    private char replayDecodingTill(int targetIndex) {
        nextChar = 0;
        nextBytePos = 0;
        char r = 0;
        while (nextChar <= targetIndex) {
            r = decodeNext();
        }
        return r;
    }

    private char decodeNext() {
        byte b = bytes[nextBytePos++];
        int nbytes = tableNbytes(b);
        if (nbytes == 1) {
            char r = (char) b;
            ++nextChar;
            return r;
        } else if (nbytes <= 3){
            //move the extra bytes of b to the high bits
            int maskShift = (1 + nbytes);
            b = (byte) ((byte) (b << maskShift) >>> maskShift);
            int shiftLeft = (nbytes - 1) * 6;
            int r = b << shiftLeft;
            for (int p = 1; p < nbytes; p++) {
                byte n = bytes[nextBytePos++];
                if (!isContinutation(n)) {
                    throw new RuntimeException("bad byte");	//TODO better error handling
                }
                r |= n & REV_CONTINUATION_MASK;
            }
            ++nextChar;
            return (char) r;
        } else {
            throw new RuntimeException("don't support " + nbytes + " bytes per char yet :(");
        }
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        // TODO Auto-generated method stub
        return null;
    }

    public static int computeLength(byte[] bytes, int start, int end) {
        return utf8Charset.decode(ByteBuffer.wrap(bytes, start, end - start)).toString().length();
    }

    final static byte CONTINUATION_MASK = (byte) bs("11000000");
    final static byte REV_CONTINUATION_MASK = (byte) ~CONTINUATION_MASK;
    final static byte CONTINUATION_BITS = (byte) bs("10000000");

    static boolean isContinutation(byte b) {
        return (b & CONTINUATION_MASK) == CONTINUATION_BITS;
    }

    static byte NBYTES_MASK = (byte) bs("10000000");
    static int nBytes(byte b) {
        int n = 0;
        int more = (b & NBYTES_MASK) >> 7;
        if (more == 0) {
            return 1;
        }
        b <<= 1;
        while (more != 0){
            more = (b & NBYTES_MASK) >> 7;
            b <<= 1;
            n += 1;
        }
        return n;
    }

    static int extractLeadingBits(byte b, int nbytes) {
        int maskShift = (1 + nbytes);
        b = (byte) ((byte) (b << maskShift) >>> maskShift);
        int shiftLeft = (nbytes - 1) * 6;
        int r = b << shiftLeft;
        return r;
    }

    static int bs(String s) {
        //in java 7, binary literals are supported, but meanwhile ...
        return Integer.parseInt(s, 2);
    }
    
    private static int[] buildLeadByteToNBytesTable() {
        int[] leadByteToNBytes = new int[256];
        for (int i = 0; i < 256; i++) {
            byte b = (byte) i;
            leadByteToNBytes[i] = nBytes(b);
        }
        return leadByteToNBytes;
    }
    private static final int[] leadByteToNBytes = buildLeadByteToNBytesTable();
    static final int LOW_MASK = bs("11111111");
    static final int tableNbytes(byte b) {
        return leadByteToNBytes[b & LOW_MASK];
    }
    
    private static int[] buildLeadByteToHighBitsTable() {
        int[] leadByteToHighBits = new int[256];
        for (int i = 0; i < 256; i++) {
            byte b = (byte) i;
            leadByteToHighBits[i] = extractLeadingBits(b, nBytes(b));
        }
        return leadByteToHighBits;
    }
    private static final int[] leadByteToHighBits = buildLeadByteToHighBitsTable();
    static final int tableHighBits(byte b) {
        return leadByteToHighBits[b & LOW_MASK];
    }


}
