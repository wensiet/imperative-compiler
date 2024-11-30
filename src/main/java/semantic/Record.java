package semantic;

import java.util.Set;

public class Record {
    public String name;
    public Set<String> fields;

    public Record(String recordName, Set<String> fieldNames) {
        this.name = recordName;
        this.fields = fieldNames;
    }
}
