package fromNand.VirtualMachine;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class Starter implements CommandLineRunner {
    @Resource
    FileService fileHandler;

    @Resource
    Translator translator;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Starter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<String[]> commands = fileHandler.readFile();
        translator.translateCommands(commands);
        fileHandler.writeFile();
    }
}
