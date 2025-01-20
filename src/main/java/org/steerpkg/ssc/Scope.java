package org.steerpkg.ssc;

import org.steerpkg.ssc.lex.Lexer;

import java.util.HashMap;

public class Scope extends HashMap<String, Object> {

    // TODO utility methods (e.g. getString, getBool, set)

    public static Scope parse(String source) {
        System.out.println(new Lexer(source.toCharArray()).lex());
        return new Scope(); // TODO actual parsing
    }
}