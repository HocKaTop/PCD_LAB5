import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

class Store {
    ArrayList<Integer> stockList=new ArrayList<Integer>();

    public synchronized void get(String str) {

        if (stockList.isEmpty()) {
            System.out.println(str + " хотел взять, но склад пуст!");
            return;
        }

        System.out.println(str + " взял со склада: " + stockList.getLast());
        stockList.remove(stockList.size() - 1);

        if (!stockList.isEmpty()) {
            System.out.print("На складе имеется " + stockList.size() + " единиц -> ");
            for (int odd : stockList) {
                System.out.print(odd + " ");
            }
            System.out.println(" ");
        } else {
            System.out.println("Склад пуст");
        }
    }

    public synchronized void put(String str, int a, int b) {

        if (stockList.size() >= 5) {
            System.out.println(str + " хотел положить, но склад переполнен!");
            return;
        }

        System.out.print(str + " поместил в хранилище два числа: ");
        stockList.add(a);
        System.out.print(stockList.get(stockList.size() - 1) + ", ");
        stockList.add(b);
        System.out.println(stockList.get(stockList.size() - 1));

        System.out.print("На складе имеется " + stockList.size() + " единиц -> ");
        for (int odd : stockList) {
            System.out.print(odd + " ");
        }
        System.out.println(" ");
    }
}

class Producer implements Callable<String> {
    Semaphore sem;
    Store s;

    public Producer(Semaphore sem, Store s) {
        this.sem= sem;this.s = s;
    }
    @Override
    public String call() {
        int[] odd = new int[]{1,3,5,7,9,11,13,15,17,19};
        try{

            for (int i = 0; i <3 ; i++) {


                sem.acquire();
                s.put(Thread.currentThread().getName(), odd[(int)(Math.random()*9)], odd[(int)(Math.random()*9)]);
                sem.release();
            }
        }catch(Exception e){}
        return Thread.currentThread().getName() + " Поток завершен";
    }
}

class Consumer implements Callable<String>{
    Semaphore sem;
    Store s;
    public Consumer(Semaphore sem, Store s) {
        this.sem=sem;this.s = s;
    }
    @Override
    public String call () {
        int cons = 0;
        try {
            for (int i = 0; i < 2; i++) {
                sem.acquire();
                s.get(Thread.currentThread().getName());
                sem.release();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Thread.currentThread().getName() + " Поток завершен";
    }
}
