package net.so.nio.test;

import net.so.nio.compressor.DoubleByteCompressor;
import net.so.nio.hash.SHA256;

import java.nio.charset.StandardCharsets;

public class SonioTest {

    public static void main(String[] args){

        try{
            DoubleByteCompressor dbc = new DoubleByteCompressor();

            byte compressed = dbc.compress((byte) 'H', (byte) 'o');
            System.out.println((char)compressed);
            byte[] decompressed = dbc.decompress(compressed);
            System.out.println(new String(decompressed));

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
