package org.steerpkg.ssc.lex;

/**
 * Represents a token for the lexer.
 * @since 0.1.0
 * @author shiftfox
 */
public class Token {

    /**
     * The token's type.
     */
    public TokenType type;
    /**
     * The token's value.
     */
    public Object value;
    /**
     * Debug information (line:character).
     */
    public DebugInfo info;

    /**
     * Creates a new token without a value.
     * @param type The type of the token
     * @param info Debug information (line:character).
     */
    public Token(TokenType type, DebugInfo info) {
        this(type, null, info);
    }

    /**
     * Creates a new token
     * @param type The type of the token
     * @param value The value of the token
     * @param info Debug information (line:character).
     */
    public Token(TokenType type, Object value, DebugInfo info) {
        this.type = type;
        this.value = value;
        this.info = info.clone();
    }

    /**
     * String representation of a token.
     * @return String representation of a token
     */
    @Override
    public String toString() {
        if (this.value != null) return type.toString() + "{" + value + "}";
        return type.toString();
    }
}
