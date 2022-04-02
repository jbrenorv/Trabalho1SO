package application;

import java.util.concurrent.Semaphore;

public class Semaforo {
	
	public static Semaphore posVazias = new Semaphore(Deposito.M); // quantidade de posições vazias no depósito
	
	public static Semaphore posCheias = new Semaphore(0); // quantidade de posições preenchidas no depósito
	
	public static Semaphore mutex = new Semaphore(1);
	
}