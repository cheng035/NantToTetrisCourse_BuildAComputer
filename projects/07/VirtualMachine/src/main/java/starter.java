import java.io.IOException;
import java.util.List;

public class starter {

    public static void main(String[] args) throws IOException {
        FileHandler fileHandler = new FileHandler();
        Translator translator = new Translator();
        List<String[]> commands = fileHandler.readFile();
        translator.translateCommands(commands);
        fileHandler.writeFile(translator.getOutputCommands());
    }
}
