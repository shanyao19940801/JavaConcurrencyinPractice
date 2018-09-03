package com.jvm.chapter03;

public class GCTest1 {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        testAllocation();
    }

    public static void testAllocation() {
        byte[] allocation1,allocation2,allocation3,allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        System.gc();
        allocation4 = new byte[134 * _1MB];//出现一次Minor GC
        System.gc();
    }


}
