package net.so.nio.channel.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputWriter extends OutputStream  {

    private ByteArrayOutputStream out = new ByteArrayOutputStream();

    public OutputWriter(){}

    @Override
    public void write(int b) throws IOException {
        this.write(new byte[]{(byte)b});
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    public byte[] toByteArray(){
        return out.toByteArray();
    }

    public void clear(){
        out = new ByteArrayOutputStream();
    }

}
