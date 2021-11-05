import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Project Description:
 * This program decompresses files. It utilizes many of the same ideas as the compress method however
 * there are slight differences. Most notably being in the way decompression is done and how the
 * HashMap has reversed variables from the compress one.
 *
 *@Author Ivan Zarate
 *CSCI 340
 *Section 001*
 *Assignment 2 LZW Decompress
 *Known Bugs: None
 */

public class LZWDecompress {

    // Global HashMap variable "decompressTime" that will be updated and used throughout program

    private static Map<Integer, String> decompressTime = new HashMap<>();

    public static void main(String[] args) throws IOException {

        // Loop to fill the Hashmap with all ASCII values first
        for (int i = 0; i < 128; i++) {
            char temp = (char) i;
            decompressTime.put(i, Character.toString(temp));
        }

        // Scanner used to take user input
        Scanner input = new Scanner(System.in);

        // Prompt to ask user for name of file to decompress
        System.out.println("Please enter the name of the file you wanna decompress, it MUST end with .lzw so heads up:");

        // String "compressed" that holds the original name of the file
        String compressed = input.nextLine().trim();

        // String variable to hold the new name for the output file
        String decompressFileName = compressed.replaceAll(".lzw", "");

        // File object made using name of original file user typed in
        File inputFile = new File(compressed);

        // File object made using string name held in "decompressFileName"
        File outputFile = new File(decompressFileName);

        // Send to decompress method using files we created above as parameters
        decompress(inputFile, outputFile);
    }

    /**
     * Method that does the decompression for the file, takes two parameters
     * @param original is the first parameter and holds the file created from the original name the user typed in
     * @param newFile holds the name of the new file created in the main
     * @throws IOException is what's thrown when an error in outputting or inputting file occurs
     */

    private static void decompress(File original, File newFile) throws IOException {

        // Variable to hold the starting point where the next thing will be placed in the HashMap
        int mapPosition = 128;

        try {

            // RandomAccessFile that reads the original file sent in as the first parameter
            RandomAccessFile file = new RandomAccessFile(original, "r");

            // RandomAccessFile that reads and writes to the new file sent in as the second parameter
            RandomAccessFile decompressedFile = new RandomAccessFile(newFile, "rw");

            // String variable called "str" to be used and updated throughout loop
            String str;

            // String variable "chr" to hold the next character in the file
            String chr = "" + (char) file.readByte();

            // Loop to go through entire file
            while (true) {

                // Sets a variable "i" equal to the current byte
                int i = file.readByte();

                // Checks to see if the byte is negative, if so adds 256 to it
                if (i < 0) {
                    i += 256;
                }

                // Checks to see if the HashMap has the corresponding key "i"
                if (decompressTime.containsKey(i)) {

                    // If so "str" is set equal to whatever string is held in that point in the HashMap
                    str = decompressTime.get(i);

                } else {

                    // If not "str" is set equal to "chr" in addition to the first character in "chr"
                    str = chr + chr.charAt(0);
                }

                // Create a character array that holds all the characters in "ch"
                char[] ch = chr.toCharArray();

                // For loop to go through for each character in the array above
                for (char c : ch) {

                    // Writes to the output file
                    decompressedFile.writeBytes("" + c);
                }

                // Checks to see that the variable "mapPosition" hasn't exceeded 256 as that is the limit for the size of the HashMap
                if (mapPosition < 256) {

                    // Places the corresponding integer and string into the global HashMap "decompressTime"
                    decompressTime.put(mapPosition++, chr + str.charAt(0));
                }

                    // Sets "chr" equal to "str"
                    chr = str;
            }

            // Catch statements to catch and errors and prints out the following statements if they do occur
        } catch (EOFException eof) {
            System.out.println("End of File reached.");
        } catch (Exception e) {
            System.out.println("An Exception occurred. " + e);
        }
    }
}