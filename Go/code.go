package main

import (
	"fmt"
	"sync"
)

var numeroActualImpar = -1
var numeroActualPar = 0

func imprimirImpares(lock *sync.Mutex, cond *sync.Cond, wg *sync.WaitGroup) {
	defer wg.Done()

	for numeroActualImpar < 99 {
		lock.Lock()
		for i := 0; i < 5 && numeroActualImpar < 99; i++ {
			numeroActualImpar += 2
			fmt.Println("Impar:", numeroActualImpar)
		}
		cond.Signal() // Notificar al otro hilo
		cond.Wait()   // Esperar al otro hilo
		lock.Unlock()
	}

	lock.Lock()
	cond.Signal() // Notificar al otro hilo por si está esperando
	lock.Unlock()
}

func imprimirPares(lock *sync.Mutex, cond *sync.Cond, wg *sync.WaitGroup) {
	defer wg.Done()

	lock.Lock()
	cond.Wait() // El hilo de pares espera a que el de impares comience
	lock.Unlock()

	for numeroActualPar < 99 {
		lock.Lock()
		for i := 0; i < 5 && numeroActualPar < 99; i++ {
			numeroActualPar += 2
			fmt.Println("Par:", numeroActualPar)
		}
		cond.Signal() // Notificar al otro hilo
		cond.Wait()   // Esperar al otro hilo
		lock.Unlock()
	}

	lock.Lock()
	cond.Signal() // Notificar al otro hilo por si está esperando
	lock.Unlock()
}

func main() {
	var lock sync.Mutex
	cond := sync.NewCond(&lock)
	var wg sync.WaitGroup

	wg.Add(2)

	go imprimirPares(&lock, cond, &wg)
	go imprimirImpares(&lock, cond, &wg)

	wg.Wait()
}