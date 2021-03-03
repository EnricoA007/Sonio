package net.so.nio.compressor;

public interface ByteDecompressor {

    byte[] decompress(byte[] b) throws Throwable;

}
