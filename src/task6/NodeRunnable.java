package task6;

public class NodeRunnable implements Runnable {

    private final Node node;
    private final TokenQueue tokenQueue;

    public NodeRunnable(Node node, TokenQueue tokenQueue) {
        this.node = node;
        this.tokenQueue = tokenQueue;
    }

    @Override
    public void run() {
        try {
            job();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void job() throws InterruptedException {
        while (!Thread.currentThread().getName().equals("stop")) {
            long start = System.nanoTime();
            String string = tokenQueue.poll();
            node.receive(string);
            long finish = System.nanoTime();
            System.out.println(finish - start);
        }
    }
}
