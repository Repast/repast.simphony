package repast.simphony.gis.styleEditor;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StyleEditorDialog extends JPanel {
	JButton fillColorButton;

	JButton strokeColorButton;

	StyleEditor editor;

	public StyleEditorDialog() {
		init();
		initLayout();

	}

	public StyleEditorDialog(StyleEditor editor) {
		this();
		setEditor(editor);
	}

	private void init() {
		fillColorButton = new JButton();
		fillColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Color color = JColorChooser.showDialog(StyleEditorDialog.this,
						"Choose a Fill Color", StyleEditorDialog.this.editor
								.getFillColor());
				if (color != null) {
					StyleEditorDialog.this.editor.setFillColor(color);
					fillColorButton.setIcon(new SquareIcon(color));
				}
			}
		});

		strokeColorButton = new JButton();
		strokeColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Color color = JColorChooser.showDialog(StyleEditorDialog.this,
						"Choose a Outline Color", StyleEditorDialog.this.editor
								.getStrokeColor());
				if (color != null) {
					StyleEditorDialog.this.editor.setStrokeColor(color);
					strokeColorButton.setIcon(new SquareIcon(color));
				}
			}
		});
	}

	public StyleEditor getEditor() {
		return editor;
	}

	public void setEditor(StyleEditor editor) {
		this.editor = editor;
		Color color = editor.getFillColor();
		fillColorButton.setIcon(new SquareIcon(color));
		color = editor.getStrokeColor();
		strokeColorButton.setIcon(new SquareIcon(color));
	}

	private void initLayout() {
		FormLayout layout = new FormLayout("3dlu, 10dlu, pref", "");
		PanelBuilder builder = new PanelBuilder(layout, this);
		builder.addSeparator("Fill Color");
	}

	class SquareIcon implements Icon {
		Color color;

		public SquareIcon(Color color) {
			this.color = color;
		}

		public int getIconHeight() {
			return 10;
		}

		public int getIconWidth() {
			return 10;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color oldColor = g.getColor();
			g.setColor(color);
			g.fillRect(x, y, getIconWidth(), getIconHeight());
			g.setColor(Color.BLACK);
			g.drawRect(x, y, getIconWidth(), getIconHeight());
			g.setColor(oldColor);
		}

	}
}
