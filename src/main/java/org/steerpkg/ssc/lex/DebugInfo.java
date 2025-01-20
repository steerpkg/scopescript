package org.steerpkg.ssc.lex;

public class DebugInfo {

    public int character = 0;
    public int line = 0;

    @Override
    public DebugInfo clone() {
        try {
            return (DebugInfo) super.clone();
        } catch (Exception ignored) { return null; }
    }
}