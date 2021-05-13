package task6;

import java.util.function.Consumer;

public class NodeImpl implements Node {

    private final TokenQueue next;
    private final Consumer<Integer> tokenConsumer;
    private final int id;

    public NodeImpl(TokenQueue next, Consumer<Integer> tokenConsumer, int id) {
        this.tokenConsumer = tokenConsumer;
        this.next = next;
        this.id = id;
    }

    @Override
    public int getId() {return id;}

    @Override
    public void receive(String str) throws InterruptedException {
        int id = getId();
        if(id == Integer.parseInt(str)){
            tokenConsumer.accept(0);
        }
        next.push(str);
        TokenRingImpl.counter.getAndIncrement();
    }
}
