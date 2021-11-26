package fromNand.VirtualMachine;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ArithmeticService {
    int count = 0;

    @Resource
    MemoryService memoryService;

    @Resource
    CommandOutput outputCommands;

    public void opStack(String command){
        memoryService.popStack();
        memoryService.getStackTop();
        outputCommands.add(command);
    }


    private void compare(String compare){
        memoryService.popStack();
        memoryService.getStackTop();
        outputCommands.add("D=M-D");
        outputCommands.add("@EQUAL"+count);
        outputCommands.add(compare);
        memoryService.getStackTop();
        outputCommands.add("M=0");
        outputCommands.add("@END"+count);
        outputCommands.add("0;JMP");
        outputCommands.add(String.format("(EQUAL%d)",count));
        memoryService.goToStack();
        outputCommands.add("A=A-1");
        outputCommands.add("M=-1");
        outputCommands.add(String.format("(END%d)",count));
        count+=1;
    }




    public void eq(){
        compare("D;JEQ");
    }

    public void gt(){
        compare("D;JGT");
    }

    public void lt(){
        compare("D;JLT");
    }



    public void add(){
        opStack("M=D+M");
    }

    public void sub(){
        opStack("M=M-D");
    }

    public void and(){
        opStack("M=D&M");
    }

    public void or(){
        opStack("M=D|M");
    }


    public void not(){
        memoryService.getStackTop();
        outputCommands.add("M=!M");
    }

    public void neg(){
        memoryService.getStackTop();
        outputCommands.add("M=-M");
    }

}
