package org.steerpkg.ssc.lex;

/**
 * All types of tokens in ScopeScript.
 * @since 0.1.0
 * @author shiftfox
 */
public enum TokenType {
    STRING,
    NUMBER,
    TRUE,
    FALSE,
    NULL,
    EQUALS,
    BRACE_L,
    BRACE_R,
    BRACKET_L,
    BRACKET_R,
    COMMA,
    IDENTIFIER
}
