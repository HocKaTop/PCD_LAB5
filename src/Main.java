import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int choice;

        System.out.println("Выберите вариант: 1) Слободенюк 2) Кравченко");
        Scanner sc = new Scanner(System.in);
        choice=sc.nextInt();
        switch (choice) {
            case 1:
                Store store = new Store();
                Semaphore semaphore = new Semaphore(2);
                ExecutorService producerExecutor = Executors.newFixedThreadPool(3);
                ExecutorService consumerExecutor = Executors.newFixedThreadPool(4);


                producerExecutor.submit(new Producer(semaphore, store));
                producerExecutor.submit(new Producer(semaphore, store));
                producerExecutor.submit(new Producer(semaphore, store));
                Future<String> c1 = consumerExecutor.submit(new Consumer(semaphore, store));
                Future<String> c2 = consumerExecutor.submit(new Consumer(semaphore, store));
                Future<String> c3 = consumerExecutor.submit(new Consumer(semaphore, store));
                Future<String> c4 = consumerExecutor.submit(new Consumer(semaphore, store));
                consumerExecutor.shutdown();
                producerExecutor.shutdown();
                break;
                case 2:
                    CravcencoStore s = new CravcencoStore();
                    Semaphore sem=new Semaphore(2);
                    ExecutorService cproducerExecutor = Executors.newFixedThreadPool(4 );
                    ExecutorService cconsumerExecutor = Executors.newFixedThreadPool(3);
                    cproducerExecutor.submit(new CravcencoProducer(s,sem));
                    Future<String> cc1 = cconsumerExecutor.submit(new CravcencoConsumer(s,sem));
                    cproducerExecutor.submit(new CravcencoProducer(s,sem));
                    Future<String> cc2 = cconsumerExecutor.submit(new CravcencoConsumer(s,sem));
                    cproducerExecutor.submit(new CravcencoProducer(s,sem));
                    cproducerExecutor.submit(new CravcencoProducer(s,sem));
                    Future<String> cc3 = cconsumerExecutor.submit(new CravcencoConsumer(s,sem));

                    cconsumerExecutor.shutdown();
                    cproducerExecutor.shutdown();
                    break;
        }

    }
}
