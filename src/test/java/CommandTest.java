public class CommandTest {

//    @Test
//    public void testInfo() {
//        StringWriter sw = new StringWriter();
//        InfoCommand infoCommand = new InfoCommand();
//        CommandLine cmd = new CommandLine(infoCommand);
//        cmd.setOut(new PrintWriter(sw));
//        cmd.execute("-m", "aboba");
//        System.out.println(sw.toString());
//    }
//
//    @Test
//    public void testPrint() {
//        StringWriter sw = new StringWriter();
//        PrintCommand printCommand= new PrintCommand();
//        CommandLine cmd = new CommandLine(printCommand);
//        cmd.setOut(new PrintWriter(sw));
//        cmd.execute("all");
//        cmd.execute("cache");
//        cmd.execute("jar");
//        System.out.println(sw.toString());
//    }
//
//    @Test
//    public void testCache() throws ResourceException {
//        StringWriter sw = new StringWriter();
//        var printCommand= new CacheCommand();
//        CommandLine cmd = new CommandLine(printCommand);
//        cmd.setOut(new PrintWriter(sw));
//        cmd.execute("print");
//        System.out.println(sw.toString());
//    }
//
//    @Test
//    public void testClass() throws ResourceException {
//        var printCommand= new ClassCommand();
//        CommandLine cmd = new CommandLine(printCommand);
//        cmd.execute("java.io.InputStream");
//        cmd.execute("jar", "/home/owpk/Downloads/snmp4j-3.5.1.jar");
//    }
}
