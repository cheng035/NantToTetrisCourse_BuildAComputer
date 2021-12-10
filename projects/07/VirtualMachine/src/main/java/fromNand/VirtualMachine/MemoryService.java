package fromNand.VirtualMachine;

import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MemoryService {
    @Resource
    CommandOutput outputCommands;
    private static HashMap<String, String> addressMap;

    @Resource
    FunctionService functionService;

    @PostConstruct
    public void init() {

        addressMap = new HashMap<String, String>() {{
            put("local", "LCL");
            put("argument", "ARG");
            put("this", "THIS");
            put("that", "THAT");
        }};
    }

    private void gotoAddress(String basic, String shift) {
        outputCommands.add("@" + shift);
        outputCommands.add("D=A");
        outputCommands.add("@" + basic);
        outputCommands.add("A=M");
        outputCommands.add("A=D+A");
    }

    private void getAddress(String basic, String shift) {
        gotoAddress(basic, shift);
        outputCommands.add("D=A");
    }

    private void getAddressContent(String basic, String shift) {
        gotoAddress(basic, shift);
        outputCommands.add("D=M");
    }

    //push xxx  i

    /**
     * @param command input command
     * @author Haohan Cheng.
     */
    public void pushBasic(String[] command) {
        getAddressContent(addressMap.get(command[1]), command[2]);
        writeToStack();
    }


    //    @i
    //    D=a
    //    @SP
//    A = M
//    M = D
//    @SP
//    M=M+1
    public void pushConstant(String[] command) {
        outputCommands.add("@" + command[2]);
        outputCommands.add("D=A");
        writeToStack();
    }

    //    @Foo.command[2]
//    D=M
//    push to stack
    public void pushStatic(String[] command) {
        outputCommands.add("@"+getFileNameLabel() + command[2]);
        outputCommands.add("D=M");
        writeToStack();
    }

    //@ i
    // D=A
    //@ 5
    //A= D+A
    // D  = M
    public void pushTemp(String[] command) {
        int add = 5 + Integer.parseInt(command[2]);
        outputCommands.add("@" + add);
        outputCommands.add("D=M");
        writeToStack();
    }

    public void pushPointer(String[] command) {
        goToPointerAddress(command);
        outputCommands.add("D=M");
        writeToStack();

    }


    //@i
    //D=A
    //@ COMMAND[1]
    //@ A=A+Dqoxz
    //D=A

    //@SP
    //
    public void popBasic(String[] command) {
        getAddress(addressMap.get(command[1]), command[2]);
        saveAddress();
        popStack();
        writeToSavedAddress();

    }

    private String getFileNameLabel(){
        return functionService.getFunctionName().split("\\.")[0]+".";
    }

    public void popStatic(String[] command) {
        popStack();
        outputCommands.add("@"+getFileNameLabel() + command[2]);
        outputCommands.add("M=D");
    }

    public void popTemp(String[] command) {
        popStack();
        int add = 5 + Integer.parseInt(command[2]);
        outputCommands.add("@" + add);
        outputCommands.add("M=D");
    }

    public void popPointer(String[] command) {
        goToPointerAddress(command);
        outputCommands.add("D=A");
        saveAddress();
        popStack();
        writeToSavedAddress();

    }

    public void saveAddress() {
        outputCommands.add("@tp_save");
        outputCommands.add("M=D");
    }

    public void saveAddress(String label){
        outputCommands.add("@"+label);
        outputCommands.add("M=D");
    }


    public void writeToSavedAddress() {
        outputCommands.add("@tp_save");
        outputCommands.add("A=M");
        outputCommands.add("M=D");
    }

    public void writeToSavedAddress(String label) {
        outputCommands.add("@"+label);
        outputCommands.add("A=M");
        outputCommands.add("M=D");
    }

    public void goToPointerAddress(String[] command) {
        String addr;
        if (command[2].equals("0")) {
            addr = "THIS";
        } else {
            addr = "THAT";
        }

        outputCommands.add("@" + addr);
    }


    public void goToStack() {
        outputCommands.add("@SP");
        outputCommands.add("A=M");
    }

    public void getStackTop() {
        goToStack();
        outputCommands.add("A=A-1");
    }

    public void writeToStack() {
        goToStack();
        outputCommands.add("M=D");
        outputCommands.add("@SP");
        outputCommands.add("M=M+1");
    }

    public void popStack() {
        getStackTop();
        outputCommands.add("D=M");
        outputCommands.add("@SP");
        outputCommands.add("M=M-1");
    }


    // functions used by call function
    //push the basic address to the stack
    public void pushBasicAddress(String symbol) {
        getBasicAddress(symbol);
        writeToStack();
    }

    public void getBasicAddress(String symbol){
        outputCommands.add("@"+addressMap.get(symbol));
        outputCommands.add("D=M");
    }



    public void setArg(String n){
        outputCommands.add("@SP");
        outputCommands.add("D=M");

        outputCommands.add("@5");
        outputCommands.add("D=D-A");

        outputCommands.add("@"+n);
        outputCommands.add("D=D-A");

        outputCommands.add("@ARG");
        outputCommands.add("M=D");



    }


}
