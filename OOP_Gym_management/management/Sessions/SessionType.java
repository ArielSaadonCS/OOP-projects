package gym.management.Sessions;

public enum SessionType {
    Pilates(30,60),
    MachinePilates(10,80),
    ThaiBoxing(20,100),
    Ninja(5,150);
    private final int capacity;
    private final int price;

    SessionType(int capacity, int price) {
    this.capacity = capacity;
    this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }
    public int getPrice() {
        return price;
    }
}
