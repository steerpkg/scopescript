package org.steerpkg.ssc.lex;

public class Token {

    public TokenType type;
    public Object value;

    public Token(TokenType type) {
        this(type, null);
    }

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }
}
