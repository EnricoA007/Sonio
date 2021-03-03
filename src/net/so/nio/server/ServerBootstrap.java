package net.so.nio.server;

import net.so.nio.channel.handler.ChannelHandler;
import net.so.nio.channel.pipeline.ChannelPipeline;

import java.net.ServerSocket;

public class ServerBootstrap {

    private int port,timeout;
    private ChannelPipeline pipeline;
    private boolean bound = false;

    protected ServerBootstrap(int port, int timeout){
        this.port=port;
        this.timeout=timeout;
    }

    public int getTimeout(){
        return timeout;
    }

    public int getPort() {
        return port;
    }

    public ChannelPipeline getPipeline(){
        return pipeline;
    }

    public boolean isBound(){
        return bound;
    }

    public ServerBootstrap setPipeline(ChannelPipeline pipeline){
        if(pipeline == null) return null;
        this.pipeline=pipeline;
        return this;
    }

    public ChannelHandler bind(){
        if(bound) throw new NullPointerException("Already bound on port " +getPort());
        try{
            new ServerSocket(getPort()).close();
        }catch(Exception e){
            throw new NullPointerException("Some other services are bound on port " +getPort());
        }
        bound = true;
        return new ChannelHandler(this);
    }

    /**
     * Create a server bootstrap on specific port and a timeout, if you want
     * @param port On which port listen the server
     * @param timeout -1 when the socket does not timeout, any other time in milliseconds
     * @return Returns a server bootstrap
     */
    public static ServerBootstrap createBootstrap(int port, int timeout){
        return new ServerBootstrap(port,timeout);
    }

}
