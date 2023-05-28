import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String args[]) {
        if (args.length != 4)
            System.out.println(
                    "INVALID FORMAT\nThe correct format is java Main.java Cache_size(in KB) Set_Associativity Block size(64B) PATH.");
        else {
            int cacheSize = 0, columnNum = 0, blockSize = 0;
            ArrayList<Block> fileHex = new ArrayList<Block>();
            try {
                cacheSize = Integer.parseInt(args[0]) * 1024;
            } catch (NumberFormatException e) {
                System.out.println("Cache Size must be an Integer");
                System.exit(0);
            }
            try {
                columnNum = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Associativity must be an Integer");
                System.exit(0);
            }
            try {
                blockSize = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Block Size must be an Integer");
                System.exit(0);
            }
            int rowNum = cacheSize / (blockSize * columnNum);
            try {
                File data = new File(args[3]);
                Scanner myReader = new Scanner(data);
                while (myReader.hasNextLong(16)) {
                    Long hex = myReader.nextLong(16);
                    String binString = (Long.toBinaryString(hex));
                    fileHex.add(new Block(binString, blockSize, rowNum));
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("Invalid File Path");
                System.exit(0);
            }
            ArrayList<ArrayList<Integer>> cache = new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i < rowNum; i++) {
                cache.add(new ArrayList<Integer>());
            }
            ArrayList<Integer> setHits = new ArrayList<Integer>();
            ArrayList<Integer> setMiss = new ArrayList<Integer>();
            for (int i = 0; i < rowNum; i++) {
                setHits.add(0);
                setMiss.add(0);
            }
            for (Block current : fileHex) {
                int setIndex = current.getSetIndex();
                int tag = current.getTag();
                ArrayList<Integer> row = cache.get(setIndex);
                if (row.contains(tag)) {
                    row.remove(row.indexOf(tag));
                    int currHits = setHits.get(setIndex);
                    setHits.set(setIndex, currHits + 1);
                    row.add(tag);
                } else {
                    int currMiss = setMiss.get(setIndex);
                    setMiss.set(setIndex, currMiss + 1);
                    if (row.size() < columnNum) {
                        row.add(tag);
                    } else {
                        row.remove(0);
                        row.add(tag);
                    }
                }
            }
            System.out.println("Set Index" + "\tSet Hits" + "\tSet Miss");
            for (int i = 0; i < rowNum; i++) {
                System.out.println("    " + i + ":" + "\t\t   " + setHits.get(i) + "\t\t   " + setMiss.get(i));
            }
        }
    }
}

class Block {
    int offset;
    int setIndex;
    int tag;

    Block(String binary, int blockSize, int rowNum) {
        int offsetBits = (int) (Math.log(blockSize) / Math.log(2));
        int setIndexBits = (int) ((Math.log(rowNum) / Math.log(2)));
        this.offset = Integer.parseInt(binary.substring(32 - offsetBits), 2);
        this.setIndex = Integer.parseInt(binary.substring(32 - offsetBits - setIndexBits, 32 - offsetBits), 2);
        this.tag = Integer.parseInt(binary.substring(0, 32 - offsetBits - setIndexBits), 2);
    }

    int getSetIndex() {
        return this.setIndex;
    }

    int getTag() {
        return this.tag;
    }
}