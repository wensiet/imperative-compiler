package compiler;

import java.util.HashMap;
import java.util.Map;

public class UserDefinedType {
    public String name;
    public Map<String, String> fields;

    public UserDefinedType(String name) {
        this.name = name;
        this.fields = new HashMap<>();
    }
}
