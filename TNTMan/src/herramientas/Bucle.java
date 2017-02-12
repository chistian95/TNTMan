package herramientas;

public abstract class Bucle extends Thread {
	private long tiempo;
	private boolean parar;
	private boolean pausa;
	
	public Bucle() {
		this(20);
	}
	
	public Bucle(long tiempo) {
		this.tiempo = tiempo;
		start();
	}
	
	public abstract void onBucle() throws InterruptedException;
	
	@Override
	public void run() {
		while(!parar) {
			try {
				while(pausa) {
					Thread.sleep(tiempo);
				}
				onBucle();
				Thread.sleep(tiempo);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void parar() {
		parar = true;
	}
	
	public void pausar() {
		pausa = true;
	}
	
	public void seguir() {
		pausa = false;
	}
}
