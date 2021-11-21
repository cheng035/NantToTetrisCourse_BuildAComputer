import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHandler {
    private String fileName;
    public List<String[]> readFile() {
        System.out.println("please enter your file name");
        Scanner sc = new Scanner(System.in);
        fileName = sc.nextLine();
        List<String[]> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            //filter comment empty line and trim the String
            list = stream
                .filter(line -> !line.startsWith("/")) // remove comment line
                .filter(line -> !line.isEmpty()) // remove empty line
                .map(String::trim)// remove white space
                .map(line -> line.split("//")[0].trim()) // remove the comment
                .map(line -> line.split("\\s"))
                .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public void writeFile(List<String> code) throws IOException {
        String[] desName = fileName.split("\\.");
        System.out.println(Arrays.toString(desName));
        Files.write(Paths.get(desName[0]+".asm"), code);
    }


}
