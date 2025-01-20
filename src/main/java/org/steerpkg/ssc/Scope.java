package org.steerpkg.ssc;

import java.util.HashMap;

public class Scope extends HashMap<String, Object> {

    // TODO utility methods (e.g. getString, getBool, set)

    public static Scope parse(String source) {
        return new Scope(); // TODO actual parsing
    }
}