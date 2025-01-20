package org.steerpkg.ssc.lex;

public class DebugInfo {

    public int line;
    public int character;

    public DebugInfo(int line, int character) {
        this.line = line;
        this.character = character;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DebugInfo clone() {
        return new DebugInfo(line, character);
    }
}