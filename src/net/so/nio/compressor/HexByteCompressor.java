package net.so.nio.compressor;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HexByteCompressor implements ByteCompressor, ByteDecompressor {

    @Override
    public byte[] compress(byte[] b)  {
        return new byte[] { ((byte)(int)convertMapSI().get(new String(b))) };
    }

    @Override
    public byte[] decompress(byte[] b)  {
        return convertMapIS().get((int)b[0]).getBytes(StandardCharsets.UTF_8);
    }

    private HashMap<Integer, String> convertMapIS(){
        HashMap<Integer, String> map = new HashMap<Integer, String>();

        int i = 0;
        String hex ="ABCDEF0987654321";

        for(int a = 0; a<hex.length(); a++){
            for(int b = 0; b<hex.length(); b++){
                String h = ""+ hex.charAt(a) + hex.charAt(b);
                map.put(i, h);
                i++;
            }
        }

        return map;
    }

    private HashMap<String, Integer> convertMapSI(){
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        int i = 0;
        String hex ="ABCDEF0987654321";

        for(int a = 0; a<hex.length(); a++){
            for(int b = 0; b<hex.length(); b++){
                String h = ""+ hex.charAt(a) + hex.charAt(b);
                map.put(h, i);
                i++;
            }
        }

        return map;
    }

}
