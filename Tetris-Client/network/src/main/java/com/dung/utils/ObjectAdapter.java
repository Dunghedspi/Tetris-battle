package com.dung.utils;

/**
 * Convert bytes to object and vice versa.
 */
public interface ObjectAdapter {
    byte[] objectToBytes(Object obj);

    Object bytesToObject(byte[] bytes);
}
