package net.so.nio.hash;

@FunctionalInterface
public interface HashAlgorithm {

    byte[] hash(byte[] toHash) throws Throwable;

}
