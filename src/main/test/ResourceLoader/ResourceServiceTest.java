package ResourceLoader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class ResourceServiceTest {
    GSResources resource;


    @Before
    public void setUp()
    {
        try
        {
            PrintWriter writer = new PrintWriter("resources/resourceTest.xml", "UTF-8");
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<resourceTest>");
            writer.println("    <test test=\"TEST\"></test>");
            writer.println("</resourceTest>");
            writer.close();
        } catch (java.io.UnsupportedEncodingException | java.io.FileNotFoundException e)
        {
            System.out.println(e);
        }

        resource = ResourcesService.getInstance().getResources("board.xml");
    }

    @After
    public void tearDown()
    {
        try
        {
            Files.delete(FileSystems.getDefault().getPath("resources/resourceTest.xml"));
        }catch (java.io.IOException e)
        {
            System.out.println(e);
        }
        resource = null;
    }


    @Test
    public void testResourceLoading()
    {
        GSResources res = ResourcesService.getInstance().getResources("resourceTest.xml");
        Assert.assertEquals(res.getContentByName("test").get(0).getSetting("test"), "TEST");
    }

    @Test
    public void testAdminInfo() {
        Assert.assertEquals(Integer.valueOf(resource.getContentByName("field_count").get(0).getSetting("count")),
                Integer.valueOf(44));
    }
}
