package juego;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import herramientas.Pintable;

public class Mapa implements Pintable {
	public static final int ANCHO = 60;
	public static final int ALTO = 60;
	public static final int PROB_CAJA = 45;
	public static final int PROB_ITEM = 10;
	
	private int[][] mapa;
	private int[][] items;
	private BufferedImage grass;
	private BufferedImage wall;	
	private BufferedImage crate;
	
	public Mapa(Pantalla pantalla) {
		new Thread() {
			@Override
			public void run() {
				try {
					grass = ImageIO.read(getClass().getClassLoader().getResource("grass.png"));
					wall = ImageIO.read(getClass().getClassLoader().getResource("wall.png"));
					crate = ImageIO.read(getClass().getClassLoader().getResource("crate.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		generarMapa();
	}
	
	public boolean colision(Jugador jugador) {
		int xJugador = jugador.getX();
		int yJugador = jugador.getY();
		Rectangle recJugador = new Rectangle(xJugador, yJugador, Jugador.ANCHO + Jugador.ANCHO / 2, Jugador.ALTO + Jugador.ALTO / 2);
		
		for(int y = 0; y < mapa[0].length; y++) {
			for(int x = 0; x < mapa.length; x++) {
				if(mapa[x][y] == 1 && items[x][y] == 0) {
					continue;
				}
				Rectangle recCasilla = new Rectangle(x * ANCHO, y * ALTO, ANCHO, ALTO);
				if(recCasilla.intersects(recJugador)) {
					if(mapa[x][y] == 1) {
						if(items[x][y] != 0 && recCasilla.contains(recJugador.getX() + recJugador.getWidth() / 2, recJugador.getY() + recJugador.getHeight() / 2)) {
							if(items[x][y] == 1) {
								jugador.darBomba();
							} else if(items[x][y] == 2) {
								jugador.aumentarRango();
							}
							items[x][y] = 0;
						}
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void romperCaja(int x, int y) {
		mapa[x][y] = 1;
		double rnd = Math.random() * 100;
		if(rnd <= PROB_ITEM) {
			rnd = Math.random() * 100;
			items[x][y] = rnd <= 33 ? 1 : 2;
		}
	}
	
	private void generarMapa() {
		mapa = new int[15][13];
		items = new int[15][13];
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
					double rnd = Math.random() * 100;
					if(rnd <= PROB_CAJA) {
						mapa[x][y] = 2;
					}
				}
				mapa[1][1] = 1;
				mapa[1][2] = 1;
				mapa[2][1] = 1;
				
				mapa[mapa.length - 2][mapa[0].length - 2] = 1;
				mapa[mapa.length - 3][mapa[0].length - 2] = 1;
				mapa[mapa.length - 2][mapa[0].length - 3] = 1;
			}
		}
	}

	@Override
	public void pintar(Graphics2D g) {
		for(int y = 0; y < mapa[0].length; y++) {
			for(int x = 0; x < mapa.length; x++) {
				if(mapa[x][y] == 0) {
					g.drawImage(wall, (x * ANCHO), (y * ALTO), ANCHO, ALTO, null);
				} else if(mapa[x][y] == 1) {
					g.drawImage(grass, (x * ANCHO), (y * ALTO), ANCHO, ALTO, null);
				} else if(mapa[x][y] == 2) {
					g.drawImage(crate, (x * ANCHO), (y * ALTO), ANCHO, ALTO, null);
				}
				if(items[x][y] != 0) {
					g.setColor(Color.RED);
					if(items[x][y] == 2) {
						g.setColor(Color.GREEN);
					}
					g.fillRect((x * ANCHO), (y * ALTO), ANCHO, ALTO);
				}
			}
		}
	}
	
	public int[][] getMapa() {
		return mapa;
	}
}

