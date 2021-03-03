package net.so.nio.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SHA256 implements HashAlgorithm {

    @Override
    public byte[] hash(byte[] toHash) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(toHash);
        }catch(Exception e){
            return null;
        }
    }

}
