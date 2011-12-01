/*CopyrightHere*/
/**
 * 
 */
package repast.simphony.ui.widget;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import simphony.util.messages.MessageCenter;

/**
 * A {@link javax.swing.JButton} that shows a little arrow icon on it. When the user clicks in the
 * area of that icon it will pop up a given menu. If the user clicks anywhere else the first menu
 * item will be executed.
 * 
 * @author Jerry Vos
 */
public class DropDownButton extends JButton {
	private static final long serialVersionUID = -7493669615704536121L;

	private static final MessageCenter LOG = MessageCenter.getMessageCenter(DropDownButton.class);
	
	private static Image downArrow;

	public static final int DROP_DOWN_WIDTH = 20;

	static {
		try {
			downArrow = new ImageIcon(DropDownButton.class.getResource("down-arrow.png"))
					.getImage();
		} catch (Exception ex) {
			// ignore
			downArrow = null;
			LOG.warn("Could not find the down-arrow.png for the drop down buttons");
		}
	}

	private JPopupMenu popupMenu;

	private boolean inMenuLocation;

	private boolean alwaysDropDown = false;

	private ActionListener defaultActionAction;

	public DropDownButton() {
		this(false);
	}

	public DropDownButton(boolean alwaysDropDown) {
		super();

		this.alwaysDropDown = alwaysDropDown;
		
		init();
	}

	public DropDownButton(Action a) {
		super(a);

		init();
	}

	public DropDownButton(Icon icon) {
		super(icon);

		init();
	}

	public DropDownButton(String text, Icon icon) {
		super(text + " ", icon);

		init();
	}

	public DropDownButton(String text) {
		super(text + " ");

		init();
	}

	public DropDownButton(String text, JPopupMenu popupMenu) {
		super(text + " ");
		this.popupMenu = popupMenu;

		init();
	}

	private void init() {
		// need to:
		//  * track the mouse's location
		//  * show the pop up menu if we're in the corner
		//  * show the menu on a right click
		if (!alwaysDropDown) {
			defaultActionAction = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (popupMenu != null && popupMenu.getComponentCount() > 0) {
						if (popupMenu.getComponent(0) instanceof JMenuItem) {
							((JMenuItem) popupMenu.getComponent(0)).doClick();
						}
					}
				}
			};
			addActionListener(defaultActionAction);
		}

		addMouseListener(new MouseAdapter() {
			private void showMenu(MouseEvent e) {
				if (popupMenu != null) {
					Dimension baseSize = popupMenu.getPreferredSize();

					int width = Math.max(getWidth(), baseSize.width);
					popupMenu.setPreferredSize(new Dimension(width,
							popupMenu.getPreferredSize().height));
					popupMenu.show(DropDownButton.this, 0, getHeight());
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (alwaysDropDown || inMenuLocation || e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (e.getPoint().x >= getWidth() - DROP_DOWN_WIDTH) {
					inMenuLocation = true;
				} else {
					inMenuLocation = false;
				}
			}
		});
	}

	/**
	 * If we are not in the pop-up menu's location this will just call the super's
	 * {@link javax.swing.AbstractButton#fireActionPerformed(java.awt.event.ActionEvent)} method,
	 * otherwise this will not fire the event.
	 * 
	 * @param event
	 *            the action event that may be passed on.
	 */
	@Override
	protected void fireActionPerformed(ActionEvent event) {
		if (!alwaysDropDown && !inMenuLocation) {
			// we're going to show the pop-up menu so don't fire an event
			super.fireActionPerformed(event);
		}
	}

	/**
	 * Performs the super's drawing and then draws the little arrow icon
	 * 
	 * @param g
	 *            the graphics object used for drawing
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (downArrow != null) {
			g.drawImage(downArrow, getWidth() - 15, getHeight() / 2, null);
		}
	}

	/**
	 * Gets the pop-up menu that will be show as a drop-down.
	 * 
	 * @return the button's menu
	 */
	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	/**
	 * Sets the pop-up menu that will be show as a drop-down.
	 * 
	 * @param popupMenu
	 *            the button's menu
	 */
	public void setPopupMenu(JPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}

	public void setAlwaysDropDown(boolean alwaysDropDown) {
		if (!this.alwaysDropDown && alwaysDropDown) {
			addActionListener(defaultActionAction);
		} else if (this.alwaysDropDown && !alwaysDropDown) {
			removeActionListener(defaultActionAction);
		}
		this.alwaysDropDown = alwaysDropDown;
	}
	
	public boolean getAlwaysDropDown() {
		return alwaysDropDown;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPopupMenu menu = new JPopupMenu();
		JMenuItem item = new JMenuItem("menu1");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("menu item clicked");
			}
		});
		menu.add(item);
		menu.add(new JMenuItem("menu2"));

		frame.add(new DropDownButton("Add", menu));

		frame.pack();
		frame.setVisible(true);
	}
}