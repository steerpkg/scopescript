package org.steerpkg.ssc.parse;

import org.steerpkg.ssc.Scope;
import org.steerpkg.ssc.lex.Token;
import org.steerpkg.ssc.lex.TokenType;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Parses a list of tokens into a Scope object.
 * @since 0.1.0
 * @author shiftfox
 */
public class Parser {

    /**
     * The current index.
     */
    private int i = 0;
    /**
     * The source list of tokens.
     */
    private final List<Token> source;

    /**
     * Creates a new parser for a list of tokens.
     * @param source The source list of tokens
     */
    public Parser(List<Token> source) {
        this.source = source;
    }

    /**
     * Shows what the <code>next + skip</code> token is.
     * @param skip The amount of tokens to skip
     * @return The type of the next token
     */
    private TokenType peek(int skip) {
        if (i + skip > source.size()) return null;
        return source.get(i + skip).type;
    }

    /**
     * Shows what the last token is.
     * @return The last token
     */
    private Token peekGetToken() {
        if (i - 1 > source.size()) return null;
        return source.get(i - 1);
    }

    /**
     * Gets a token, and increases the current index.
     * @return The current token
     */
    private Token consume() {
        Token token = source.get(i);
        i++;
        return token;
    }

    /**
     * Looks up a variable recursively through scopes.
     * @param scope The scope to look into
     * @param key The key to search for
     * @return The value of the key, or null if not found
     */
    private Object lookup(Scope scope, String key) {
        if (scope.containsKey(key)) {
            Object value = scope.get(key);
            if (value != null) {
                while (peek(0) == TokenType.BRACKET_L) {
                    consume();
                    Token token = consume();
                    switch (token.type) {
                        case STRING:
                        case IDENTIFIER:
                            assert value instanceof Scope;
                            value = ((Scope) value).get((String) token.value);
                            break;
                        case NUMBER:
                            assert value instanceof Object[];
                            value = ((Object[]) value)[((Double) token.value).intValue()];
                            break;
                        default:
                            throw new SSCParseException(token);
                    }

                    Token rightBracket = consume();
                    if (rightBracket.type != TokenType.BRACKET_R)
                        throw new SSCParseException(rightBracket);
                }
                return value;
            }
        }

        if (scope.parent == null)
            return null;
        return lookup(scope.parent, key);
    }

    /**
     * Parses a variable.
     * @param parent The scope this variable is part of (the parent in the case of arrays or scopes)
     * @return The parsed variable
     */
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

    /**
     * Parses an array.
     * @param parent The parent of the array
     * @return The parsed array
     */
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

    /**
     * Parses a scope.
     * @param parent The parent of the scope
     * @return The parsed scope
     */
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