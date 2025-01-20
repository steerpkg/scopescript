package org.steerpkg.ssc.parse;

import org.steerpkg.ssc.Scope;
import org.steerpkg.ssc.lex.Token;
import org.steerpkg.ssc.lex.TokenType;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Parser {

    private int i = 0;
    private final List<Token> source;

    public Parser(List<Token> source) {
        this.source = source;
    }

    private TokenType peek(int skip) {
        if (i + skip > source.size()) return null;
        return source.get(i + skip).type;
    }

    private Token peekGetToken() {
        if (i - 1 > source.size()) return null;
        return source.get(i - 1);
    }

    private Token consume() {
        Token token = source.get(i);
        i++;
        return token;
    }

    public Object parseVariable() {
        Token token = consume();
        switch (token.type) {
            case NULL:
            case STRING:
            case NUMBER:
                return token.value;
            case TRUE:
                return true;
            case FALSE:
                return false;
            case BRACE_L:
                return parseScope();
            case BRACKET_L:
                return parseArray();
            default:
                throw new ScopeScriptParseException(token);
        }
    }

    public Object[] parseArray() {
        List<Object> list = new LinkedList<>();
        while (true) {
           list.add(parseVariable());
           Token token = consume();
           if (token.type == TokenType.BRACKET_R)
               break;
           if (token.type != TokenType.COMMA)
               throw new ScopeScriptParseException(token);
        }

        return list.toArray();
    }

    public Scope parseScope() {
        Scope scope = new Scope();
        while (peek(0) != TokenType.BRACE_R) {
            Token token = consume();
            if (token.type != TokenType.IDENTIFIER)
                throw new ScopeScriptParseException(token);

            consume();
            if (peek(-1) == TokenType.EQUALS)
                scope.put((String) token.value, parseVariable());
            else if (peek(-1) == TokenType.BRACE_L)
                scope.put((String) token.value, parseScope());
            else if (peek(-1) == TokenType.BRACKET_L)
                scope.put((String) token.value, parseArray());
            else
                throw new ScopeScriptParseException(Objects.requireNonNull(peekGetToken()));
        }
        consume();

        return scope;
    }
}