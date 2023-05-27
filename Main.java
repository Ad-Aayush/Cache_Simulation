import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

class Main {
    public static void main(String args[]) {
        if (args.length != 4)
            System.out.println(
                    "INVALID FORMAT\nThe correct format is java Main.java Cache_size(in KB) Set_Associativity Block size(64B) PATH.");
        else {
            ArrayList<String> fileHex = new ArrayList<String>();
            try {
                int cacheSize = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Cache Size must be an Integer");
            }
            try {
                int columnNum = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Associativity must be an Integer");
            }
            try {
                int blockSize = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Block Size must be an Integer");
            }
            try {
                File data = new File(args[3]);
                Scanner myReader = new Scanner(data);
                while (myReader.hasNextLong(16)) {
                    Long hex = myReader.nextLong(16);
                    fileHex.add(Long.toBinaryString(hex));
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("Invalid File Path");
            }
            System.out.println(fileHex.size());
        }
    }
}