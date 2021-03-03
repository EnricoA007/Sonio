package net.so.nio.channel.pipeline;

public class DefaultExceptionHandlerContext implements ExceptionHandlerContext {

    protected DefaultExceptionHandlerContext(){}

    @Override
    public void handle(Exception e) {
        System.err.println("Your channel pipeline does not have a exception handler.\nThis is the default exception handler context\nPlease add a exception handler into your ChannelPipeline");
        System.err.println("ChannelPipeline.setExceptionHandler(ExceptionHandlerContext ehc);");
        e.printStackTrace();
    }
}
