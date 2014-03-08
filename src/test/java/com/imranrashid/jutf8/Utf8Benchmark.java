package com.imranrashid.jutf8;

import java.util.Random;

import com.google.caliper.Benchmark;

public class Utf8Benchmark extends Benchmark {

    private static String genRandomString(int maxChar, int nChars) {
        StringBuilder sb = new StringBuilder();
        Random rng = new Random(54);
        for(int i = 0; i < nChars;i++){
            sb.append((char)rng.nextInt(maxChar));
        }
        return sb.toString();
    }

    static String asciiString = genRandomString(127, 10);
    static Utf8 asciiUtf8 = new Utf8(asciiString);

//    public int timeForwardScanAsciiString(int reps) {
//        int result = 0;
//        int l = asciiString.length();
//        for (int i = 0; i < reps; i++) {
//            for (int j = 0; j < l; j++){
//                result ^= asciiString.charAt(j);
//            }
//        }
//        return result;
//    }
//    
//    public int timeForwardScanAsciiUtf8(int reps) {
//        int result = 0;
//        int l = asciiUtf8.length();
//        for (int i = 0; i < reps; i++) {
//            for (int j = 0; j < l; j++){
//                result ^= asciiUtf8.charAt(j);
//            }
//        }
//        return result;
//    }
//    
//    public int timeForwardScanDecodeAsciiStringUtf8(int reps) {
//        int result = 0;
//        int l = asciiString.length();
//        for (int i = 0; i < reps; i++) {
//            String decoded = new String(asciiUtf8.bytes, asciiUtf8.start, asciiUtf8.end - asciiUtf8.start, Utf8.utf8Charset);
//            for (int j = 0; j < l; j++){
//                result ^= decoded.charAt(j);
//            }
//        }
//        return result;
//    }
    
    
    public int timeA(int reps) {
        int result = 0;
        int l = asciiUtf8.length();
        for (int i = 0; i < reps; i++) {
            for (int j = 0; j < l; j++){
                result ^= asciiUtf8.a(j);
            }
        }
        return result;
    }
    
    public int timeB(int reps) {
        int result = 0;
        int l = asciiUtf8.length();
        for (int i = 0; i < reps; i++) {
            for (int j = 0; j < l; j++){
                result ^= asciiUtf8.b(j);
            }
        }
        return result;
    }
    
    public int timeC(int reps) {
        int result = 0;
        int l = asciiUtf8.length();
        for (int i = 0; i < reps; i++) {
            for (int j = 0; j < l; j++){
                result ^= asciiUtf8.c(j);
            }
        }
        return result;
    }

    public int timeD(int reps) {
        int result = 0;
        int l = asciiUtf8.length();
        for (int i = 0; i < reps; i++) {
            for (int j = 0; j < l; j++){
                result ^= asciiUtf8.d(j);
            }
        }
        return result;
    }

    public int timeE(int reps) {
        int result = 0;
        int l = asciiUtf8.length();
        for (int i = 0; i < reps; i++) {
            for (int j = 0; j < l; j++){
                result ^= asciiUtf8.e(j);
            }
        }
        return result;
    }

    public int timeF(int reps) {
        int result = 0;
        int l = asciiUtf8.length();
        for (int i = 0; i < reps; i++) {
            for (int j = 0; j < l; j++){
                result ^= asciiUtf8.f(j);
            }
        }
        return result;
    }
    
    
    public int timeG(int reps) {
        int result = 0;
        int l = asciiUtf8.length();
        for (int i = 0; i < reps; i++) {
            for (int j = 0; j < l; j++){
                result ^= asciiUtf8.g(j);
            }
        }
        return result;
    }
    
    
    public int timeH(int reps) {
        int result = 0;
        int l = asciiUtf8.length();
        for (int i = 0; i < reps; i++) {
            for (int j = 0; j < l; j++){
                result ^= asciiUtf8.h(j);
            }
        }
        return result;
    }


    
    
    public static void main(String[] args) {
        Utf8Benchmark u = new Utf8Benchmark();
//        int t = u.timeForwardScanAsciiUtf8(500000000);
//        int t = u.timeForwardScanAsciiString(500000000);
//        System.out.println(t);
    }
}
