import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;

@Data
public class Translator {
    private static HashMap<String, String> addressMap;
    private List<String> outputCommands;
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
        outputCommands = new LinkedList<>();
        for (String[] command : commands){

            outputCommands.add("//"+ Arrays.toString(command));
            translateCommand(command);

        }
        addEnd();

    }



private void translateCommand(String[] command){
    if (command.length == 3 && command[0].equals("push")){
        if(addressMap.containsKey(command[1]))
            pushBasic(command);
        else if(command[1].equals("constant"))
            pushConstant(command);

        else if(command[1].equals("static"))
            pushStatic(command);
        else if(command[1].equals("temp"))
            pushTemp(command);
        else
            pushPointer(command);
    }

    else if (command.length == 3 &&command[0].equals("pop")){
        if(addressMap.containsKey(command[1]))
            popBasic(command);
        else if(command[1].equals("static"))
            popStatic(command);
        else if(command[1].equals("temp"))
            popTemp(command);
        else
            popPointer(command);
    }

    else{
        switch (command[0]){
            case "add": {
                add();
                break;
            }

            case "sub":{
                sub();
                break;
            }

            case "neg":{
                neg();
                break;
            }

            case "eq":{
                eq();
                break;
            }

            case "gt":{
                gt();
                break;
            }

            case "lt":{
                lt();
                break;
            }

            case "and":{
                and();
                break;

            }

            case "or":{
                or();
                break;
            }

            case "not":{
                not();
                break;
            }


        }
        
    }
}





    //    @ i
//    D = A
//    @ commandAddr
//
//    A = D+A
//    D = M
//
//    @SP
//    A = M
//    M = D
//    @SP
//    M=M+1

    private void gotoAddress(String basic, String shift){
        outputCommands.add("@"+shift);
        outputCommands.add("D=A");
        outputCommands.add("@"+basic);
        outputCommands.add("A=M");
        outputCommands.add("A=D+A");
    }

    private void getAddress(String basic, String shift){
        gotoAddress(basic,shift);
        outputCommands.add("D=A");
    }

    private void getAddressContent(String basic, String shift){
        gotoAddress(basic,shift);
        outputCommands.add("D=M");
    }

    //push xxx  i
    /**
     * @author Haohan Cheng.
     * @param command input command
     */
    private void pushBasic (String[] command){
        getAddressContent(command[2], addressMap.get(command[1]));
        writeToStack();
    }



    //    @i
   //    D=a
    //    @SP
//    A = M
//    M = D
//    @SP
//    M=M+1
    private void pushConstant(String[] command){
        outputCommands.add("@"+command[2]);
        outputCommands.add("D=A");
        writeToStack();
    }

//    @Foo.command[2]
//    D=M
//    push to stack
    private void pushStatic(String[] command){
        outputCommands.add("@Foo."+command[2]);
        outputCommands.add("D=M");
        writeToStack();
    }

    //@ i
    // D=A
    //@ 5
    //A= D+A
    // D  = M
    private void pushTemp(String[] command){
        int add = 5 + Integer.parseInt(command[2]);
        outputCommands.add("@"+add);
        writeToStack();
    }

    private void pushPointer(String[] command){
        goToPointerAddress(command);
        writeToStack();

    }





    //@i
    //D=A
    //@ COMMAND[1]
    //@ A=A+Dqoxz
    //D=A

    //@SP
    //
    private void popBasic (String[] command){
        getAddress(addressMap.get(command[1]),command[2]);
        saveAddress();
        popStack();
        writeToSavedAddress();

    }


    private void popStatic(String[] command){
        popStack();
        outputCommands.add("@foo."+command[2]);
        outputCommands.add("M=D");
    }

    private void popTemp(String[] command){
        popStack();
        int add = 5 + Integer.parseInt(command[2]);
        outputCommands.add("@"+add);
        outputCommands.add("M=D");
    }

    private void popPointer(String[] command){
        goToPointerAddress(command);
        saveAddress();
        popStack();
        writeToSavedAddress();

    }

    private void saveAddress(){
        outputCommands.add("@tp_save");
        outputCommands.add("M=D");
    }


    private void writeToSavedAddress(){
        outputCommands.add("@tp_save");
        outputCommands.add("A=M");
        outputCommands.add("M=D");
    }

    private void goToPointerAddress(String[] command){
        String addr;
        if (command[2].equals("0")){
            addr = "THIS";
        }
        else{
            addr= "THAT";
        }

        outputCommands.add("@"+addr);
        outputCommands.add("D=M");
    }







// popStack
//  goToStack()
//    D=D-M
//
//    @ equal0
//        D;JEQ
//
//
//      M=0
//
//    @ END0
//    0;JMP
//
//        (equal0)
//        gotoStack()
//            M=1
//
//    (END0)

    private void compare(String compare){
        popStack();
        goToStack();
        outputCommands.add("D=M-D");
        outputCommands.add("@EQUAL"+count);
        outputCommands.add(compare);
        goToStack();
        outputCommands.add("M=0");
        outputCommands.add("@END"+count);
        outputCommands.add("0;JMP");
        outputCommands.add(String.format("(EQUALS%d)",count));
        goToStack();
        outputCommands.add("M=1");
        outputCommands.add(String.format("(END%d)",count));
        count+=1;
    }

    private void eq(){
        compare("D;JEQ");
    }

    private void gt(){
        compare("D;JGT");
    }

    private void lt(){
        compare("D;JLT");
    }



    private void add(){
        opStack("M=M+D");
    }

    private void sub(){
        opStack("M=M-D");
    }

    private void and(){
        opStack("M=D&M");
    }

    private void or(){
        opStack("M=D|M");
    }

    private void not(){
        goToStack();
        outputCommands.add("M=!M");
    }

    private void neg(){
        goToStack();
        outputCommands.add("M=-M");
    }


    private void opStack(String command){
        popStack();
        goToStack();
        outputCommands.add("A=A-1");
        outputCommands.add(command);
    }


    private void goToStack(){
        outputCommands.add("@SP");
        outputCommands.add("A=M");
    }

    private void writeToStack(){
        goToStack();
        outputCommands.add("M=D");
        outputCommands.add("@SP");
        outputCommands.add("M=M+1");
    }

    private void popStack(){
        goToStack();
        outputCommands.add("A=A-1");
        outputCommands.add("D=M");
        outputCommands.add("@SP");
        outputCommands.add("M=M-1");
    }

    private void addEnd(){
        outputCommands.add("(END)");
        outputCommands.add("@END");
        outputCommands.add("0;JMP");
    }


}
