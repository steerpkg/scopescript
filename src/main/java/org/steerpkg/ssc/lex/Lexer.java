package org.steerpkg.ssc.lex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Lexer {

    private char[] source;
    private int i;
    private String mode = "";
    private final DebugInfo info = new DebugInfo();

    public Lexer(char[] source) {
        this.source = source;
    }

    private char peek(int skip) {
        if (i + skip >= source.length) return '\0';
        return source[i + skip];
    }

    private char consume() {
        char c = peek(0);
        i++;
        if (c != '\n') info.character++;
        else {
            info.line++;
            info.character = 0;
        }
        return c;
    }

    public boolean isNotEOF() {
        return i < source.length;
    }

    private void flushIdentifier(List<Token> list, StringBuilder identifier, boolean stringMode) {

    }

    public List<Token> lex() {
        List<Token> list = new LinkedList<>();

        StringBuilder identifier = new StringBuilder();
        while (isNotEOF()) {
            char c = consume();
            if (mode.equals("#")) {
                if (c == '\n') mode = "";
                continue;
            } else if (mode.equals("##")) {
                if (c == '#' && peek(1) == '#') {
                    consume();
                    mode = "";
                }
                continue;
            }

            System.out.print(c);

            Token t = null;
            switch (c) {
                case '#':
                    if (peek(1) == '#') mode = "##";
                    else mode = "#";
                    continue;
            }

            flushIdentifier(list, identifier, false);
            if (t != null) list.add(t);
        }

        return new ArrayList<>(list);
    }
}
