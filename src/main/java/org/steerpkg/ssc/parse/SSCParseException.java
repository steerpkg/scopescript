package org.steerpkg.ssc.parse;

import org.steerpkg.ssc.lex.Token;

/**
 * An error for the ScopeScript parser, if it fails to parse.
 * @since 0.1.0
 * @author shiftfox
 */
public class SSCParseException extends RuntimeException {

    /**
     * Creates a new parsing error.
     * @param cause What token caused this issue?
     */
    public SSCParseException(Token cause) {
        super("Unexpected token: " + cause + " (" + cause.info.line + ":" + cause.info.character + ")");
    }
}
