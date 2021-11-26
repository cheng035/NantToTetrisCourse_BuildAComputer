package fromNand.VirtualMachine;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data

public class CommandOutput {
    List<String> outputList;

    @PostConstruct
    public void init(){
        this.outputList = new LinkedList<>();
    }

    public void add(String command){
        outputList.add(command);
    }
}
