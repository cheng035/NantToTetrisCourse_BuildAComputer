package com.example.analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class FileService {
    private String fileName;


    public HashMap<String, List<String>> readFile() {
        System.out.println("please enter your file name");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();

        if(! name.equals("re")){
            fileName = name;
        }

        final File folder = new File(fileName);

        File[] fileList = Objects.requireNonNull(folder.listFiles(File::isFile));
        HashMap<String,List<String>> retList = new  HashMap<String,List<String>>();



        for (File file : fileList) {
            if (!file.getName().endsWith("jack")){
                continue;
            }

            List<String> list = new LinkedList<>();
            try (Stream<String> stream = Files.lines(Paths.get(file.toURI()))) {

                //filter comment empty line and trim the String
                list = stream

                    .filter(line -> !line.startsWith("/")) // remove comment line
                    .map(String::trim)// remove white space
                    .map(line -> line.split("//")[0].trim()) // remove the comment
                    .filter(line -> !line.isEmpty()) // remove empty line
//                    .map(line -> line.split("\\s"))
                    .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }

            retList.put(file.getName(),list);

        }
        return retList;

    }

//
//    public void writeFile() throws IOException {
//        String[] desName = fileName.split("\\.");
//        System.out.println(Arrays.toString(desName));
//        Files.write(Paths.get(desName[0] + ".asm"), commandOutput.outputList);
//    }


}
