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

    private Object lookup(Scope scope, String key) {
        if (scope.containsKey(key)) {
            Object value = scope.get(key);
            if (value != null) {
                Object result = value;
                while (peek(0) == TokenType.BRACKET_L) {
                    consume();
                    Token token = consume();
                    switch (token.type) {
                        case STRING:
                        case IDENTIFIER:
                            assert value instanceof Scope;
                            result = ((Scope) result).get((String) token.value);
                            break;
                        case NUMBER:
                            assert value instanceof Object[];
                            result = ((Object[]) result)[((Double) token.value).intValue()];
                            break;
                        default:
                            throw new SSCParseException(token);
                    }

                    Token rightBracket = consume();
                    if (rightBracket.type != TokenType.BRACKET_R)
                        throw new SSCParseException(rightBracket);
                }
                return result;
            }
        }

        if (scope.parent == null)
            return null;
        return lookup(scope.parent, key);
    }

    public Object parseVariable(Scope parent) {
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
                return parseScope(parent);
            case BRACKET_L:
                return parseArray(parent);
            case IDENTIFIER:
                return lookup(parent, (String) token.value);
            default:
                throw new SSCParseException(token);
        }
    }

    public Object[] parseArray(Scope parent) {
        List<Object> list = new LinkedList<>();
        while (true) {
           list.add(parseVariable(parent));
           Token token = consume();
           if (token.type == TokenType.BRACKET_R)
               break;
           if (token.type != TokenType.COMMA)
               throw new SSCParseException(token);
        }

        return list.toArray();
    }

    public Scope parseScope(Scope parent) {
        Scope scope = new Scope(parent);
        while (peek(0) != TokenType.BRACE_R) {
            Token token = consume();
            if (token.type != TokenType.IDENTIFIER)
                throw new SSCParseException(token);

            consume();
            if (peek(-1) == TokenType.EQUALS)
                scope.put((String) token.value, parseVariable(scope));
            else if (peek(-1) == TokenType.BRACE_L)
                scope.put((String) token.value, parseScope(scope));
            else if (peek(-1) == TokenType.BRACKET_L)
                scope.put((String) token.value, parseArray(scope));
            else
                throw new SSCParseException(Objects.requireNonNull(peekGetToken()));
        }
        consume();

        return scope;
    }
}