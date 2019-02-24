package rivnam.akash.contactspro;

/**
 * Created by Akash on 21-02-2019.
 */

public class PhoneBookPojo {

    private String Name;
    private String Number;
    private String Email;
    private String Type;
    private String Status;
    private String Address;

    public PhoneBookPojo(){}

    public PhoneBookPojo(String name, String number, String email, String type, String status, String address)
    {
        this.Name=name;
        this.Number=number;
        this.Email=email;
        this.Type=type;
        this.Status=status;
        this.Address=address;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
