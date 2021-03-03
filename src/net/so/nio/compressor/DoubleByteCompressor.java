package net.so.nio.compressor;

import crypto.api.bitman.Base64;
import crypto.api.bitman.BitMan;
import net.so.nio.hash.SHA256;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DoubleByteCompressor implements ByteCompressor, ByteDecompressor {

    public static BitMan.BitManager man = BitMan.createBitMan();
    public static Base64.Encoder encoder = man.getBase64().getEncoder();
    public static SHA256 sha256 = new SHA256();

    @Override
    public byte[] compress(byte[] b) throws Throwable {
        return new byte[0];
    }

    @Override
    public byte[] decompress(byte[] b) throws Throwable {
        return new byte[0];
    }

    public byte[] decompress(byte a){
        HexByteCompressor hexByteCompressor = new HexByteCompressor();
        String hex = "ABCDEF0987654321";
        while(true){
            Random r = new Random();
            String h1= "" + hex.charAt(r.nextInt(hex.length())) + hex.charAt(r.nextInt(hex.length()));
            String h2= "" + hex.charAt(r.nextInt(hex.length())) + hex.charAt(r.nextInt(hex.length()));

            if((hash((""+h1+h2).getBytes(StandardCharsets.UTF_8))) == a){
                return new byte[]{
                    man.createContent(h1, BitMan.BitManager.ContentView.HEX).getContent()[0],
                    man.createContent(h2, BitMan.BitManager.ContentView.HEX).getContent()[0],
                };
            }
        }
    }

    public byte compress(byte a, byte b){
        HexByteCompressor hex = new HexByteCompressor();

        String hex1 = man.createContent(new byte[]{a}, BitMan.BitManager.ContentView.BYTE_ARRAY).getHex();
        String hex2 = man.createContent(new byte[]{b}, BitMan.BitManager.ContentView.BYTE_ARRAY).getHex();

        hex1 = hex1.substring(3,hex1.length());
        hex2 = hex2.substring(3,hex2.length());

        return (byte)(hash((""+hex1+hex2).getBytes(StandardCharsets.UTF_8)));
    }

    private int hash(byte[] a){
        byte[] toHash =sha256.hash(a);
        String hex = man.createContent(toHash, BitMan.BitManager.ContentView.BYTE_ARRAY).getHex();
        int i = 0;
        hex = hex.replaceAll(" ", "");
        for(char c : hex.toCharArray()) {
            i += (int)c;
        }
        return i;
    }

}
