package rivnam.akash.contactspro;

/**
 * Created by Akash on 27-02-2019.
 */

public class LogPojo {

    private String Name;
    private String Number;
    private String Type;
    private String Duration;
    private String Simid;
    private String Calldate;

    public LogPojo(){}

    public LogPojo(String name, String number, String type, String duration, String simid, String calldate)
    {
        this.Name=name;
        this.Number=number;
        this.Type=type;
        this.Duration=duration;
        this.Simid=simid;
        this.Calldate=calldate;
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

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getSimid() {
        return Simid;
    }

    public void setSimid(String simid) {
        Simid = simid;
    }

    public String getCalldate() {
        return Calldate;
    }

    public void setCalldate(String calldate) {
        Calldate = calldate;
    }
}
