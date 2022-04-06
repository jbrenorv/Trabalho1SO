package application;

import java.util.concurrent.Semaphore;

public class Semaforo {
	
	// quantidade de posições vazias no depósito
	public static Semaphore posVazias = null;
	
	// quantidade de posições preenchidas no depósito
	public static Semaphore posCheias = new Semaphore(0);
	
	public static Semaphore mutex = new Semaphore(1);
	
	public static void setPosVaziasSemaphore(int m) {
		posVazias = new Semaphore(m);
	}
	
}
