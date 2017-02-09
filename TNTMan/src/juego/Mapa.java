package juego;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import herramientas.Pintable;

public class Mapa implements Pintable {
	public static final int ANCHO = 60;
	public static final int ALTO = 60;
	
	private int[][] mapa;
	private BufferedImage grass;
	private BufferedImage wall;	
	
	public Mapa(Pantalla pantalla) {
		new Thread() {
			@Override
			public void run() {
				try {
					grass = ImageIO.read(getClass().getClassLoader().getResource("grass.png"));
					wall = ImageIO.read(getClass().getClassLoader().getResource("wall.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		generarMapa();
	}
	
	public boolean colision(int xJugador, int yJugador) {
		Rectangle recJugador = new Rectangle(xJugador, yJugador, Jugador.ANCHO + Jugador.ANCHO / 2, Jugador.ALTO + Jugador.ALTO / 2);
		
		for(int y = 0; y < mapa[0].length; y++) {
			for(int x = 0; x < mapa.length; x++) {
				if(mapa[x][y] != 0) {
					continue;
				}
				Rectangle recCasilla = new Rectangle(x * ANCHO, y * ALTO, ANCHO, ALTO);
				if(recCasilla.intersects(recJugador)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void generarMapa() {
		mapa = new int[15][13];
		for(int y = 0; y < mapa[0].length; y++) {
			for(int x = 0; x < mapa.length; x++) {
				if(y == 0 || y == mapa[0].length - 1) {
					mapa[x][y] = 0;
				} else if(x == 0 || x == mapa.length - 1) {
					mapa[x][y] = 0;
				} else if(y % 2 == 0 && x % 2 == 0) {
					mapa[x][y] = 0;
				} else {
					mapa[x][y] = 1;
				}
			}
		}
	}

	@Override
	public void pintar(Graphics2D g) {
		for(int y = 0; y < mapa[0].length; y++) {
			for(int x = 0; x < mapa.length; x++) {
				if(mapa[x][y] == 0) {
					g.drawImage(wall, (x * ANCHO), (y * ALTO), ANCHO, ALTO, null);
				} else {
					g.drawImage(grass, (x * ANCHO), (y * ALTO), ANCHO, ALTO, null);
				}
			}
		}
	}
	
	public int[][] getMapa() {
		return mapa;
	}
}
