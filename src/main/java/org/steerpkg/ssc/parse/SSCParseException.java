package org.steerpkg.ssc.parse;

import org.steerpkg.ssc.lex.Token;

public class SSCParseException extends RuntimeException {

    public SSCParseException(Token cause) {
        super("Unexpected token: " + cause + " (" + cause.info.line + ":" + cause.info.character + ")");
    }
}
