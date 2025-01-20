package org.steerpkg.ssc.lex;

public class Token {

    public TokenType type;
    public Object value;
    public DebugInfo info;

    public Token(TokenType type, DebugInfo info) {
        this(type, null, info);
    }

    public Token(TokenType type, Object value, DebugInfo info) {
        this.type = type;
        this.value = value;
        this.info = info.clone();
    }

    @Override
    public String toString() {
        if (this.value != null) return type.toString() + "{" + value + "}";
        return type.toString();
    }
}
