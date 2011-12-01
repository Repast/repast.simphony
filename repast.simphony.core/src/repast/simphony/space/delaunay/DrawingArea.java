package repast.simphony.space.delaunay;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*******************************************************************************
 * Canvas class. Accpets inputs and draws the graph. * - initialize():
 * Initializes the whole graph; * - mouseDown(): Reacts to clicks or
 * shift-clicks; * - mouseDrag(): Reacts to mouse drags; * - addPoint(): Add a
 * point and update display; * - deletePoint():Delete a point and update
 * display; * - run(): Show animation; * - paint(): Draw graphs. *
 ******************************************************************************/

class DrawingArea extends JPanel {
	private static final long serialVersionUID = 3256723961659405622L;

	DelaunayGraph graph; // Underlying graph
	IrregularGridRenderer rend;

	public DrawingArea() {
		super();
		initialize();
		rend = new IrregularGridRenderer();
	}

	public DrawingArea(DelaunayGraph graph) {
		this.graph = graph;
		initialize();
		rend = new IrregularGridRenderer();
	}

	// Initialize all. Start from beginning...
	void initialize() {
		setBackground(Color.WHITE);
		if (graph == null)
			graph = new DelaunayGraph();
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int x = getSize().width;
		int y = getSize().height;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, x, y);
		rend.drawGraph(graph, g, Color.blue, Color.RED, new Color(0, 192, 64), false,
				true, false);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Irregular2DGrid<String> grid = new Irregular2DGrid<String>(1000);
		DelaunayGraph graph = null;//new DelaunayGraph(grid);
		DrawingArea da = new DrawingArea(graph);
		frame.getContentPane().add(da);

		frame.setVisible(true);

	}
}
