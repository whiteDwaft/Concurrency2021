package task6;

public interface Node {
    int getId();

    void receive(String str) throws InterruptedException;
}
