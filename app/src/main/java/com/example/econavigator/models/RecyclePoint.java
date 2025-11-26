package kg.econavigator.models;

public class RecyclePoint {
    private int id;
    private String name;
    private String type; // plastic, paper, glass
    private double latitude;
    private double longitude;
    private String address;
    private String description;

    public RecyclePoint() {}

    public RecyclePoint(int id, String name, String type, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTypeIcon() {
        switch (type) {
            case "plastic": return "🗑️";
            case "paper": return "📄";
            case "glass": return "🍶";
            default: return "♻️";
        }
    }
}