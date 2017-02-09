package juego;

public class Juego {
	private Pantalla pantalla;
	private Jugador jugador;
	private Mapa mapa;
	
	public Juego() {
		pantalla = new Pantalla(this);
		jugador = new Jugador(this);		
		mapa = new Mapa(pantalla);
		pantalla.comenzar();
	}
	
	public static void main(String[] args) {
		new Juego();
	}
	
	public Pantalla getPantalla() {
		return pantalla;
	}
	
	public Jugador getJugador() {
		return jugador;
	}
	
	public Mapa getMapa() {
		return mapa;
	}
}
