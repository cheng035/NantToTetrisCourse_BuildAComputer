package fromNand.VirtualMachine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Translator {
    @Resource
    MemoryService memoryService;
    @Resource
    ArithmeticService arithmeticService;

    @Resource
    CommandOutput commandOutput;

    @Resource
    FunctionService functionService;

    private static HashMap<String, String> addressMap;

    int count = 0;
    static {
        addressMap = new HashMap<String, String>() {{
            put("local","LCL");
            put("argument", "ARG");
            put("this", "THIS");
            put("that", "THAT");
        }};
    }
    public void translateCommands(List<String[]> commands) {

        for (String[] command : commands){
            int k = commandOutput.getSize();
            translateCommand(command);
            commandOutput.addToPrevious(k,Arrays.toString(command));
        }

    }



private void translateCommand(String[] command){

    if (command[0].equals("push")){
        if(addressMap.containsKey(command[1]))
            memoryService.pushBasic(command);
        else if(command[1].equals("constant"))
            memoryService.pushConstant(command);

        else if(command[1].equals("static"))
            memoryService.pushStatic(command);
        else if(command[1].equals("temp"))
            memoryService.pushTemp(command);
        else
            memoryService.pushPointer(command);
    }

    else if (command[0].equals("function")){
        System.out.println("function"+command[1]);
        if(functionService.getFunctionName()!=null
            && functionService.getFunctionName().equals("Sys.init")){
        }
        functionService.setFunctionName(command[1]);
        functionService.initializeFunction(command);
    }

    else if(command[0].equals("return")){
        functionService.returnFunction();
    }

    else if(command[0].equals("call")){
        functionService.callFunction(command);
    }

    else if(command[0].equals("label")){
        functionService.label(command);
    }


    else if(command[0].equals("if-goto")){
        functionService.ifGoTo(command);
    }

    else if(command[0].equals("goto")){
        functionService.goTo(command);
    }

    else if (command.length == 3 &&command[0].equals("pop")){
        if(addressMap.containsKey(command[1]))
            memoryService.popBasic(command);
        else if(command[1].equals("static"))
            memoryService.popStatic(command);
        else if(command[1].equals("temp"))
            memoryService.popTemp(command);
        else
            memoryService.popPointer(command);
    }

    else{
        switch (command[0]){
            case "add": {
                arithmeticService.add();
                break;
            }

            case "sub":{
                arithmeticService.sub();
                break;
            }

            case "neg":{
                arithmeticService.neg();
                break;
            }

            case "eq":{
                arithmeticService.eq();
                break;
            }

            case "gt":{
                arithmeticService.gt();
                break;
            }

            case "lt":{
                arithmeticService.lt();
                break;
            }

            case "and":{
                arithmeticService.and();
                break;

            }

            case "or":{
                arithmeticService.or();
                break;
            }

            case "not":{
                arithmeticService.not();
                break;
            }


        }
        
    }


}
}
