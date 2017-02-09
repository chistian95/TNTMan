package juego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import herramientas.Bucle;

public class Pantalla extends JFrame implements KeyListener {
	public static final int WIDTH = 900;
	public static final int HEIGHT = 780;
	
	private static final long serialVersionUID = 1L;
	private BufferedImage bf;
	private Juego juego;

	public Pantalla(Juego juego) {
		this.juego = juego;
		
		setSize(WIDTH, HEIGHT);
		setUndecorated(true);
		setLocationRelativeTo(null);
		
		addKeyListener(this);
		
		bf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	}
	
	public void comenzar() {
		setVisible(true);
		new Bucle() {
			@Override
			public void onBucle() {
				repaint();
			}
		};
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) bf.getGraphics();
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		
		juego.getMapa().pintar(g2d);
		juego.getJugador().pintar(g2d);
		
		g.drawImage(bf, 0, 0, null);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
