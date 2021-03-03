package net.so.nio.channel.pipeline;

import net.so.nio.channel.handler.ChannelHandlerContext;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class ChannelPipeline {

    private HashMap<String,ChannelHandlerContext> map = new HashMap<>();
    private Object[] currentHandler;
    private ExceptionHandlerContext exceptionhandler = new DefaultExceptionHandlerContext();

    public ChannelPipeline(){}

    /**
     * The Channel Pipeline is a model, that is similar like a real water pipeline.
     * You must add handlers in the pipeline which reads the data from the pipeline.
     * With this method, you can add a handler on the top of the pipeline. (This will be first handled)
     *
     * @param name Name of the handler
     * @param context IO Handler (ByteBuffer Read / Write)
     */
    public void addFirst(String name, ChannelHandlerContext context){
        HashMap<String,ChannelHandlerContext> clone = new HashMap<>();
        clone.put(name,context);
        map.forEach((s,c) -> {
            clone.put(s,c);
        });
        this.map=clone;
    }

    /**
     * The Channel Pipeline is a model, that is similar like a real water pipeline.
     * You must add handlers in the pipeline which reads the data from the pipeline.
     * With this method, you can add a handler on the bottom of the pipeline. (This will be last handled)
     *
     * @param name Name of the handler
     * @param context IO Handler (ByteBuffer Read / Write)
     */
    public void addLast(String name, ChannelHandlerContext context){
        this.map.put(name,context);
    }

    /**
     * The Channel Pipeline is a model, that is similar like a real water pipeline.
     * You must add handlers in the pipeline which reads the data from the pipeline.
     * With this method, you can add a handler before a already created handler
     *
     * @param name Name of the handler
     * @param context IO Handler (ByteBuffer Read / Write)
     * @param after Name of the handler which you want to add before
     */
    public void addBefore(String name, String after, ChannelHandlerContext context){
        HashMap<String,ChannelHandlerContext> clone = new HashMap<>();

        map.forEach((s,c) -> {
            if(s.equals(name)){
                clone.put(name,context);
            }
            clone.put(s,c);
        });

        this.map=clone;
    }

    /**
     * The Channel Pipeline is a model, that is similar like a real water pipeline.
     * You must add handlers in the pipeline which reads the data from the pipeline.
     * With this method, you can add a handler after a already created handler
     *
     * @param name Name of the handler
     * @param context IO Handler (ByteBuffer Read / Write)
     * @param after Name of the handler which you want to add after
     */
    public void addAfter(String name, String after, ChannelHandlerContext context){
        HashMap<String,ChannelHandlerContext> clone = new HashMap<>();

        map.forEach((s,c) -> {
            clone.put(s,c);
            if(s.equals(name)){
                clone.put(name,context);
            }
        });

        this.map=clone;
    }

    /**
     * Get all elements in this pipeline
     * @param consumer BiConsumer with String: name of handler and ChannelHandlerContext: handler
     */
    public void consume(BiConsumer<String,ChannelHandlerContext> consumer){
        map.forEach(consumer);
    }

    /**
     * Call the next channel handler in this pipeline
     * Get current pipeline handler with <i>ChannelPipeline.getCurrentHandler()</i>
     */
    public void firePipelinePush(){
        ChannelHandlerContext chx = getCurrentHandler();

        int jumpTo = 0;
        String no = null;
        ChannelHandlerContext handler = null;

        for(String name : map.keySet()){
            if(chx.equals(map.get(name))) {
                jumpTo++;
                break;
            }
            jumpTo++;
        }

        int iterate = 0;

        for(String name : map.keySet()){
            ChannelHandlerContext ctx = map.get(name);
            if(iterate == jumpTo){
                no = name;
                handler = ctx;
                break;
            }
            iterate++;
        }

        if(no == null || handler == null) throw new NullPointerException("You are at the end of the pipeline");

        currentHandler = new Object[] { no,handler };
    }

    /**
     * @throws NullPointerException When no current handler was set
     * @return the current handler in this pipeline
     */
    public ChannelHandlerContext getCurrentHandler(){
        if(this.currentHandler == null) throw new NullPointerException("No current handler was set");
        return (ChannelHandlerContext)currentHandler[1];
    }

    /**
     * Set an exception handler which allows you to catch errors when a IO error occur
     * @param ehc Exception Handler Context which catches the exception
     */
    public void setExceptionHandler(ExceptionHandlerContext ehc){
        this.exceptionhandler = ehc;
    }

    /**
     * Call the ExceptionHandlerContext, we want handle this exception, that does the ExceptionHandlerContext
     * @param e Which exception throw
     */
    public void fireException(Exception e){

    }

}
