package fromNand.VirtualMachine;

import javax.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class FunctionService {
    @Resource
    MemoryService memoryService;
    @Resource
    CommandOutput commandOutput;


    int count = 0;

    String functionName = "default";

    public void callFunction(String[] command){

        String label = String.format("%s$ret.%d",command[1],count);

        count+=1;
        commandOutput.add("@"+label);
        commandOutput.add("D=A");
        memoryService.writeToStack();



        memoryService.pushBasicAddress("local");
        memoryService.pushBasicAddress("argument");
        memoryService.pushBasicAddress("this");
        memoryService.pushBasicAddress("that");
        memoryService.setArg(command[2]);

        commandOutput.add("@SP");
        commandOutput.add("D=M");
        commandOutput.add("@LCL");
        commandOutput.add("M=D");

        commandOutput.add("@"+command[1]);
        commandOutput.add("0;JMP");

        commandOutput.add(String.format("(%s)",label));


    }

    public void returnFunction(){
        memoryService.getBasicAddress("local");
        memoryService.saveAddress("endFrame");


       getElementByEndFrame(5);
        memoryService.saveAddress("retAddr");

        //reposition the return value for the caller
        //*ARG = POP()
        memoryService.popStack();
        commandOutput.add("@ARG");
        commandOutput.add("A=M");
        commandOutput.add("M=D");

        //SP = AGR+1
        memoryService.getBasicAddress("argument");
        commandOutput.add("@SP");
        commandOutput.add("M=D+1");

        restoreElement(1,"THAT");
        restoreElement(2,"THIS");
        restoreElement(3,"ARG");
        restoreElement(4,"LCL");

        commandOutput.add("@retAddr");
        commandOutput.add("A=M");
        commandOutput.add("0;JMP");


    }

    public void initializeFunction(String[] commands){
        // add a label for the string
        this.functionName = commands[1];

        commandOutput.add(String.format("(%s)",commands[1]));

        commandOutput.add("@0");
        commandOutput.add("D=A");
        for(int i = 0; i< Integer.parseInt(commands[2]); i++){
            memoryService.writeToStack();
        }
    }

    private void getElementByEndFrame(int shift){
        commandOutput.add("@endFrame");
        commandOutput.add("D=M");
        commandOutput.add("@"+shift);
        commandOutput.add("D=D-A");
        commandOutput.add("A=D");
        commandOutput.add("D=M");
    }

    private void restoreElement(int n, String label){
        getElementByEndFrame(n);
        commandOutput.add("@"+label);
        commandOutput.add("M=D");
    }

    public void ifGoTo (String[] commands){
        memoryService.popStack();
        commandOutput.add("@"+functionName+"$"+commands[1]);
        commandOutput.add("D;JNE");

    }

    public void goTo (String[] commands){
        commandOutput.add("@"+functionName+"$"+commands[1]);
        commandOutput.add("0;JMP");
    }

    public void label(String[] commands){
        commandOutput.add(String.format("(%s$%s)",functionName,commands[1]));
    }



}
