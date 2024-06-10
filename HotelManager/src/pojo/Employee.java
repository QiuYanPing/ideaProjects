package pojo;

public class Employee {
    private String id;
    private String password;
    private String name;
    private String phone;
    private String gender;
    private String post;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", post='" + post + '\'' +
                '}';
    }

    public Employee(String id, String password, String name, String phone, String gender, String post) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.post = post;
    }
}
