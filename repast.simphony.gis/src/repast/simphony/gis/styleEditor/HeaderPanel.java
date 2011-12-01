package repast.simphony.gis.styleEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class HeaderPanel extends JPanel {

	private ImageIcon icone;

	public HeaderPanel() {
		super(new BorderLayout());

		this.icone = new ImageIcon(new BufferedImage(24, 24,
				BufferedImage.TYPE_INT_ARGB));

		JPanel titres = new JPanel(new GridLayout(3, 1));
		titres.setOpaque(false);
		titres.setBorder(new EmptyBorder(12, 0, 12, 0));

		JLabel titreEnTete = new JLabel("Welcome to the Drag n' Ghost demo.");
		Font police = titreEnTete.getFont().deriveFont(Font.BOLD);
		titreEnTete.setFont(police);
		titreEnTete.setBorder(new EmptyBorder(0, 12, 0, 0));
		titres.add(titreEnTete);

		JLabel titre;
		titres
				.add(titre = new JLabel(
						"Try to drag the pictures and the buttons to drop them over the table."));
		police = titreEnTete.getFont().deriveFont(Font.PLAIN);
		titre.setFont(police);
		titre.setBorder(new EmptyBorder(0, 24, 0, 0));
		titres
				.add(titre = new JLabel(
						"You can also try to drag the gray area below the table rows."));
		police = titreEnTete.getFont().deriveFont(Font.PLAIN);
		titre.setFont(police);
		titre.setBorder(new EmptyBorder(0, 24, 0, 0));

		titre = new JLabel(this.icone);
		titre.setBorder(new EmptyBorder(0, 0, 0, 12));

		add(BorderLayout.WEST, titres);
		add(BorderLayout.EAST, titre);

		setPreferredSize(new Dimension(500, this.icone.getIconHeight() + 24));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!isOpaque()) {
			return;
		}

		Color control = UIManager.getColor("control");
		int width = getWidth();
		int height = getHeight();

		Graphics2D g2 = (Graphics2D) g;
		Paint storedPaint = g2.getPaint();
		g2.setPaint(new GradientPaint(this.icone.getIconWidth(), 0,
				Color.white, width, 0, control));
		g2.fillRect(0, 0, width, height);
		g2.setPaint(storedPaint);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(new HeaderPanel(), BorderLayout.NORTH);
		frame.setVisible(true);
	}
}

/*
 * Swing Hacks Tips and Tools for Killer GUIs By Joshua Marinacci, Chris Adamson
 * First Edition June 2005 Series: Hacks ISBN: 0-596-00907-0
 * http://www.oreilly.com/catalog/swinghks/
 */