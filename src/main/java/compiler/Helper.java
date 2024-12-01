package compiler;

public class Helper {
    public Integer idx;
    public String type;

    public Helper(Integer idx, String type) {
        this.idx = idx;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Helper{" + "idx=" + idx + ", type='" + type + '\'' + '}';
    }
}
