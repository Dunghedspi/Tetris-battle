package com.dung.utils;

import java.io.*;

/**
 * Converts object to bytes and vice versa based on basic java Serializable feature.
 */
public class SerializableObjectAdapter implements ObjectAdapter {
    @Override
    public byte[] objectToBytes(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        byte[] resultBytes = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            resultBytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return resultBytes;
    }

    @Override
    public Object bytesToObject(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        Object resultObject = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            resultObject = objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObject;
    }
}
