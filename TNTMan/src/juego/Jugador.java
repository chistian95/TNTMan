package juego;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import herramientas.Bucle;
import herramientas.Pintable;

public class Jugador implements Pintable, KeyListener {
	public static final int ALTO = 30;
	public static final int ANCHO = 20;
	
	private int x;
	private int y;
	private int pos;
	private int posAnt;
	private int bombas;
	private int rangoBombas;
	private int[][] bombasMapa;
	private List<Integer[]> animaciones;
	private boolean moverDerecha;
	private boolean moverIzquierda;
	private boolean moverArriba;
	private boolean moverAbajo;
	private BufferedImage tileset;
	private Juego juego;
	
	public Jugador(Juego juego) {
		this.juego = juego;
		
		x = Mapa.ANCHO;
		y = Mapa.ALTO;
		pos = -1;
		posAnt = -1;
		bombas = 1;
		rangoBombas = 1;
		bombasMapa = new int[15][13];
		animaciones = new ArrayList<Integer[]>();
		
		new Thread() {
			@Override
			public void run() {
				try {
					tileset = ImageIO.read(getClass().getClassLoader().getResource("bombermanTileset.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		new Bucle() {
			@Override
			public void onBucle() {
				mover();
			}
		};
		
		new Bucle(150) {
			@Override
			public void onBucle() {
				animar();
			}
		};
		
		juego.getPantalla().addKeyListener(this);
	}
	
	private void mover() {
		if(moverArriba) {
			y -= 2;
			if(juego.getMapa().colision(this)) {
				y += 2;
			}
		}
		if(moverAbajo) {
			y += 2;
			if(juego.getMapa().colision(this)) {
				y -= 2;
			}
		}
		if(moverIzquierda) {
			x -= 2;
			if(juego.getMapa().colision(this)) {
				x += 2;
			}
		}
		if(moverDerecha) {
			x += 2;
			if(juego.getMapa().colision(this)) {
				x -= 2;
			}
		}
	}
	
	private void animar() {
		pos = 4;
		if(moverArriba && !moverAbajo) {
			if(posAnt >= 6 && posAnt <= 8) {
				pos = posAnt + 1;
				if(pos > 8) {
					pos = 6;
				}
			} else {
				pos = 6;
			}
		} else if(moverAbajo && !moverArriba) {
			if(posAnt >= 3 && posAnt <= 5) {
				pos = posAnt + 1;
				if(pos > 5) {
					pos = 3;
				}
			} else {
				pos = 3;
			}
		} else if(moverDerecha && !moverIzquierda) {
			if(posAnt >= 9 && posAnt <= 11) {
				pos = posAnt + 1;
				if(pos > 11) {
					pos = 9;
				}
			} else {
				pos = 9;
			}
		} else if(moverIzquierda && !moverDerecha) {
			if(posAnt >= 0 && posAnt <= 2) {
				pos = posAnt + 1;
				if(pos > 2) {
					pos = 0;
				}
			} else {
				pos = 0;
			}
		}
		posAnt = pos;
	}
	
	private void plantarBomba() {
		if(bombas > 0) {
			int tileX = (int) (Math.floor((x + ANCHO / 2) / Mapa.ANCHO));
			int tileY = (int) (Math.floor((y + ALTO / 2) / Mapa.ALTO));
			if(bombasMapa[tileX][tileY] != 0) {
				return;
			}
			bombas--;
			bombasMapa[tileX][tileY] = 1;
			new Bucle(150) {
				@Override
				public void onBucle() throws InterruptedException {
					if(bombasMapa[tileX][tileY] == 1) {
						pausar();
						Thread.sleep(1500);
						seguir();
					}					
					bombasMapa[tileX][tileY] = bombasMapa[tileX][tileY] + 1;
					if(bombasMapa[tileX][tileY] >= 4) {
						bombasMapa[tileX][tileY] = 0;
						animExplosion(tileX, tileY);
						bombas++;
						parar();
					}
				}
			};
		}
	}
	
	private void animExplosion(int x, int y) {
		int[][] mapa = juego.getMapa().getMapa();
		
		List<Integer[]> anim = new ArrayList<Integer[]>();
		anim.add(new Integer[]{x, y, 0, 0});
		
		for(int i = 1; i <= rangoBombas; i++) {
			int px = x - i;
			if(mapa[px][y] == 2) {
				anim.add(new Integer[]{px, y, 1, 0});
				juego.getMapa().romperCaja(px, y);
				break;
			}
			if(px <= 0 || mapa[px][y] == 0) {
				break;
			}
			if(px - 1 <= 0 || mapa[px - 1][y] == 0 || i == rangoBombas) {
				anim.add(new Integer[]{px, y, 1, 0});
				break;
			}
			anim.add(new Integer[]{px, y, 2, 0});
		}
		for(int i = 1; i <= rangoBombas; i++) {
			int px = x + i;
			if(mapa[px][y] == 2) {
				anim.add(new Integer[]{px, y, 4, 0});
				juego.getMapa().romperCaja(px, y);
				break;
			}
			if(px >= mapa.length - 1 || mapa[px][y] == 0) {
				break;
			}
			if(px + 1 >= mapa.length - 1 || mapa[px + 1][y] == 0 || i == rangoBombas) {
				anim.add(new Integer[]{px, y, 4, 0});
				break;
			}
			anim.add(new Integer[]{px, y, 2, 0});
		}
		
		for(int i = 1; i <= rangoBombas; i++) {
			int py = y - i;
			if(mapa[x][py] == 2) {
				anim.add(new Integer[]{x, py, 3, 0});
				juego.getMapa().romperCaja(x, py);
				break;
			}
			if(py <= 0 || mapa[x][py] == 0) {				
				break;
			}
			if(py - 1 <= 0 || mapa[x][py - 1] == 0 || i == rangoBombas) {
				anim.add(new Integer[]{x, py, 3, 0});
				break;
			}
			anim.add(new Integer[]{x, py, 5, 0});
		}
		for(int i = 1; i <= rangoBombas; i++) {
			int py = y + i;
			if(mapa[x][py] == 2) {
				anim.add(new Integer[]{x, py, 6, 0});
				juego.getMapa().romperCaja(x, py);
				break;
			}
			if(py >= mapa[0].length - 1 || mapa[x][py] == 0) {				
				break;
			}
			if(py + 1 >= mapa[0].length - 1 || mapa[x][py] == 0 || i == rangoBombas) {
				anim.add(new Integer[]{x, py, 6, 0});
				break;
			}
			anim.add(new Integer[]{x, py, 5, 0});
		}
		
		animaciones.addAll(anim);
		new Bucle(100) {
			@Override
			public void onBucle() {
				for(Integer[] an : anim) {
					an[3] = an[3] + 1;
					if(an[3] > 4) {
						animaciones.remove(an);
					}
				}
			}
		};
	}

	@Override
	public void pintar(Graphics2D g) {
		g.drawImage(tileset, x, y, (int) (x + ANCHO * 1.5), (int) (y + ALTO * 1.5), (pos * ANCHO), 0, (pos * ANCHO + ANCHO), ALTO, null);
		
		for(int y = 0; y < bombasMapa[0].length; y++) {
			for(int x = 0; x <bombasMapa.length; x++) {
				if(bombasMapa[x][y] != 0) {
					g.drawImage(tileset, (x * Mapa.ANCHO), (y * Mapa.ALTO), (x * Mapa.ANCHO + Mapa.ANCHO), (y * Mapa.ALTO + Mapa.ALTO), 60 + (20 * bombasMapa[x][y]), 100, 80 + (20 * bombasMapa[x][y]), 120, null);
				}
			}
		}
		
		for(int i = 0; i < bombas; i++) {
			g.drawImage(tileset, 10 + 60 * i, Pantalla.HEIGHT - 50, 50 + 60 * i, Pantalla.HEIGHT - 10, 80, 100, 100, 120, null);
		}
		
		try {
			for(Integer[] anim : animaciones) {
				if(anim[2] == 0) {
					g.drawImage(tileset, 60 * anim[0], 60 * anim[1], 60 * anim[0] + 60, 60 * anim[1] + 60, 20 * anim[3] - 20, 60, 20 * anim[3], 80, null);
				} else if(anim[2] == 1) {
					g.drawImage(tileset, 60 * anim[0], 60 * anim[1], 60 * anim[0] + 60, 60 * anim[1] + 60, 20 * anim[3] + 60, 60, 20 * anim[3] + 80, 80, null);
				} else if(anim[2] == 2) {
					g.drawImage(tileset, 60 * anim[0], 60 * anim[1], 60 * anim[0] + 60, 60 * anim[1] + 60, 20 * anim[3] + 140, 60, 20 * anim[3] + 160, 80, null);
				} else if(anim[2] == 3) {
					g.drawImage(tileset, 60 * anim[0], 60 * anim[1], 60 * anim[0] + 60, 60 * anim[1] + 60, 20 * anim[3] - 20, 80, 20 * anim[3], 100, null);
				} else if(anim[2] == 4) {
					g.drawImage(tileset, 60 * anim[0], 60 * anim[1], 60 * anim[0] + 60, 60 * anim[1] + 60, 20 * anim[3] + 60, 80, 20 * anim[3] + 80, 100, null);
				} else if(anim[2] == 5) {
					g.drawImage(tileset, 60 * anim[0], 60 * anim[1], 60 * anim[0] + 60, 60 * anim[1] + 60, 20 * anim[3] + 140, 80, 20 * anim[3] + 160, 100, null);
				} else if(anim[2] == 6) {
					g.drawImage(tileset, 60 * anim[0], 60 * anim[1], 60 * anim[0] + 60, 60 * anim[1] + 60, 20 * anim[3] - 20, 100, 20 * anim[3], 120, null);
				}
			}
		} catch(Exception e) {
			
		}		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			moverArriba = true;
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			moverAbajo = true;
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			moverDerecha = true;
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			moverIzquierda = true;
			break;
		case KeyEvent.VK_SPACE:
			plantarBomba();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			moverArriba = false;
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			moverAbajo = false;
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			moverDerecha = false;
			break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			moverIzquierda = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public void darBomba() {
		bombas++;
	}
	
	public void aumentarRango() {
		rangoBombas++;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
