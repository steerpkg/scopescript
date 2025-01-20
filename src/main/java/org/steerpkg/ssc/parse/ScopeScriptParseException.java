package org.steerpkg.ssc.parse;

import org.steerpkg.ssc.lex.Token;

public class ScopeScriptParseException extends RuntimeException {

    public ScopeScriptParseException(Token cause) {
        super("Unexpected token: " + cause + " (" + cause.info.line + ":" + cause.info.character + ")");
    }
}
