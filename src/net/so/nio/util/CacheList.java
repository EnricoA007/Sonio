package net.so.nio.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class CacheList<T> {

    private Object[] obj;
    private Object[] cached;

    public CacheList(){}

    public void add(T t){
        if(obj == null){
            obj = new Object[1];
            obj[0]= t;
        }else{
            Object[] array = new Object[cached.length+1];

            for(int i = 0; i<cached.length; i++) {
                array[i] = cached[i];
            }

            array[cached.length] = t;

            obj =array;
        }
        new Thread(() -> {
            try{
                Thread.sleep(1000);
                cached=obj;
            }catch(Exception e) {
                cached=obj;
            }
        }).start();
    }

    public void waitForCache(){
        PrintStream out = System.out;
        while(cached != obj){

            System.setOut(new PrintStream(System.out) {
                @Override
                public void print(float f) {

                }
            });

            System.out.print(1f);
        }
        System.setOut(out);
    }

    public int getSize(){
        waitForCache();
        return cached.length;
    }

    public Object[] getAll() {
        waitForCache();
        return cached;
    }

    public T get(int index){
        waitForCache();
        return (T) getAll()[index];
    }

    public void set(int index, T o){
        waitForCache();
        obj[index] = o;
        new Thread(() -> {
            try{
                Thread.sleep(1000);
                cached=obj;
            }catch(Exception e) {
                cached=obj;
            }
        }).start();
    }

    public void remove(T t){
        waitForCache();

        Object[] clone = new Object[obj.length-1];

        int i = 0;

        for(Object o : obj){
            if(o == t){
                continue;
            }
            clone[i] = o;
            i++;
        }

        obj = clone;

        new Thread(() -> {
            try{
                Thread.sleep(1000);
                cached=obj;
            }catch(Exception e) {
                cached=obj;
            }
        }).start();
    }

    public T get(T o){
        waitForCache();
        int i = 0;

        for(Object obj : cached) {
            if(obj.equals(o)){
                return get(i);
            }
            i++;
        }

        return null;
    }

}
