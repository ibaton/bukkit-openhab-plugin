package se.treehouse.minecraft;

import java.util.Scanner;

/**
 * TestClass
 */
public class Main {

    public static void main(String[] args){
        WSMinecraft.DiscoveryService discoveryService = new WSMinecraft.DiscoveryService();
        discoveryService.start();
        System.out.print("Starting service");

        Scanner s = new Scanner(System.in);
        s.next();

        discoveryService.stop();
        System.out.print("Service stopped");
    }
}
