package org.lld.patterns.structural.facade;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Facade Pattern Demo ===\n");

        // Without Facade: Complex interaction with multiple subsystems
        System.out.println("1. Starting computer WITHOUT Facade (complex):");
        CPU cpu = new CPU();
        Memory memory = new Memory();
        HardDrive hardDrive = new HardDrive();

        cpu.freeze();
        byte[] bootData = hardDrive.read(0x0000, 512);
        memory.load(0x0000, bootData);
        cpu.jump(0x0000);
        cpu.execute();
        System.out.println();

        // With Facade: Simple interface
        System.out.println("2. Starting computer WITH Facade (simple):");
        ComputerFacade computer = new ComputerFacade();
        computer.startComputer();

        System.out.println("3. Shutting down computer:");
        computer.shutDown();

        System.out.println("Notice: Facade hides the complexity of subsystem interactions!");
    }
}

