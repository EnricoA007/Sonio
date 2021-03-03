package net.so.nio.channel.thread;

public interface ChannelListenerThread {

    Thread getChannelRegisterThread();
    Thread getChannelWorkerThread();

}
