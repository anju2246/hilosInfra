import threading

class ImparesRunnable(threading.Thread):
    numero_actual = -1  # Compartido entre los hilos

    def __init__(self, lock):
        threading.Thread.__init__(self)
        self.lock = lock

    def run(self):
        while ImparesRunnable.numero_actual < 99:
            with self.lock:
                for _ in range(5):
                    if ImparesRunnable.numero_actual < 99:
                        ImparesRunnable.numero_actual += 2
                        print(f"Impar: {ImparesRunnable.numero_actual}")
                self.lock.notify()  # Notificar al otro hilo
                self.lock.wait()  # Esperar al otro hilo
        with self.lock:
            self.lock.notify()  # Asegurarse de liberar el hilo en caso de que espere


class ParesRunnable(threading.Thread):
    numero_actual = 0  # Compartido entre los hilos

    def __init__(self, lock):
        threading.Thread.__init__(self)
        self.lock = lock

    def run(self):
        with self.lock:
            self.lock.wait()  # El hilo de pares espera a que el de impares comience
        while ParesRunnable.numero_actual < 99:
            with self.lock:
                for _ in range(5):
                    if ParesRunnable.numero_actual < 99:
                        ParesRunnable.numero_actual += 2
                        print(f"Par: {ParesRunnable.numero_actual}")
                self.lock.notify()  # Notificar al otro hilo
                self.lock.wait()  # Esperar al otro hilo
        with self.lock:
            self.lock.notify()  # Asegurarse de liberar el hilo en caso de que espere


if __name__ == "__main__":
    lock = threading.Condition()  # Crear un objeto de bloqueo

    hilo_pares = ParesRunnable(lock)
    hilo_impares = ImparesRunnable(lock)

    hilo_pares.start()
    hilo_impares.start()

    hilo_pares.join()
    hilo_impares.join()