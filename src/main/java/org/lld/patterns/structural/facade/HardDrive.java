package org.lld.patterns.structural.facade;

/**
 * Subsystem component: Hard Drive
 */
public class HardDrive {
    public byte[] read(long lba, int size) {
        System.out.println("HardDrive: Reading " + size + " bytes from LBA " + lba);
        return new byte[size];
    }
}

