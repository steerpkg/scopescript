package org.steerpkg.ssc;

import org.steerpkg.ssc.lex.Lexer;
import org.steerpkg.ssc.parse.Parser;

import java.util.HashMap;

public class Scope extends HashMap<String, Object> {

    // TODO utility methods (e.g. getString, getBool, set)

    public static Scope parse(String source) {
        return new Parser(new Lexer(source.toCharArray()).lex()).parseScope();
    }
}