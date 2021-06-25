### Intstall
```bash
# run from project root
$ ./install.sh

# uninstall
$ ./intall.sh --unistall
```
### Usage
```bash
$ jreflect ls
java.nio.Bits
java.io.BufferedInputStream
java.io.BufferedOutputStream
...
# Prints all default jre classes
```
```bash
$ jreflect InputStreamReader
InputStreamReader
        extends Reader
public read(CharBuffer)
public read(char[], int, int)
public close()
...

# also simple regex available (ignore case enabled by default)
$ reflect %input
# or
$ reflect input%
```
- verbose format
```bash
$ jreflect inputstreamrea% -v
java.io.InputStreamReader
        extends java.io.Reader
public read(java.nio.CharBuffer)
public read(char[], int, int)
public close()
```
- add jar lib
```bash
$ jreflect jar "/abs/path/to/lib.jar"

# example:
$ jreflect jar "/home/owpk/Downloads/spring-boot-2.5.0.jar"
org.springframework.boot.SpringApplicationExtensionsKt
org.springframework.boot.system.SystemProperties
...
# prints a list of jar classes and puts the jar path to cache
# to search classes in that jar too

$ jreflect unsupportedconfig%
org.springframework.boot.context.config.UnsupportedConfigDataLocationException
        extends org.springframework.boot.context.config.ConfigDataException
public getLocation()
public printStackTrace()
public printStackTrace(java.io.PrintWriter arg0)
... # prints all methods
```
