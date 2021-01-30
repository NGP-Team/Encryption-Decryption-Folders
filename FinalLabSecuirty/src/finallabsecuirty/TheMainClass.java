/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package finallabsecuirty;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;


public class TheMainClass {
    
    
    

    public static void main(String[] args) {
        
      Scanner scanner = new Scanner(System.in);
        int menuChoice = 0;
        int inputChoice = 0;
        String algorithmChoice = "";
        String cryptoMode = "";
        String key;
        String filename;
        String dirPath;
        File inputFile = null;
        File[] inputFiles = null;

        menu:
        do {
            System.out.println("                      A SYMMETRIC CRYPTO SYSTEM \n==========================================================================");
            System.out.println("MAIN MENU \n---------------------- \n1. Encrypt \n2. Decrypt \n3. Exit \n----------------------");
            System.out.print("Enter your choice: ");

            try {
                menuChoice = scanner.nextInt();
                while (menuChoice != 1 && menuChoice != 2 && menuChoice != 3) {
                    System.out.println("Invalid menu choice!");
                    System.out.print("Enter your choice: ");
                    menuChoice = scanner.nextInt();
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }

            switch (menuChoice) {
                case 1:
                    cryptoMode = "encrypt";
                    break;
                case 2:
                    cryptoMode = "decrypt";
                    break;
                case 3:
                    System.out.println("------------ Goodbye ------------");
                    break menu;
                default:
                    System.out.println("Invalid menu choice!");
                    break menu;
            }

            System.out.println("(1) File (2) Folder");
            System.out.print("Enter your choice: ");

            try {
                inputChoice = scanner.nextInt();
                while (inputChoice != 1 && inputChoice != 2) {
                    System.out.println("Invalid input! ");
                    System.out.print("Enter your choice: ");
                    inputChoice = scanner.nextInt();
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input! ");
                break menu;
            }

            // ------ File ------
            if (inputChoice == 1) {
                System.out.print("Name: ");
                filename = scanner.next();

                try {
                    inputFile = new File(filename);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }

                // ------ Folder ------
            } else if (inputChoice == 2) {
                System.out.print("Folder path: ");
                dirPath = scanner.next();

                try {
                    File directory = new File(dirPath);
                    inputFiles = directory.listFiles();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.print("Algorithm (AES, DES): ");
            algorithmChoice = scanner.next().toUpperCase();
            while (!algorithmChoice.equals("AES") && !algorithmChoice.equals("DES")) {
                System.out.println("Invalid input! ");
                System.out.print("Algorithm (AES, DES): ");
                algorithmChoice = scanner.next().toUpperCase();
            }

            System.out.print("Key: ");
            key = scanner.next();
            while (algorithmChoice.equals("AES") && key.length() < 16) {
                System.out.println("Key should be at least 16 characters long");
                System.out.print("Key: ");
                key = scanner.next();
            }

            while (algorithmChoice.equals("DES") && key.length() < 8) {
                System.out.println("Key should be at least 8 characters long");
                System.out.print("Key: ");
                key = scanner.next();
                
            }
            System.out.println("----------------------");

            if (inputChoice == 1) { // ------ File ------
                doCrypto(cryptoMode, algorithmChoice, key, inputFile);

            } else if (inputChoice == 2) { // ------ Folder ------
                for (int i = 0; i < inputFiles.length; i++) {
                    try {
                        // in decrypt mode: ignore all the folders and the files that doesn't end with .encrypted
                        if (inputFiles[i].isFile() && cryptoMode.equals("decrypt") && inputFiles[i].getName().toLowerCase().endsWith(".encrypted")) {
                            doCrypto(cryptoMode, algorithmChoice, key, inputFiles[i]);
                        } // in encrypt mode: ignore all the folders and the files that doesn't end with .txt
                        else if (inputFiles[i].isFile() && inputFiles[i].getName().toLowerCase().endsWith(".txt") && !cryptoMode.equals("decrypt")) {
                            doCrypto(cryptoMode, algorithmChoice, key, inputFiles[i]);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            // reset variables
            menuChoice = 0;
            inputChoice = 0;
            algorithmChoice = "";
        } while (menuChoice != 3);

    }

    public static void doCrypto(String cryptoMode, String Algorithm, String key, File inputFile) {
        String filename = inputFile.getName().split("\\.")[0]; //get the file name without the extension
        String filePath = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().length() - inputFile.getName().length()); //get the file path without the filename

        File outputFile = new File(filePath + filename + "." + cryptoMode + "ed");

        try {
            if (cryptoMode.equals("encrypt")) {
                FinalLabSecuirty.encrypt(Algorithm, key, inputFile, outputFile);
            } else if (cryptoMode.equals("decrypt")) {
                System.out.println("deecryption");
                FinalLabSecuirty.decrypt(Algorithm, key, inputFile, outputFile);
            }

            System.out.println("Done! File " + inputFile.getName() + " is " + cryptoMode + "ed" + " using " + Algorithm);
            System.out.println("Output file is " + outputFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

    }
    
}
