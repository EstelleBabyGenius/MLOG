package mlog;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

/**
 * TODO
 * @author Filip Östermark
 * @version 2013-05-28
 */
public class CustomButton extends JButton {
	private static final long serialVersionUID = -370283324776718331L;

	private boolean buttonPressed;
	private boolean mouseOver;
	private Color currentBackColor;
	private final Color DEFAULT_BACKGROUND_COLOR = Color.DARK_GRAY;
	private final Color ENTERED_BACKGROUND_COLOR = Color.GRAY;
	private final Color DISABLED_BACKGROUND_COLOR = new Color(40, 40, 40);

	/**
	 * TODO
	 * @param label
	 */
	public CustomButton(String label) {
		super(label);
		this.buttonPressed = false;
		this.mouseOver = false;
		this.currentBackColor = DEFAULT_BACKGROUND_COLOR;
		this.setForeground(Color.WHITE);
		this.setFocusPainted(false);
		this.setFocusable(false);
		this.setContentAreaFilled(false);
		this.setOpaque(false);
		this.setMargin(new Insets(5, 0, 5, 0));

		this.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (CustomButton.this.isEnabled()) {
					CustomButton.this.mouseOver = true;
					CustomButton.this.currentBackColor = ENTERED_BACKGROUND_COLOR;
				}
			}

			public void mouseExited(MouseEvent e) {
				CustomButton.this.mouseOver = false;
				CustomButton.this.currentBackColor = DEFAULT_BACKGROUND_COLOR;
			}

			public void mousePressed(MouseEvent e) {
				if (CustomButton.this.isEnabled()) {
					CustomButton.this.buttonPressed = true;
					repaint();
				}
			}

			public void mouseReleased(MouseEvent e) {
				CustomButton.this.buttonPressed = false;
				repaint();
			}
		});
	}

	/**
	 * TODO
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(currentBackColor);
		g.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		if (this.buttonPressed && this.mouseOver) {
			Graphics2D g2d = (Graphics2D)g;
			GradientPaint gradient = new GradientPaint(0, 5, DEFAULT_BACKGROUND_COLOR, 0, this.getHeight() - 5, ENTERED_BACKGROUND_COLOR, true);
			g2d.setPaint(gradient);
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		super.paintComponent(g);
	}

	/**
	 * TODO
	 */
	@Override
	public void paintBorder(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 5, 5);
	}
	
	/**
	 * TODO
	 */
	@Override
	public void setEnabled(boolean b) {
		if (!b) {
			this.currentBackColor = DISABLED_BACKGROUND_COLOR;
			this.setForeground(Color.GRAY);
		} else {
			this.currentBackColor = DEFAULT_BACKGROUND_COLOR;
			this.setForeground(Color.WHITE);
		}
		repaint();
		super.setEnabled(b);
	}
}
