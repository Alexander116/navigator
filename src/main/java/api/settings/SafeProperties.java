package api.settings;

import java.util.Properties;

/**
 * обертка над классом Properties для безопасного извлечения данных
 */
public class SafeProperties{

    private Properties prop;

    public SafeProperties(Properties prop) {
        this.prop = prop;
    }

    public int getInt(String key, int def){
        try{
            String res = (String) prop.get(key);
            return Integer.parseInt(res);
        }catch(Exception e){
            return def;
        }
    }

    public double getDouble(String key, double def){
        try{
            String res = (String) prop.get(key);
            return Double.parseDouble(res);
        }catch(Exception e){
            return def;
        }
    }

    public String getString(String key, String def){
        String res = (String) prop.getOrDefault(key, def);
        return res.isEmpty() ? def : res;
    }

    public void put(Object key, Object val){
        prop.put(key, val);
    }
}
