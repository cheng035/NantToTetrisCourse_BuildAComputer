//[function, SimpleFunction.test, 2]
(SimpleFunction.test)
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M
M=D
@SP
M=M+1
//[push, local, 0]
@0
D=A
@LCL
A=M
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
//[push, local, 1]
@1
D=A
@LCL
A=M
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
//[add]
@SP
A=M
A=A-1
D=M
@SP
M=M-1
@SP
A=M
A=A-1
M=D+M
//[not]
@SP
A=M
A=A-1
M=!M
//[push, argument, 0]
@0
D=A
@ARG
A=M
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
//[add]
@SP
A=M
A=A-1
D=M
@SP
M=M-1
@SP
A=M
A=A-1
M=D+M
//[push, argument, 1]
@1
D=A
@ARG
A=M
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
//[sub]
@SP
A=M
A=A-1
D=M
@SP
M=M-1
@SP
A=M
A=A-1
M=M-D
//[return]
@LCL
D=M
@endFrame
M=D
@endFrame
D=M
@5
D=D-A
A=D
D=M
@retAddr
M=D
@SP
A=M
A=A-1
D=M
@SP
M=M-1
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D+1
@endFrame
D=M
@1
D=D-A
A=D
D=M
@THAT
M=D
@endFrame
D=M
@2
D=D-A
A=D
D=M
@THIS
M=D
@endFrame
D=M
@3
D=D-A
A=D
D=M
@ARG
M=D
@endFrame
D=M
@4
D=D-A
A=D
D=M
@LCL
M=D
(END)
@END
0;JMP