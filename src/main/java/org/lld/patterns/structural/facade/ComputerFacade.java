package org.lld.patterns.structural.facade;

/**
 * Facade class that provides a simplified interface to the complex subsystem.
 * Hides the complexity of CPU, Memory, and HardDrive interactions.
 */
public class ComputerFacade {
    private final CPU cpu;
    private final Memory memory;
    private final HardDrive hardDrive;

    private static final long BOOT_ADDRESS = 0x0000;
    private static final long BOOT_SECTOR = 0x0000;
    private static final int SECTOR_SIZE = 512;

    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }

    /**
     * Simplified method to start the computer.
     * Hides the complex boot sequence from the client.
     */
    public void startComputer() {
        System.out.println("=== Starting Computer ===");
        cpu.freeze();
        byte[] bootData = hardDrive.read(BOOT_SECTOR, SECTOR_SIZE);
        memory.load(BOOT_ADDRESS, bootData);
        cpu.jump(BOOT_ADDRESS);
        cpu.execute();
        System.out.println("=== Computer Started ===\n");
    }

    /**
     * Simplified method to shut down the computer.
     */
    public void shutDown() {
        System.out.println("=== Shutting Down Computer ===");
        cpu.freeze();
        System.out.println("=== Computer Shut Down ===\n");
    }
}

