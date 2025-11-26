package kg.econavigator.models;

public class User {
    private int id;
    private String name;
    private String email;
    private String className;
    private String role; // student, teacher, admin

    public User() {}

    public User(int id, String name, String email, String className, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.className = className;
        this.role = role;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}