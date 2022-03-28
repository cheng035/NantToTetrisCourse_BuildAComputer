package com.example.analyzer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
    String token;
    TokenType type;
}
