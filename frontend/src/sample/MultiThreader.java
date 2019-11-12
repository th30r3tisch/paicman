package sample;

import java.util.concurrent.TimeUnit;

public class MultiThreader {
    public MultiThreader() {
    }

    int threadCounter = 1;
    final static Object token = new Object();

    public synchronized void startThreads() {
//        createThread(1, 91);
//        createThread(2, 81);
//        createThread(3, 100);
//        createThread(4, 110);
//        createThread(5, 120);
        for(int i  = 1; i < 6; i++ ){
            createThread(i, 121);
        }
    }

    void createThread(int threadNum, int num) {
        new Thread(() -> {
            int counter = 0;
            while (counter < num) {
                if (counter != 0 && counter % 20 == 0) {
                    synchronized (token) {
                        try {
                            if (threadCounter % 5 == 0) {
                                threadCounter = 1;
                                token.notifyAll();
                            } else {
                                System.out.println("waiting with thread " + threadNum);
                                threadCounter++;
                                token.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                counter++;
                System.out.println("thread " + threadNum + ": " + counter);
            }
        }).start();
    }
}