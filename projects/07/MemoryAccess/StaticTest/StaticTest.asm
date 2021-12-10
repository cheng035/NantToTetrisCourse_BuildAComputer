@111   //[push, constant, 111]
D=A
@SP
A=M
M=D
@SP
M=M+1
@333   //[push, constant, 333]
D=A
@SP
A=M
M=D
@SP
M=M+1
@888   //[push, constant, 888]
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP   //[pop, static, 8]
A=M
A=A-1
D=M
@SP
M=M-1
@Foo.8
M=D
@SP   //[pop, static, 3]
A=M
A=A-1
D=M
@SP
M=M-1
@Foo.3
M=D
@SP   //[pop, static, 1]
A=M
A=A-1
D=M
@SP
M=M-1
@Foo.1
M=D
@Foo.3   //[push, static, 3]
D=M
@SP
A=M
M=D
@SP
M=M+1
@Foo.1   //[push, static, 1]
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP   //[sub]
A=M
A=A-1
D=M
@SP
M=M-1
@SP
A=M
A=A-1
M=M-D
@Foo.8   //[push, static, 8]
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP   //[add]
A=M
A=A-1
D=M
@SP
M=M-1
@SP
A=M
A=A-1
M=D+M
