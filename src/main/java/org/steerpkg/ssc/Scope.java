package org.steerpkg.ssc;

import org.steerpkg.ssc.lex.Lexer;
import org.steerpkg.ssc.parse.Parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Scope extends HashMap<String, Object> {

    public Scope parent;

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public int getInt(String key) {
        return (int) get(key);
    }

    public double getNumber(String key) {
        return (double) get(key);
    }

    public boolean getBool(String key) {
        return (boolean) get(key);
    }

    public Scope getScope(String key) {
        return (Scope) get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] getArray(String key) {
        return (T[]) get(key);
    }

    public String serializeArray(Object[] array) {
        StringBuilder builder = new StringBuilder("[");
        for (Object object : array)
            builder.append(serializeValue(object)).append(",");

        return builder.substring(0, builder.toString().length() - 1) + "]";
    }

    public String serializeValue(Object entry) {
        StringBuilder builder = new StringBuilder();
        if (entry instanceof Scope)
            builder.append(((Scope) entry).toString());
        else if (entry instanceof Object[])
            builder.append(serializeArray((Object[]) entry));
        else if (entry instanceof String)
            builder.append("\"").append(entry).append("\"");
        else if (entry == null)
            builder.append("null");
        else
            builder.append(entry);
        return builder.toString();
    }

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
        if (parent != null) builder.append("}");
        return builder.toString();
    }

    public static Scope parse(InputStream source) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        while ((read = source.read(buffer)) != -1)
            out.write(buffer, 0, read);
        return parse(out.toString());
    }

    public static Scope parse(byte[] source, Charset charset) {
        return parse(new String(source, charset));
    }

    public static Scope parse(byte[] source) {
        return parse(new String(source));
    }

    public static Scope parse(String source) {
        return parse(source.toCharArray());
    }

    public static Scope parse(char[] source) {
        return new Parser(new Lexer(source).lex()).parseScope(null);
    }
}