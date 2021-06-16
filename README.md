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
java.io.InputStreamReader
        extends java.io.Reader
public read()
public read(char[] arg0, int arg1, int arg2)
public close()
...

# also simple regex available (ignore case enabled by default)
$ reflect %input
# or
$ reflect input%
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

$ reflect UnsupportedConfigDataLocationException
org.springframework.boot.context.config.UnsupportedConfigDataLocationException
        extends org.springframework.boot.context.config.ConfigDataException
public getLocation()
public printStackTrace()
public printStackTrace(java.io.PrintWriter arg0)
... # prints all methods
```
