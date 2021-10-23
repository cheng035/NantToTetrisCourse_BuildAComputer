// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

//set the number of register we need to light
@24576
D=A

@n
M=D



(LOOP)
//reset scr 
	@SCREEN
	D=A

	@scr
	M=D
	
	@KBD //get the keyboard
	D=M
	
	@LOOP  //if the keyboard is not pressed, go back
	D;JEQ 
	
	//else dark the screen
	(LOOP2)  // LOOP FOR LIGHT
		@scr //get the address of screen
		A=M
		M=-1
		
		
		
		@scr
		M=M+1
		
		@n // get total value
		D=M
		
		@scr
		D=D-M  //get the value of n-i
		
	@LOOP2
	D;JGT
	
	//turn it back to white
	
	@SCREEN
	D=A

	@scr
	M=D
		(LOOP3)  // LOOP FOR LIGHT
		@scr //get the address of screen
		A=M
		M=0
		
		
		
		@scr
		M=M+1
		
		@n // get total value
		D=M
		
		@scr
		D=D-M  //get the value of n-i
		
	@LOOP3
	D;JGT
	 
	
	
	
	

@LOOP
0;JMP

