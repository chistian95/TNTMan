package herramientas;

public abstract class Bucle extends Thread {
	private long tiempo;
	private boolean parar;
	
	public Bucle() {
		this(20);
	}
	
	public Bucle(long tiempo) {
		this.tiempo = tiempo;
		start();
	}
	
	public abstract void onBucle();
	
	@Override
	public void run() {
		while(!parar) {
			try {
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
}
