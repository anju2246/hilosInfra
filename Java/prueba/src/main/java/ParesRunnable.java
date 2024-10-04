


public class ParesRunnable implements Runnable {
    private final Object lock;
    private static int numeroActual = 0;

    public ParesRunnable(Object lock) {
        this.lock = lock; // Usar el mismo lock que pasa Main
    }

    @Override
    public void run() {
        synchronized (lock) {
            try {
                lock.wait(); // El hilo de pares espera a que el de impares comience
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        while (numeroActual < 99) {
            synchronized (lock) {
                for (int i = 0; i < 5 && numeroActual < 99; i++) {
                    numeroActual += 2;
                    System.out.println("Par: " + numeroActual);
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
