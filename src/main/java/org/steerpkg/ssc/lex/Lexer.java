package org.steerpkg.ssc.lex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Converts a source <code>char[]</code> into a list of tokens.
 * @since 0.1.0
 * @author shiftfox
 */
public class Lexer {

    /**
     * The source character array.
     */
    private final char[] source;
    /**
     * The current index.
     */
    private int i;
    /**
     * What mode is the lexer on (string, comment or default)?
     */
    private String mode = "\0";
    /**
     * Debug information (line:character).
     */
    private final DebugInfo info = new DebugInfo(0, 0);

    /**
     * Creates a new lexer from a source character array.
     * @param source The source character array.
     */
    public Lexer(char[] source) {
        this.source = source;
    }

    /**
     * Sees what character we're at + the amount to skip.
     * @param skip The amount of characters forward to look
     * @return The character
     */
    private char peek(int skip) {
        if (i + skip >= source.length) return '\0';
        return source[i + skip];
    }

    /**
     * Gets a character, increases the current index and updates debug information.
     * @return The current character
     */
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

    /**
     * Is the lexer not at the end of the file?
     * @return If the lexer is not at the end of the file.
     */
    public boolean isNotEOF() {
        return i < source.length;
    }

    /**
     * Converts an identifier builder into a token.
     * @param list The list of tokens to add to
     * @param identifier The identifier builder
     * @param stringMode Are we lexing a string or not?
     */
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

    /**
     * Converts the source character array into a list of tokens.
     * @return The list of tokens
     */
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
        list.add(new Token(TokenType.BRACE_R, info));

        return new ArrayList<>(list);
    }
}
