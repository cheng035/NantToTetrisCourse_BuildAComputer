import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javafx.util.Pair;

public class Assembler {

    static int address = 16;
    private static HashMap<String, Integer> symbolTable;
    private static HashMap<String, String> jumpTable;
    private static HashMap<String, String> compTable;
    private static HashMap<String, String> destTable;
    private String fileName;

    /**
     * generate the initial map
     */
    static void init() {
        address = 16;
        symbolTable = new HashMap<String, Integer>() {{
            put("SCREEN", 16384);
            put("KBD", 24576);
            put("SP", 0);
            put("LCL", 1);
            put("ARG", 2);
            put("THIS", 3);
            put("THAT", 4);
        }};

        /**
         * add R0-R15  to the symbol map
         */
        IntStream
            .range(0, 15)
            .forEach(
                i -> symbolTable.put("R" + i, i)
            );

        jumpTable = new HashMap<String, String>() {{
            put("","000");
            put("JGT", "001");
            put("JEQ", "010");
            put("JGE", "011");
            put("JLT", "100");
            put("JNE", "101");
            put("JLE", "110");
            put("JMP", "111");
        }};

        destTable = new HashMap<String, String>() {{

            put("", "000");
            put("M", "001");
            put("D", "010");
            put("MD", "011");
            put("A", "100");
            put("AM", "101");
            put("AD", "110");
            put("AMD", "111");
        }};

        compTable = new HashMap<String, String>() {{
            put("0", "0101010");
            put("1", "0111111");
            put("-1", "0111010");
            put("D", "0001100");
            put("A", "0110000");
            put("!D", "0001101");
            put("!A", "0110001");
            put("-D", "0001111");
            put("-A", "0110011");
            put("D+1", "0011111");
            put("A+1", "0110111");
            put("D-1", "0001110");
            put("A-1", "0110010");
            put("D+A", "0000010");
            put("D-A", "0010011");
            put("A-D", "0000111");
            put("D&A", "0000000");
            put("D|A", "0010101");
        }};
/**
 * add symbol for M
 */
        List<Pair<String,String>> pairList = new LinkedList<>();
        compTable.forEach((k, v)->
            {
                if(k.contains("A")){
                    String nk = k.replace('A','M');
                    String nv = '1'+v.substring(1,v.length());
                    pairList.add(new Pair<>(nk,nv));
                }
            }
            );
        pairList.forEach((k)->compTable.put(k.getKey(),k.getValue()));


    }






    public static void main(String[] args) throws IOException {
        Assembler assembler = new Assembler();
        while (true) {
            assembler.init();
            List<String> code = assembler.readFile();
            System.out.println(code.toString());

            assembler.addAddressSymbol(code);

            List<String> resultCode = assembler.translateProgram(code);

            assembler.writeFile(resultCode);
        }

    }



    private List<String> readFile() {
        System.out.println("please enter your file name");
        Scanner sc = new Scanner(System.in);
        fileName = sc.nextLine();
        List<String> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            //filter comment empty line and trim the String
            list = stream
                .filter(line -> !line.startsWith("/")) // remove comment line
                .filter(line -> !line.isEmpty()) // remove empty line
                .map(String::trim)// remove white space
                .map(line -> line.split("//")[0].trim()) // remove the comment
                .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    //first pass the program, add the(LOOP) like symbol into table
    private void addAddressSymbol(List<String> code) {
        int count = 0;
        for (String line : code) {
            int lineType = checkInstructionType(line);

            if (lineType == 2) {
                String symbol = line.substring(1, line.indexOf(")"));
                symbolTable.put(symbol, count);
            }
            else {
                count += 1;
            }
        }
    }

    /**
     * second pass of the program
     */
    private List<String> translateProgram(List<String> code) {
        List<String> resultCode = new LinkedList<>();
        for (String line : code) {
            System.out.println("line is"+line);
            int type = checkInstructionType(line);
            if (type == 0) { // instruction is A instruction
                resultCode.add(createAInstruction(line));
            }

            if (type == 1) { // instruction is C instruction
                resultCode.add(createCInstruction(line));
            }
        }

        return resultCode;
    }


    private void writeFile(List<String> code) throws IOException {
        String[] desName = fileName.split("\\.");
        System.out.println(Arrays.toString(desName));
        Files.write(Paths.get(desName[0]+".hack"), code);
    }

    private String createAInstruction(String line) {
        int currentAddress;
        line = line.substring(1); // remove the first @ symbol
        if (isNumeric(line)) {
            currentAddress = Integer.parseInt(line);
        }
        else if (symbolTable.containsKey(line)) {
            currentAddress = symbolTable.get(line);
        } else {
            currentAddress = address++;
            symbolTable.put(line, currentAddress);
        }

        String binary = Integer.toBinaryString(currentAddress);
        String ret = "0000000000000000" + binary;
        return ret.substring(ret.length() - 16);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    private String createCInstruction(String line) {
        String[] symbols = line.split(";");  // dest = comp;jump, get the symbols on the lfet


        String jumpString;
        if(symbols.length>1) {
            String jumpIns = symbols[1].trim();
            jumpString = jumpTable.get(jumpIns);
        }
        else{
            jumpString = "000";
        }


        String[] leftSymbol = symbols[0].trim().split("=");
        String comString;
        String destString;
        if(leftSymbol.length ==1 ) // only have comp
        {
            comString = compTable.get(leftSymbol[0]);
            destString = "000";
        }

        else{
            comString = compTable.get(leftSymbol[1]);
            destString=destTable.get(leftSymbol[0]);
        }


        return "111"+comString+destString+jumpString;
    }

    /**
     * return a number represents instruction type
     * 0: a instruction
     * 1: c instruction
     * 2:() instruction of the form(xxx)
     */

    private int checkInstructionType(String line) {
        if (line.charAt(0) == '(') {
            return 2;
        }

        if (line.charAt(0) == '@') {
            return 0;
        }

        return 1;

    }

}
