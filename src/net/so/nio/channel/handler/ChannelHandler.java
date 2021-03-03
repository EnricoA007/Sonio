package net.so.nio.channel.handler;

import net.so.nio.channel.io.ByteBuffer;
import net.so.nio.channel.pipeline.ChannelPipeline;
import net.so.nio.channel.thread.ChannelListenerThread;
import net.so.nio.server.ServerBootstrap;
import net.so.nio.util.CacheList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChannelHandler {

    private ServerBootstrap bootstrap;
    private CacheList<Channel> channels = new CacheList<>();
    private final ChannelHandler handler = this;

    /**
     * Please use ServerBootstrap.createBootstrap();
     * and then bind(); to create a channel handler.
     */
    public ChannelHandler(ServerBootstrap bootstrap){
        this.bootstrap=bootstrap;
    }

    /**
     * @return Channel array with connected channels (Clients)
     */
    public Channel[] getConnectedChannels(){
      return (Channel[]) channels.getAll();
    }

    /**
     * Disconnect channel from server
     * @param ch Channel
     * @return true if disconnected, false if ch does not exist
     */
    public boolean disconnectChannel(Channel ch){
        Channel[] chs = getConnectedChannels();
        for(Channel c : chs){
            if(c.equals(ch)){
                channels.remove(ch);
                return true;
            }
        }
        return false;
    }

    /**
     * Clear all disconnected channels, if socket was closed
     * @throws IOException if IO Error occur
     */
    public void clearDisconnectedChannels() throws IOException {
        for(Channel chz : getConnectedChannels()){
            if(chz.getSocket().isClosed() || chz.getSocket().isBound()){
                channels.remove(chz);
                continue;
            }
        }
    }

    /**
     * Listen on the entered port in the bootstrap
     * @param autostart If true, the thread will automatically starts
     * @return Returns a channel listener thread. With that you can access the thread
     */
    public ChannelListenerThread listen(boolean autostart) {
        int port = bootstrap.getPort();
        ChannelPipeline pipeline = bootstrap.getPipeline();

        Thread channel_register_thread = new Thread(){
            @Override
            public void run() {

                try{
                    ServerSocket server = new ServerSocket(port);

                    try{
                        Socket client = server.accept();
                        client.setKeepAlive(true);
                        if(bootstrap.getTimeout() != -1) client.setSoTimeout(bootstrap.getTimeout());
                        InputStream in = client.getInputStream();
                        OutputStream out = client.getOutputStream();
                        Channel ch = new Channel(client,in,out, handler);
                        channels.add(ch);
                        clearDisconnectedChannels();
                    }catch(Exception ex){
                        pipeline.fireException(ex);
                    }

                }catch(IOException e){
                    throw new NullPointerException("This port is already in use");
                }

            }
        };

        Thread channel_worker_thread = new Thread() {
            @Override
            public void run() {

                while(true){

                    try{

                        HashMap<ByteBuffer, OutputStream> buffers = new HashMap<>();

                        for(Channel ch : getConnectedChannels()){

                            /**
                             * Read bytes from input stream and creating buffer
                             */
                            InputStream in = ch.getInputStream();

                            byte[] array = new byte[in.available()];
                            in.read(array);

                            if(array.length == 0){
                                pipeline.fireException(new NullPointerException("The channel sent 0 bytes"));
                            }else{
                                IByteBuffer ibb = new IByteBuffer(array);
                                ByteBuffer buffer = (ByteBuffer) ibb;
                                pipeline.getCurrentHandler().pushChannel(ch);
                                pipeline.getCurrentHandler().handle(buffer);
                                buffers.put(buffer,ch.getOutputStream());
                            }
                        }

                        clearDisconnectedChannels();

                        for(ByteBuffer bf : buffers.keySet()) {
                            OutputStream out = buffers.get(bf);

                            byte[] array = ((IByteBuffer) bf).pullWriter();
                            if (array.length != 0) {
                                out.write(array);
                            }

                            out.flush();
                        }

                        buffers.clear();

                    }catch(Exception e){
                        pipeline.fireException(e);
                    }

                }

            }
        };

        channel_register_thread.setName("Channel Register Thread | " + bootstrap.getPort());
        channel_worker_thread.setName("Channel Worker Thread | " + bootstrap.getPort());

        if(autostart){
            channel_register_thread.start();
            channel_worker_thread.start();
        }

        return new ChannelListenerThread() {
            @Override
            public Thread getChannelRegisterThread() {
                return channel_register_thread;
            }

            @Override
            public Thread getChannelWorkerThread() {
                return channel_worker_thread;
            }
        };
    }

}
