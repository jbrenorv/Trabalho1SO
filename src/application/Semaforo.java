package application;

import java.util.concurrent.Semaphore;

public class Semaforo {
	
	public static Semaphore posVazias = new Semaphore(Deposito.M); // quantidade de posi��es vazias no dep�sito
	
	public static Semaphore posCheias = new Semaphore(0); // quantidade de posi��es preenchidas no dep�sito
	
	public static Semaphore mutex = new Semaphore(1);
	
}