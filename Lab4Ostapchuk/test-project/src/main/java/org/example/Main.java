package org.example;

public class Main {
    public static void main(String[] args) {
        CpuModel cpuModel = new CpuModel();
        cpuModel.setQuantity(-3);
        System.out.println(cpuModel.getQuantity());
        try {
            NumberValidator.validateNumber(cpuModel);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }

    }
}
