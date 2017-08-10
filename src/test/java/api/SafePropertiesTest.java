package api;

import api.settings.SafeProperties;
import junit.framework.TestCase;

import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * тестироване SafeProperties
 */
public class SafePropertiesTest extends TestCase {

    private static final String KEY = "some_key";
    private Properties prop = mock(Properties.class);
    private SafeProperties safeProp = new SafeProperties(prop);

    public void testGetString(){
        when(prop.getOrDefault(KEY,"def")).thenReturn("");
        String res = safeProp.getString(KEY,"def");
        assertEquals(res, "def");
    }

    public void testGetInt(){
        when(prop.get(KEY)).thenReturn("2");
        int res = safeProp.getInt(KEY,2);
        assertEquals(res, 2);
    }

    public void testGetDouble(){
        when(prop.get(KEY)).thenReturn("2.1");
        Double res = safeProp.getDouble(KEY,2.1);
        assertEquals(res, 2.1);
    }
}
