package Action.models;

public class ActionParam {

    private String name;
    private String type;

    public ActionParam(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
