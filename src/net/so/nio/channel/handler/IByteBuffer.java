package net.so.nio.channel.handler;

import net.so.nio.channel.io.ByteBuffer;
import net.so.nio.channel.io.OutputWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class IByteBuffer implements ByteBuffer {

    private byte[] array;
    public OutputWriter writer = new OutputWriter();

    protected IByteBuffer(byte[] array){
        this.array=array;
    }

    public int available(){
        return array.length;
    }

    public byte readOnly(){
        if(available() <= 0) throw new IndexOutOfBoundsException("No bytes available. ByteBuffer.available()");
        byte b = array[available()-1];

        byte[] array = new byte[available()-1];

        for(int i = 0; i<available()-1; i++){
            array[i] = this.array[i];
        }

        this.array=array;

        return b;
    }

    public byte[] readAll(){
        if(available() <= 0) throw new IndexOutOfBoundsException("No bytes available. ByteBuffer.available()");
        byte[] array = this.array;
        this.array=null;
        return array;
    }

    public char readChar(){
        if(available() <= 0) throw new IndexOutOfBoundsException("No bytes available. ByteBuffer.available()");
        return (char)(int)readOnly();
    }

    public char readSafetyChar(){
        if(available() <= 0) throw new IndexOutOfBoundsException("No bytes available. ByteBuffer.available()");
        byte b = array[available()-1];
        int max = Character.MAX_VALUE;
        int min = Character.MIN_VALUE;

        if(max >= b && b <= min){
            readChar();
            return (char) (int) b;
        }else{
            throw new NullPointerException("No Safety Character");
        }
    }

    public int readInteger(){
        if(available() <= 0) throw new IndexOutOfBoundsException("No bytes available. ByteBuffer.available()");
        return Integer.parseInt("" + (char)(int)readOnly());
    }

    public float readFloat(){
        if(available() <= 0) throw new IndexOutOfBoundsException("No bytes available. ByteBuffer.available()");
        return Float.parseFloat("" + (char)(int)readOnly());
    }

    public double readDouble() {
        if(available() <= 0) throw new IndexOutOfBoundsException("No bytes available. ByteBuffer.available()");
        return Double.parseDouble("" + (char)(int)readOnly());
    }

    public long readLong() {
        if(available() <= 0) throw new IndexOutOfBoundsException("No bytes available. ByteBuffer.available()");
        return Long.parseLong("" + (char)(int)readOnly());
    }

    public String readString() {
        if(available() <= 0) throw new IndexOutOfBoundsException("No bytes available. ByteBuffer.available()");
        StringBuffer buffer = new StringBuffer();
        while(available() != 0){
            try{
                buffer.append(readSafetyChar());
            }catch(Exception e){
                break;
            }
        }
        return buffer.toString();
    }

    @Override
    public void writeOnly(byte b) throws Throwable {
        writer.write(b);
    }

    @Override
    public void writeAll(byte[] b) throws Throwable {
        writer.write(b);
    }

    @Override
    public void writeChar(char c) throws Throwable {
        writer.write((byte)(int)c);
    }

    @Override
    public void writeInteger(int i) throws Throwable {
        writeString(""+i);
    }

    @Override
    public void writeFloat(float f) throws Throwable {
        writeString(""+f);
    }

    @Override
    public void writeDouble(double d) throws Throwable {
        writeString(""+d);
    }

    @Override
    public void writeLong(long l) throws Throwable {
        writeString(""+l);
    }

    @Override
    public void writeString(String s) throws Throwable {
        writer.write(s.getBytes());
    }

    public void writeString(String s, Charset charset) throws Throwable {
        writer.write(s.getBytes(charset));
    }

    @Deprecated
    public void pushArrayFully(byte[] array) throws IOException {
        if(available() <= 0) throw new IOException("This array is not empty");
        this.array = array;
    }

    public void pushArrayAfter(byte[] array) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(this.array);
        out.write(array);
        this.array=array;
    }

    public byte[] pullWriter(){
        byte[] array = writer.toByteArray();
        clearWriter();
        return array;
    }

    public void clearWriter(){
        writer.clear();
    }

}
