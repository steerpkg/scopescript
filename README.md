# ScopeScript
A data language with easy to understand syntax.

```ssc
# This is a single line comment.
##
This is a block comment.
##

string = "hello world"
number = -12e-3
bool = true
nothing = null
array["hello world", 12e-3, false, null,
    { nested-array-scope = "hello world" }, ["nested array"]]

scope {
    name = "package"
    version = 0.1
}
```