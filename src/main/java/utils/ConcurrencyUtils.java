package utils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class ConcurrencyUtils {
    private ConcurrencyUtils() {}

    public static void waitAll(List<? extends Future<?>> futures) throws IOException {
        try {
            for (Future<?> future : futures) {
                future.get();
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            rethrow(e.getCause());
        }
    }

    public static void rethrow(Throwable cause) throws IOException {
        if (cause instanceof IOException io){
            throw io;
        }

        throw new RuntimeException(cause);
    }
}
