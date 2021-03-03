package net.so.nio.compressor;

import crypto.api.bitman.BitMan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MultipleCharCompressor implements ByteCompressor {

    public static byte COMPRESS_BYTE = (byte)(int)-0xAC;
    private Charset charset;

    public MultipleCharCompressor(Charset charset){
        this.charset=charset;
    }

    @Override
    public byte[] compress(byte[] b) throws IOException  {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        String unformatted = new String(b,charset);
        int skip = 0;

        for(int i = 0; i<unformatted.length(); i++){
           try{
               if(skip > 0){
                   skip = skip -1;
                   continue;
               }
               char c = unformatted.charAt(i);
               for(int a = i+1; a<unformatted.length(); a++){
                   char d = unformatted.charAt(a);
                   if(c == d){
                       skip++;
                   }else{
                       break;
                   }
               }
               if(skip > 0){
                   String a = skip + "" + c;
                   BitMan.BitManager man = BitMan.createBitMan();
                   String binary = man.createContent(a, BitMan.BitManager.ContentView.PLAIN_TEXT).getBinary();
                   String binary2 = man.createContent(new byte[] {COMPRESS_BYTE, COMPRESS_BYTE}, BitMan.BitManager.ContentView.BYTE_ARRAY).getBinary();
                   String xor = man.getGates().XOR.encrypt(new String[]{binary,binary2});
                   byte[] plain = man.createContent(xor, BitMan.BitManager.ContentView.BINARY).getContent();
                   out.write(plain);
               }else{
                   out.write((""+c).getBytes(StandardCharsets.UTF_8));
               }
           }catch(Exception e){
               out.write((""+unformatted.charAt(i)).getBytes(StandardCharsets.UTF_8));
               break;
           }
        }

        return out.toByteArray();
    }

}
