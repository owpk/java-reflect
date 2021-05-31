### Intstall
```bash
# run from project root
$ ./install.sh

# uninstall
$ ./intall.sh --unistall
```
### Usage
```bash
$ reflect list
java.nio.Bits
java.io.BufferedInputStream
java.io.BufferedOutputStream
java.io.BufferedReader
java.io.BufferedWriter
...
# Prints all default jre classes
```
- add jar lib
```bash
$ reflect jar "/abs/path/to/lib.jar"

# example:
$ reflect jar "/home/owpk/Downloads/spring-boot-2.5.0.jar"
org.springframework.boot.SpringApplicationExtensionsKt
org.springframework.boot.system.SystemProperties
org.springframework.boot.context.config.UnsupportedConfigDataLocationException
...
# prints a list of jar classes and puts the jar path in the
# to search classes in it too

$ reflect UnsupportedConfigDataLocationException
org.springframework.boot.context.config.UnsupportedConfigDataLocationException
        extends org.springframework.boot.context.config.ConfigDataException
public getLocation()
public printStackTrace()
public printStackTrace(java.io.PrintWriter arg0)
public printStackTrace(java.io.PrintStream arg0)
public synchronized fillInStackTrace()
public synchronized getCause()
public synchronized initCause(java.lang.Throwable arg0)
... # prints all methods
```
```bash
$ reflect InputStreamReader
java.io.InputStreamReader
        extends java.io.Reader
public read()
public read(char[] arg0, int arg1, int arg2)
public close()
public getEncoding()
public ready()
...

# also simple regex available (ingore case enabled by default)
$ reflect *inputstream
$ reflect inputstream*
```
