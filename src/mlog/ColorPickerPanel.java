package mlog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * TODO
 * @author Filip Östermark
 * @version 2013-05-27
 */
public class ColorPickerPanel extends JPanel {
	private JColorChooser colorChooser;
	private Color backgroundColor;
	private Color foregroundColor;

	private static final long serialVersionUID = 2266828192394712927L;
	private static final Color FRAME_COLOR = new Color(40, 40, 40);

	/**
	 * TODO
	 * @param backgroundColor
	 * @param foregroundColor
	 */
	public ColorPickerPanel(Color backgroundColor, Color foregroundColor) {
		super();
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.colorChooser = new JColorChooser();
		this.colorChooser.setColor(foregroundColor);
		
		class PreviewPanel extends JComponent {
			private static final long serialVersionUID = -1665212813721501874L;
			private Color color;
			public PreviewPanel(JColorChooser chooser) {
				color = chooser.getColor();
			}
			public void paint(Graphics g) {
				g.setColor(color);
				g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
				g.setColor(new Color(190, 190, 190));
				g.drawLine(0, 0, this.getWidth(), 0);
				g.drawLine(0, 0, 0, this.getHeight());
				g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight());
				g.drawLine(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1);
			}
		}

		final PreviewPanel colorPreview = new PreviewPanel(colorChooser);
		colorPreview.setPreferredSize(new Dimension(40, 40));
		colorPreview.setBackground(Color.black);
		colorPreview.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
		colorChooser.setPreviewPanel(colorPreview);
		ColorSelectionModel model = colorChooser.getSelectionModel();
	    model.addChangeListener(new ChangeListener() {
	      public void stateChanged(ChangeEvent evt) {
	        ColorSelectionModel model = (ColorSelectionModel) evt.getSource();
	        colorPreview.color = model.getSelectedColor();
	      }
	    });
		
		AbstractColorChooserPanel panels[] = new AbstractColorChooserPanel[1];
		AbstractColorChooserPanel[] standardPanels = colorChooser.getChooserPanels();
		for (AbstractColorChooserPanel panel : standardPanels) {
			if (panel.getDisplayName().equals("HSB")) {
				panels[0] = panel;
			}
		}
		colorChooser.setChooserPanels(panels);
		colorChooser.setPreferredSize(new Dimension(350, 300));
	}

	/**
	 * TODO
	 * @param parent
	 * @param title
	 * @return
	 */
	public void showDialog(String title) {
		ActionListener okListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Color newColor = colorChooser.getColor();
					if (newColor != null) {
						foregroundColor = newColor;
						repaint();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		JColorChooser.createDialog(null, title, false, this.colorChooser, okListener, null).setVisible(true);
	}

	/**
	 * TODO
	 * @return
	 */
	public Color getForegroundColor() {
		return this.foregroundColor;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(this.backgroundColor);
		MapGenerator.drawGradientDot(new PixelPoint(this.getWidth()/2, this.getHeight()/2), 25, this.foregroundColor, (Graphics2D)g);
	}

	@Override
	public void paintBorder(Graphics g) {
		g.setColor(FRAME_COLOR);
		g.drawLine(0, 0, this.getWidth(), 0);
		g.drawLine(0, 0, 0, this.getHeight());
		g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight());
		g.drawLine(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1);
	}
}