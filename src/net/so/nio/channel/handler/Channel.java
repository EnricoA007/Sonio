package net.so.nio.channel.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Channel {

    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private ChannelHandler handler;
    private boolean closed = false;

    protected Channel(Socket socket, InputStream in, OutputStream out, ChannelHandler handler){
        this.socket=socket;
        this.in=in;
        this.out=out;
        this.handler=handler;
    }

    @Deprecated
    public Socket getSocket() throws IOException{
        throwOnClose();
        return socket;
    }

    @Deprecated
    public InputStream getInputStream() throws IOException {
        throwOnClose();
        return in;
    }

    @Deprecated
    public OutputStream getOutputStream() throws IOException {
        throwOnClose();
        return out;
    }

    public void close() throws IOException {
        socket.close();
        handler.disconnectChannel(this);
    }

    public ChannelHandler getChannelHandler() throws IOException {
        throwOnClose();
        return handler;
    }

    private void throwOnClose() throws IOException {
        if(isClosed()) throw new IOException("Channel already closed");
    }

    public boolean isClosed(){
        return closed;
    }

}
