package pt.ulusofona.lp2.greatprogrammingjourney;

public class Event {
    String type;
    String subtype;
    String position;

    public Event(String type, String subtype, String position) {
        this.type = type;
        this.subtype = subtype;
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getName() {
        return position;
    }

    public boolean isAbyss(){
        if (type.equals("0")){
            return true;
        }
        return false;
    }

    public boolean isTool() {
        if(type.equals("1")){
            return true;
        }
        return false;
    }
}
