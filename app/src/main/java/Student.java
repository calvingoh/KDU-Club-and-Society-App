public class Student {
    private String uid; //from firebase
    private int id;
    private String name;
    private String email;
    private String[][] clubs;

    public Student(String uid, int id, String name, String email, String[][] clubs) {
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.email = email;
        this.clubs = clubs;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[][] getClubs() {
        return clubs;
    }

    public void setClubs(String[][] clubs) {
        this.clubs = clubs;
    }
}
