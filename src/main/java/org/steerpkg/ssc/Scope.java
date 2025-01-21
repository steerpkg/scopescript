package org.steerpkg.ssc;

import org.steerpkg.ssc.lex.Lexer;
import org.steerpkg.ssc.parse.Parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * A ScopeScript scope.
 * @since 0.1.0
 * @author shiftfox
 */
public class Scope extends HashMap<String, Object> {

    /**
     * The scope's parent scope.
     */
    public Scope parent;

    /**
     * Creates a new scope with null as a parent.
     */
    public Scope() {
        this(null);
    }

    /**
     * Creates a new scope with a (nullable) parent.
     * @param parent The parent of the scope
     */
    public Scope(Scope parent) {
        this.parent = parent;
    }

    /**
     * Gets a value as a string.
     * @param key The key of the value
     * @return The value, if it exists
     */
    public String getString(String key) {
        return (String) get(key);
    }

    /**
     * Gets a number value as an integer.
     * @param key The key of the value
     * @return The value, if it exists
     */
    public int getInt(String key) {
        return ((Double) get(key)).intValue();
    }

    /**
     * Gets a number value as a double.
     * @param key The key of the value
     * @return The value, if it exists
     */
    public double getNumber(String key) {
        return (double) get(key);
    }

    /**
     * Gets a boolean value.
     * @param key The key of the value
     * @return The value, if it exists
     */
    public boolean getBool(String key) {
        return (boolean) get(key);
    }

    /**
     * Gets a value as a scope.
     * @param key The key of the value
     * @return The value, if it exists
     */
    public Scope getScope(String key) {
        return (Scope) get(key);
    }

    /**
     * Gets a value as an array.
     * @param key The key of the value
     * @return The value, if it exists
     * @param <T> The type of the array
     */
    @SuppressWarnings("unchecked")
    public <T> T[] getArray(String key) {
        return (T[]) get(key);
    }

    /**
     * Serializes an array into a ScopeScript array.
     * @param array The array to serialize.
     * @return The serialized array.
     */
    public String serializeArray(Object[] array) {
        StringBuilder builder = new StringBuilder("[");
        for (Object object : array)
            builder.append(serializeValue(object)).append(",");

        return builder.substring(0, builder.toString().length() - 1) + "]";
    }

    /**
     * Serializes a value into a ScopeScript value.
     * @param value The value to serialize
     * @return The serialized value
     */
    public String serializeValue(Object value) {
        String result;
        if (value instanceof Object[])
            result = serializeArray((Object[]) value);
        else if (value instanceof String)
            result = "\"" + value + "\"";
        else if (value == null)
            result = "null";
        else
            result = value.toString();
        return result;
    }

    /**
     * Converts a scope back into a ScopeScript file.
     * @return The serialized scope
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (parent != null) builder.append("{");
        for (Map.Entry<String, Object> entry : entrySet()) {
            builder.append(entry.getKey());
            if (!(entry.getValue() instanceof Scope || entry.getValue() instanceof Object[]))
                builder.append("=");
            builder.append(serializeValue(entry.getValue())).append(" ");
        }
        return builder.substring(0, builder.length() - 1) + (parent == null? "" : "}");
    }

    /**
     * Parses an InputStream into a scope.
     * @param source The source input stream
     * @return The parsed scope
     * @throws IOException If reading from the input stream fails
     */
    public static Scope parse(InputStream source) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        while ((read = source.read(buffer)) != -1)
            out.write(buffer, 0, read);
        return parse(out.toString().toCharArray());
    }

    /**
     * Parses a <code>byte[]</code> into a scope.
     * @param source The source <code>byte[]</code>
     * @param charset The encoding to decode the <code>byte[]</code> in
     * @return The parsed scope
     */
    public static Scope parse(byte[] source, Charset charset) {
        return parse(new String(source, charset).toCharArray());
    }

    /**
     * Parses a <code>byte[]</code> into a scope in UTF-8 encoding.
     * @param source The source <code>byte[]</code>
     * @return The parsed scope
     */
    public static Scope parse(byte[] source) {
        return parse(new String(source).toCharArray());
    }

    /**
     * Parses a string into a scope.
     * @param source The source string
     * @return The parsed scope
     */
    public static Scope parse(String source) {
        return parse(source.toCharArray());
    }

    /**
     * Parses a list of characters into a scope.
     * @param source The source <code>char[]</code>
     * @return The parsed scope
     */
    public static Scope parse(char[] source) {
        return new Parser(new Lexer(source).lex()).parseScope(null);
    }
}