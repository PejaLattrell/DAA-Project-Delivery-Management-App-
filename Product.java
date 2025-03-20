package package1;

class Product {
    String name;
    double weight;
    int value;

    public Product(String name, double weight, int value) {
        this.name = name;
        this.weight = weight;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " (Weight: " + weight + ", Value: " + value + ")";
    }
}