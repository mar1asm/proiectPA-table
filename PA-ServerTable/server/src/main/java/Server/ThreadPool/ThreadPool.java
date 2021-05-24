package Server.ThreadPool;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPool {
    private ThreadPoolExecutor executor;

    public ThreadPool(int size) {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(size);
    }

    public boolean isTerminating() {
        return executor.isTerminating();
    }

    public void stop() {
        executor.shutdown();
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
