package task6;

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TokenRingImpl {

    private  int size;
    private  List<TokenQueue> tokenQueueList;
    private  List<Node> nodes;
    private  List<Thread> nodeRunnableList;
    public static AtomicInteger counter = new AtomicInteger(0);
    public static AtomicInteger counter2 = new AtomicInteger(0);



    public TokenRingImpl(int size, List<TokenQueue> tokenQueueList, List<Node> nodes, List<Thread> nodeRunnableList) {
        this.size = size;
        this.nodeRunnableList = nodeRunnableList;
        this.nodes = nodes;
        this.tokenQueueList = tokenQueueList;
    }

    public static TokenRingImpl factory(int nodeCount, int tokenCount) {
        List<TokenQueue> tokenQueueList = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            TokenQueueImpl tokenQueue = new TokenQueueImpl();
            if (i < tokenCount){
                tokenQueue.setStr(String.valueOf(i));
            }
            tokenQueueList.add(tokenQueue);
        }

        Consumer<Integer> tokenConsumer = token -> {};

        List<Node> nodes = tokenQueueList.stream()
                .map(it -> new NodeImpl(it, tokenConsumer, counter2.getAndIncrement()))
                .collect(Collectors.toList());

        List<Thread> nodeRunnableList = nodes.stream()
                .map(it -> {
                    int prevId = it.getId() == 0 ? nodeCount - 1 : it.getId() - 1;
                    return new NodeRunnable(it, tokenQueueList.get(prevId));
                }).map(it -> {
                    Thread thread = new Thread(it);
                    thread.setName(String.valueOf(thread.getId() % nodeCount));
                    return thread;
                })
                .collect(Collectors.toList());

        return new TokenRingImpl(nodeCount,tokenQueueList,nodes,nodeRunnableList);
    }


    public void start() {
        nodeRunnableList.forEach(Thread::start);
    }

    public void stop() {
        nodeRunnableList
                .forEach(thread -> thread.setName("stop"));
    }


    public static void benchmark1() throws InterruptedException {
        TokenRingImpl tokenRing = TokenRingImpl.factory(50,40);
        tokenRing.start();
        Thread.sleep(1000);
        tokenRing.stop();
        System.out.println(counter);
    }

    public static void benchmark2() throws InterruptedException {
        TokenRingImpl tokenRing = TokenRingImpl.factory(50,40);
        tokenRing.start();
        while (counter.get() < 50){}
        tokenRing.stop();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        for (int i = 0; i < 10; i++) {
//            benchmark1();
//            counter2.set(0);
//            counter.set(0);
//        }
//
//        benchmark2();
    }
}
