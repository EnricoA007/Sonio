package net.so.nio.compressor;

public interface ByteCompressor {

    byte[] compress(byte[] b) throws Throwable;

}
