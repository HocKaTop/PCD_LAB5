import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {

        Store store = new Store();

        ThreadFactory daemonFactory = r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("producer-daemon-" + t.getId());
            return t;
        };

        ExecutorService producerExecutor = Executors.newFixedThreadPool(3, daemonFactory);

        ExecutorService consumerExecutor = Executors.newFixedThreadPool(4);

        Future<String> c1 = consumerExecutor.submit(new Consumer(store));
        Future<String> c2 = consumerExecutor.submit(new Consumer(store));
        Future<String> c3 = consumerExecutor.submit(new Consumer(store));
        Future<String> c4 = consumerExecutor.submit(new Consumer(store));
        producerExecutor.submit(new Producer(store));
        producerExecutor.submit(new Producer(store));
        producerExecutor.submit(new Producer(store));



        consumerExecutor.shutdown();
    }
}
