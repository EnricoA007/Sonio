package net.so.nio.channel.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ByteBuffer {

    public int available() throws Throwable;
    public byte readOnly() throws Throwable;
    public byte[] readAll() throws Throwable;
    public char readChar() throws Throwable;
    public char readSafetyChar() throws Throwable;
    public int readInteger() throws Throwable;
    public float readFloat() throws Throwable;
    public double readDouble() throws Throwable;
    public long readLong() throws Throwable;
    public String readString() throws Throwable;

    public void writeOnly(byte b) throws Throwable;
    public void writeAll(byte[] b) throws Throwable;
    public void writeChar(char c) throws Throwable;
    public void writeInteger(int i) throws Throwable;
    public void writeFloat(float f) throws Throwable;
    public void writeDouble(double d) throws Throwable;
    public void writeLong(long l) throws Throwable;
    public void writeString(String s) throws Throwable;

    @Deprecated
    public void pushArrayFully(byte[] array) throws Throwable;
    public void pushArrayAfter(byte[] array) throws Throwable;


}
