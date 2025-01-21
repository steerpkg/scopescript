package org.steerpkg.ssc.lex;

/**
 * Debug information (line:character).
 * @since 0.1.0
 * @author shiftfox
 */
public class DebugInfo {

    /**
     * The line a token was encountered in.
     */
    public int line;
    /**
     * The character a token was encountered in.
     */
    public int character;

    /**
     * Creates new debug information.
     * @param line The line a token was encountered in
     * @param character The character a token was encountered in
     */
    public DebugInfo(int line, int character) {
        this.line = line;
        this.character = character;
    }

    /**
     * Creates new debug information from this debug information.
     * @return The new debug information
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DebugInfo clone() {
        return new DebugInfo(line, character);
    }
}