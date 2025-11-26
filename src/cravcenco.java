import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

class CravcencoStore {
    ArrayList<String> stockList=new ArrayList<String>();

    public synchronized void get(String str) {
        if (stockList.isEmpty()) {
            System.out.println("Попытался взять но у него нет ручеек");
            return;
        }

        String item = stockList.getLast();
        stockList.remove(stockList.size() - 1);

        String vowels = item.replaceAll("(?i)[^aeiouаеёиоуыэюя]", "");

        System.out.println(str + " взял со склада строку: " + item);
        System.out.println(str + " извлёк гласные: " + vowels);

        if (!stockList.isEmpty()) {
            System.out.print("На складе имеется " + stockList.size() + " единиц -> ");
            for (String s : stockList) System.out.print(s + " ");
            System.out.println();
        } else {
            System.out.println("Склад пуст");
        }

    }

    public synchronized void put(String str,String a, String b) {
        if (stockList.size()>=10){
            System.out.println("Бусик ТЦК полон");
            return;
        }
        System.out.print(str + " поместил в хранилище две строки: ");
        stockList.add(a);
        System.out.print(stockList.get(stockList.size()-1)+", ");
        stockList.add(b);
        System.out.println(stockList.get(stockList.size()-1));
        if(!stockList.isEmpty()){
            System.out.print("На складе имеется " + stockList.size()+" единиц -> ");
            for(String letters : stockList){
                System.out.print(letters+ " ");
            }
            System.out.println(" ");}
        else{
            System.out.println("Склад пуст");
        }
    }
}

class CravcencoProducer implements Callable<String> {
    Semaphore sem;
    CravcencoStore s;

    public CravcencoProducer(CravcencoStore s, Semaphore sem) {
        this.s = s;
        this.sem=sem;
    }
    @Override
    public String call() {
        String[] words = {"aboba", "programare", "java", "battlefield", "playstation"};
        try {
            sem.acquire();
        for (int i = 0; i < 2; i++) {

            String a = words[(int) (Math.random() * words.length)];
            String b = words[(int) (Math.random() * words.length)];
            s.put(Thread.currentThread().getName(), a, b);

        }
            sem.release();
        }catch (InterruptedException e){}
        return "";
    }
}

class CravcencoConsumer implements Callable<String> {
    Semaphore sem;
    CravcencoStore s;
    public CravcencoConsumer(CravcencoStore s, Semaphore sem) {
        this.s = s;
        this.sem=sem;
    }
    @Override
    public String call() {
        int cons = 0;
        try {
            sem.acquire();
            for (int i = 0; i < 3; i++) {

                s.get(Thread.currentThread().getName());

            }
            sem.release();
        } catch (InterruptedException e){}
        return Thread.currentThread().getName() + " Поток завершен";
    }
}