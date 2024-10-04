


public class ImparesRunnable implements Runnable {
    private final Object lock;
    private static int numeroActual = -1;

    public ImparesRunnable(Object lock) {
        this.lock = lock; // Usar el mismo lock que pasa Main
    }

    @Override
    public void run() {
        while (numeroActual < 99) {
            synchronized (lock) {
                for (int i = 0; i < 5 && numeroActual < 99; i++) {
                    numeroActual += 2;
                    System.out.println("Impar: " + numeroActual);
                }
                lock.notify(); // Notificar al otro hilo
                try {
                    lock.wait(); // Esperar al otro hilo
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        synchronized (lock) {
            lock.notify(); // Notificar al otro hilo por si está esperando
        }
    }
}
