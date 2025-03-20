package package1;

class DeliveryPoint {
    String name;
    String address;
    int x, y;

    public DeliveryPoint(String name, String address, int x, int y) {
        this.name = name;
        this.address = address;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return name + " (" + address + ") - [" + x + ", " + y + "]";
    }
}