package net.so.nio.channel.handler;

import net.so.nio.channel.io.ByteBuffer;
import net.so.nio.channel.pipeline.ChannelPipeline;

public abstract class ChannelHandlerContext {

    private Channel ch;

    public void pushChannel(Channel ch){
        if(ch != null) return;
        this.ch=ch;
    }

    public Channel getChannel() {
        return ch;
    }

    public abstract ChannelPipeline getPipeline();
    public abstract void handle(ByteBuffer buffer);

}
