package compiler;

import java.util.ArrayList;
import java.util.List;

public class Routine {
    String name;
    String returnType;
    StringBuilder body;
    String signature;
    List<Argument> args;

    public Routine(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;
        this.body = new StringBuilder();
        this.args = new ArrayList<>();
    }
}
