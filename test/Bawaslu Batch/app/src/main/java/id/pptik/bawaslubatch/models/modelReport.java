package id.pptik.bawaslubatch.models;
public class modelReport {

    private final String name;
    private final String komentar;
    private final String price;
    private final String category;
    private final String imageName;

    public modelReport(String name, String description, String price, String category,
                String imageName) {
        this.name = name;
        this.komentar = description;
        this.price = price;
        this.category = category;
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public String getKomentar() {
        return komentar;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getImageName() {
        return imageName;
    }
}