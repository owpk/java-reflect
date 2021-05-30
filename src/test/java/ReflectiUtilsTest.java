import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import owpk.util.ReflectUtils;

import java.util.ArrayList;
import java.util.List;

public class ReflectiUtilsTest {
    private List<String> list;

    @Before
    public void setup() {
        list = new ArrayList<>();
        list.add("A");
    }

    @Test
    public void printMethods() throws Exception {
        Class<?> cl = list.getClass();
        var list = ReflectUtils.getMethodsNames(cl);
        list.forEach(System.out::println);
        Assert.assertTrue(list.size() > 0);
    }
}
