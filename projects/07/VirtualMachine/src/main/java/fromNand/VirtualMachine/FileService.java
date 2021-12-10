package fromNand.VirtualMachine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class FileService {
    private String fileName;
    @Resource
    CommandOutput commandOutput;

    public List<String[]> readFile() {
        System.out.println("please enter your file name");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();

        if(! name.equals("re")){
            fileName = name;
        }

        final File folder = new File(fileName);

        File[] fileList = Objects.requireNonNull(folder.listFiles(File::isFile));
        List<List<String[]>> retList = new LinkedList<>();

        int count = 0 ;
        for(File file : fileList){
            if (file.getName().equals("Sys.vm")){
                File exchangeFile = fileList[0];
                fileList[0] = fileList[count];
                fileList[count] = exchangeFile;
            }
            count+=1;
        }



        for (File file : fileList) {
            if (!file.getName().endsWith("vm")){
                continue;
            }

            List<String[]> list = new LinkedList<>();
            try (Stream<String> stream = Files.lines(Paths.get(file.toURI()))) {

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
            retList.add(list);

        }

        return retList.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }


    public void writeFile() throws IOException {
        String[] desName = fileName.split("\\.");
        System.out.println(Arrays.toString(desName));
        Files.write(Paths.get(desName[0] + ".asm"), commandOutput.outputList);
    }


}
