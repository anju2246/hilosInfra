

public class MainNumerosIntercalados {
    public static void main(String[] args) {
        Object lock = new Object(); // Crear un solo objeto de bloqueo

        Thread hiloPares = new Thread(new ParesRunnable(lock));
        Thread hiloImpares = new Thread(new ImparesRunnable(lock));

        hiloPares.start();
        hiloImpares.start();

        try {
            hiloPares.join();
            hiloImpares.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// semaforo
// pool de hilos y tipos (caché)