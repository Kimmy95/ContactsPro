package rivnam.akash.contactspro;

/**
 * Created by Akash on 03-03-2019.
 */

public class FavoritePojo {

    private String Name;
    private String Number;
    private String Type;

    public FavoritePojo(){}

    public FavoritePojo(String name, String number, String type)
    {
        this.Name=name;
        this.Number=number;
        this.Type=type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

}
