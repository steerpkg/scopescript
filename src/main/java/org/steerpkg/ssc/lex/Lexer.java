package org.steerpkg.ssc.lex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Lexer {

    private char[] source;
    private int i;
    private String mode = "\0";
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
        String string = identifier.toString();
        identifier.setLength(0);

        if (stringMode) {
            list.add(new Token(TokenType.STRING, string, info));
            return;
        }

        if (string.isEmpty())
            return;

        switch (string) {
            case "true":
                list.add(new Token(TokenType.TRUE, info));
                return;
            case "false":
                list.add(new Token(TokenType.FALSE, info));
                return;
            case "null":
                list.add(new Token(TokenType.NULL, info));
                return;
        }

        try {
            double number = Double.parseDouble(string);
            list.add(new Token(TokenType.NUMBER, number, info));
            return;
        } catch (NumberFormatException ignored) {}

        list.add(new Token(TokenType.IDENTIFIER, string, info));
    }

    public List<Token> lex() {
        List<Token> list = new LinkedList<>();

        StringBuilder identifier = new StringBuilder();
        while (isNotEOF()) {
            char c = consume();
            switch (mode) {
                case "#":
                    if (c == '\n') mode = "\0";
                    continue;
                case "##":
                    if (c == '#' && peek(1) == '#') {
                        consume();
                        mode = "\0";
                    }
                    continue;
                case "\"":
                case "'":
                    if (c == mode.charAt(0)) {
                        mode = "\0";
                        flushIdentifier(list, identifier, true);
                    } else identifier.append(c);
                    continue;
            }

            Token t = null;
            switch (c) {
                case '[':
                    t = new Token(TokenType.BRACKET_L, info);
                    break;
                case ']':
                    t = new Token(TokenType.BRACKET_R, info);
                    break;
                case '{':
                    t = new Token(TokenType.BRACE_L, info);
                    break;
                case '}':
                    t = new Token(TokenType.BRACE_R, info);
                    break;
                case '=':
                    t = new Token(TokenType.EQUALS, info);
                    break;
                case ',':
                    t = new Token(TokenType.COMMA, info);
                    break;
                case '\n':
                case '\t':
                case '\r':
                case ' ':
                    break;
                case '\'':
                case '"':
                    mode = String.valueOf(c);
                    continue;
                case '#':
                    if (peek(1) == '#') mode = "##";
                    else mode = "#";
                    continue;
                default:
                    identifier.append(c);
                    continue;
            }

            flushIdentifier(list, identifier, false);
            if (t != null) list.add(t);
        }
        flushIdentifier(list, identifier, false);

        return new ArrayList<>(list);
    }
}
