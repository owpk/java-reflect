```bash
$ java Reflect -a
java.nio.Bits
java.io.BufferedInputStream
java.io.BufferedOutputStream
java.io.BufferedReader
java.io.BufferedWriter
java.io.ByteArrayInputStream
java.io.ByteArrayOutputStream
java.io.CharArrayReader
java.io.CharArrayWriter
java.io.CharConversionException
java.io.Closeable
java.io.Console
java.io.DataInput
java.io.DataInputStream
java.io.DataOutput
java.io.DataOutputStream
java.io.DefaultFileSystem
java.io.DeleteOnExitHook
java.io.EOFException
java.io.ExpiringCache
java.io.Externalizable
...
# Prints all default jre classes
```

```bash
$ java Reflect Object
java.lang.Object
void wait(long)
void wait(long, int)
void wait()
boolean equals(Object)
String toString()
int hashCode()
Class getClass()
void notify()
void notifyAll()

# Prints all available methods in a specific class
```
