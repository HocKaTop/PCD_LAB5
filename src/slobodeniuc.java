import java.util.ArrayList;
import java.util.concurrent.Callable;

class Store {
    ArrayList<Integer> stockList=new ArrayList<Integer>();

    public synchronized void get(String str) {
        while (stockList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        System.out.println(str + " взял со склада: "+ stockList.getLast());
        stockList.remove(stockList.size()-1);

        if(!stockList.isEmpty()){
            System.out.print("На складе имеется " + stockList.size()+" единиц -> ");
            for(int odd : stockList){
                System.out.print(odd+ " ");
            }
            System.out.println(" ");}
        else{
            System.out.println("Склад пуст");
        }
        notifyAll();
    }

    public synchronized void put(String str,int a, int b) {
        while (stockList.size()>=5) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        System.out.print(str + " поместил в хранилище два числа: ");
        stockList.add(a);
        System.out.print(stockList.get(stockList.size()-1)+", ");
        stockList.add(b);
        System.out.println(stockList.get(stockList.size()-1));
        if(stockList.size()!=0){
            System.out.print("На складе имеется " + stockList.size()+" единиц -> ");
            for(int odd : stockList){
                System.out.print(odd+ " ");
            }
            System.out.println(" ");}
        else{
            System.out.println("Склад пуст");
        }

        notifyAll();
    }
}

class Producer implements Callable<String> {

    Store s;

    public Producer(Store s) {
        this.s = s;
    }
    @Override
    public String call() {
        int[] odd = new int[]{1,3,5,7,9,11,13,15,17,19};
        while(true){
        s.put(Thread.currentThread().getName(), odd[(int)(Math.random()*9)], odd[(int)(Math.random()*9)]);
        }
    }
}

class Consumer implements Callable<String>{

    Store s;
    public Consumer(Store s) {
        this.s = s;
    }
    @Override
    public String call () {
        int cons = 0;
        for(int i = 0; i < 2; i++){
            s.get(Thread.currentThread().getName());
        }
        return Thread.currentThread().getName() + " Поток завершен";
    }
}
