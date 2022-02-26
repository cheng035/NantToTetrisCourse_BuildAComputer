package com.example.analyzer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class Tokenizer {
    LinkedList<Token> tokenList;
    static HashSet<String> keywordSet = new HashSet<String>() {{
        add("class");add("constructor");add("function");add("method");
        add("field");add("static");add("var");add("int");
        add("char");add("boolean");add("void");add("true");add("false");add("null");
        add("this");add("let");add("do");add("if");
        add("else");add("while");add("return");
    }};

    static HashSet<Character> symbolSet = new HashSet<Character>(){{
            add('{');add('}');add('(');add(')');add('[');add(']');add('.');
            add(',');add('+');add('-');add('*');add('/');add('&');add('|');
            add('<');add('>');add('=');add('~');add(';');

        }};


    public void init (List<String> jacks) {
        this.tokenList = new LinkedList<>();
        jacks.forEach(
            this::handleString
        );
    }

    private void handleString(String str) {
        System.out.println(str);
        int pointer = 0;
        while(pointer<str.length()){
            char cur = str.charAt(pointer);
            System.out.println("cur"+cur);
            StringBuilder curString = new StringBuilder();

            if(Util.isDigit(cur)){
                // handle the logic the current string is a number.
                // because if a new string is started with number, it can only be a numbmer.

                while ( pointer<str.length() && Util.isDigit(cur) && cur!=' '
                    && !symbolSet.contains(cur)){
                    System.out.println("cur"+cur);
                    pointer+=1;
                    if(pointer<str.length())
                        cur = str.charAt(pointer);
                }
                // can add a logic to determine if this a illegal identifier.

                addToken(curString.toString(),TokenType.INT_CONST);
            }

            else if(cur == '"'){ //handle the string constant
                pointer+=1;  //skip the first "
                cur = str.charAt(pointer);
                while ( pointer<str.length() && cur != '"'){
                    System.out.println("cur"+cur);
                    curString.append(cur);
                    pointer+=1;

                    if(pointer<str.length())
                        cur = str.charAt(pointer);
                }

                pointer+=1; // skip the last "
                addToken(curString.toString(),TokenType.STRING_CONST);
            }

            else if (cur == ' '){
                pointer+=1;
            }

            else if(symbolSet.contains(cur)){
                curString.append(cur);
                addToken(curString.toString(), TokenType.SYMBOL);
                pointer+=1;
            }

            else { //stars with character.
                while (cur!=' ' && !symbolSet.contains(cur)){
                    System.out.println(cur);
                    curString.append(cur);
                    pointer+=1;

                    if(pointer<str.length())
                        cur = str.charAt(pointer);
                }

                if (keywordSet.contains(curString.toString())){
                    addToken(curString.toString(), TokenType.KEYWORD);
                }

                else{
                    addToken(curString.toString(), TokenType.IDENTIFIER);
                }
            }
        }

    }



    private void addToken(String s, TokenType type) {
        this.tokenList.add(new Token(s,type));
    }

@Data
@AllArgsConstructor
    static class Token {
        String token;
        TokenType type;
    }

}
