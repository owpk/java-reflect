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
org.springframework.boot.system.JavaVersion
... # prints jar's class list and puts jar path to cache to search classes in it too

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
public toString()
public getMessage()
public final synchronized getSuppressed()
public getLocalizedMessage()
public getStackTrace()
public setStackTrace(java.lang.StackTraceElement[] arg0)
public final synchronized addSuppressed(java.lang.Throwable arg0)
public final native wait(long arg0)
public final wait(long arg0, int arg1)
public final wait()
public equals(java.lang.Object arg0)

@jdk.internal.HotSpotIntrinsicCandidate()
public native hashCode()

@jdk.internal.HotSpotIntrinsicCandidate()
public final native getClass()

@jdk.internal.HotSpotIntrinsicCandidate()
public final native notify()

@jdk.internal.HotSpotIntrinsicCandidate()
public final native notifyAll()
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
public read(char[] arg0)
public read(java.nio.CharBuffer arg0)
public mark(int arg0)
public transferTo(java.io.Writer arg0)
public skip(long arg0)
public markSupported()
public reset()
public static nullReader()
public final native wait(long arg0)
public final wait(long arg0, int arg1)
public final wait()
public equals(java.lang.Object arg0)
public toString()

@jdk.internal.HotSpotIntrinsicCandidate()
public native hashCode()

@jdk.internal.HotSpotIntrinsicCandidate()
public final native getClass()

@jdk.internal.HotSpotIntrinsicCandidate()
public final native notify()

@jdk.internal.HotSpotIntrinsicCandidate()
public final native notifyAll()

# Prints all available methods in a specific class
```
