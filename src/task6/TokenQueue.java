package task6;

public interface TokenQueue {

    void push(String str) throws InterruptedException;

    String poll() throws InterruptedException;
}
