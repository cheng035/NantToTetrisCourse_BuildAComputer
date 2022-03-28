package com.example.analyzer;

import java.security.InvalidParameterException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompletionEng {

    Tokenizer tokenizer ;
    List<Pair> returnList;

    private void addRet(String symbol, int type){
        returnList.add(new Pair(symbol,type));

    }

    private void addToken(Token token){
        String type = token.getType().toString().toLowerCase();
        returnList.add(new Pair(type,1));
        returnList.add(new Pair(token.getToken(),0));
        returnList.add(new Pair(type,2));

    }
    private void compileClass(){
        addRet("class",1);
        Token token = tokenizer.advance();

        if (token.type != TokenType.IDENTIFIER){
            throw new InvalidParameterException("invalid token at at line");
        }



        addRet("class",2);
    }




}
