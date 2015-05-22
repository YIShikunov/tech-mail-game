package ResourceLoader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class ResourceServiceTest {
    GSResources resource;


    @Before
    public void setUp()
    {
        resource = ResourcesService.getInstance().getResources("board.xml");
    }

    @After
    public void tearDown()
    {
        resource = null;
    }

    @Test
    public void testAdminInfo() {
        Assert.assertEquals(Integer.valueOf(resource.getContentByName("field_count").get(0).getSetting("count")),
                Integer.valueOf(44));
    }
}
