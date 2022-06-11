import com.github.mrmks.mc.marcabone.utils.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class TestUtils {

    //@Test
    public void testStringReplaceB() {
        String src = "name is: <asbname>, please call me <name>, <asd>";
        String tar = "name is: <asbname>, please call me Mirora_Mikasa, <asd>";
        String tar1 = StringUtils.replaceTag(src, "name", "Mirora_Mikasa");
        Assert.assertEquals(tar, tar1);
    }

    //@Test
    public void testStringReplace() {
        String src = "name is: <name>, please call me <name>, <asd>";
        String tar = "name is: Mirora_Mikasa, please call me Mirora_Mikasa, 1234567890";
        String tar1 = null, tar2 = null, tar3 = null;
        int times = 500000;

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            tar1 = StringUtils.replacer(src).replaceTag("name", "Mirora_Mikasa").replaceTag("asd", "1234567890").toString();
        }

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", "Mirora_Mikasa");
            map.put("asd", "1234567890");
            tar3 = StringUtils.replaceTag(src, map);
        }

        long t2 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            tar2 = src.replace("<name>", "Mirora_Mikasa").replace("<asd>", "1234567890");
        }

        long t3 = System.currentTimeMillis();
        Assert.assertEquals(tar, tar1);
        Assert.assertEquals(tar, tar2);
        Assert.assertEquals(tar, tar3);
        System.out.printf("StringUtils: %d\nStringReplace: %d\nString.replace: %d", t1-t0, t2-t1, t3-t2);
    }

    //@Test
    public void testSplit() {
        String srcA = "";
        String[] res = StringUtils.split(srcA, ';');
        System.out.println(Arrays.toString(res) + res.length);

        List<String> lst = new ArrayList<>();
        lst.add("avd");
        lst.add("fdjs");
        System.out.println(StringUtils.append(lst, ';'));
    }

    @Test
    public void testReplace() {
        String src = "name is: <name>, please call me <name>, <asd>23456789,<asd>0";
        String tar = "name is: Mirora_Mikasa, please call me Mirora_Mikasa, 123456789,10";
        Assert.assertEquals(tar, StringUtils.replace(src, new String[]{"<name>", "Mirora_Mikasa", "<asd>", "1"}));
    }
}
