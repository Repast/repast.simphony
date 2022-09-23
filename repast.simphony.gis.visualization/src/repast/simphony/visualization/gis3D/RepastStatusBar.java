package repast.simphony.visualization.gis3D;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.NetworkCheckThread;
import gov.nasa.worldwind.util.NetworkStatus;
import gov.nasa.worldwind.util.WWMath;

/**
 * This implementation of WW StatusBar fixes bugs relating to disposal
 *   and WW shutdown to make sure all objects within are disposed properly, 
 *   notably the Timer and PropertyChangeListeners.
 * 
 * @author Eric Tatara
 *
 */
public class RepastStatusBar extends JPanel implements PositionListener, RenderingListener {

	// Units constants TODO: Replace with UnitsFormat
	public final static String UNIT_METRIC = "gov.nasa.worldwind.StatusBar.Metric";
	public final static String UNIT_IMPERIAL = "gov.nasa.worldwind.StatusBar.Imperial";

	protected static final int MAX_ALPHA = 254;

	private WorldWindow eventSource;
	private String elevationUnit = UNIT_METRIC;
	private String angleFormat = Angle.ANGLE_FORMAT_DD;

	protected final JLabel latDisplay = new JLabel("");
	protected final JLabel lonDisplay = new JLabel(Logging.getMessage("term.OffGlobe"));
	protected final JLabel altDisplay = new JLabel("");
	protected final JLabel eleDisplay = new JLabel("");

	protected AtomicBoolean showNetworkStatus = new AtomicBoolean(true);
	protected AtomicBoolean isNetworkAvailable = new AtomicBoolean(true);
	protected Thread netCheckThread;
	Timer downloadTimer;

	public RepastStatusBar()
	{
		super(new GridLayout(1, 0));

		final JLabel heartBeat = new JLabel(Logging.getMessage("term.Downloading"));

		altDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		latDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		lonDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		eleDisplay.setHorizontalAlignment(SwingConstants.CENTER);

		this.add(altDisplay);
		this.add(latDisplay);
		this.add(lonDisplay);
		this.add(eleDisplay);
		this.add(heartBeat);

		heartBeat.setHorizontalAlignment(SwingConstants.CENTER);
		heartBeat.setForeground(new java.awt.Color(255, 0, 0, 0));

		downloadTimer = new Timer(100, new ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent actionEvent)
			{
				if (!showNetworkStatus.get())
				{
					if (heartBeat.getText().length() > 0)
						heartBeat.setText("");
					return;
				}

				if (!isNetworkAvailable.get())
				{
					heartBeat.setText(Logging.getMessage("term.NoNetwork"));
					heartBeat.setForeground(new Color(255, 0, 0, MAX_ALPHA));
					return;
				}

				Color color = heartBeat.getForeground();
				int alpha = color.getAlpha();
				if (isNetworkAvailable.get() && WorldWind.getRetrievalService().hasActiveTasks())
				{
					heartBeat.setText(Logging.getMessage("term.Downloading"));
					if (alpha >= MAX_ALPHA)
						alpha = MAX_ALPHA;
					else
						alpha = alpha < 16 ? 16 : Math.min(MAX_ALPHA, alpha + 20);
				}
				else
				{
					alpha = Math.max(0, alpha - 20);
				}
				heartBeat.setForeground(new Color(255, 0, 0, alpha));
			}
		});
		downloadTimer.start();

		this.netCheckThread = this.startNetCheckThread();

		WorldWind.getNetworkStatus().addPropertyChangeListener(NetworkStatus.HOST_UNAVAILABLE,listenerUnavaliable);
		WorldWind.getNetworkStatus().addPropertyChangeListener(NetworkStatus.HOST_AVAILABLE,listenerAvaliable);

	}

	PropertyChangeListener listenerAvaliable =  new PropertyChangeListener()
	{
		public void propertyChange(PropertyChangeEvent evt)
		{
			Object nv = evt.getNewValue();
			String message = Logging.getMessage("NetworkStatus.HostNowAvailable",
					nv != null && nv instanceof URL ? ((URL) nv).getHost() : "Unknown");
			Logging.logger().info(message);
		}
	};

	PropertyChangeListener listenerUnavaliable =new PropertyChangeListener()
	{
		public void propertyChange(PropertyChangeEvent evt)
		{
			Object nv = evt.getNewValue();
			String message = Logging.getMessage("NetworkStatus.UnavailableHost",
					nv != null && nv instanceof URL ? ((URL) nv).getHost() : "Unknown");
			Logging.logger().info(message);
		}
	};

	protected NetworkCheckThread startNetCheckThread()
	{
		NetworkCheckThread nct = new NetworkCheckThread(this.showNetworkStatus, this.isNetworkAvailable, null);
		nct.setDaemon(true);
		nct.start();

		return nct;
	}

	public void setEventSource(WorldWindow newEventSource)
	{
		if (this.eventSource != null)
		{
			this.eventSource.removePositionListener(this);
			this.eventSource.removeRenderingListener(this);
		}

		if (newEventSource != null)
		{
			newEventSource.addPositionListener(this);
			newEventSource.addRenderingListener(this);
		}

		this.eventSource = newEventSource;
	}

	public boolean isShowNetworkStatus()
	{
		return showNetworkStatus.get();
	}

	public void setShowNetworkStatus(boolean showNetworkStatus)
	{
		this.showNetworkStatus.set(showNetworkStatus);

		if (showNetworkStatus)
		{
			if (this.netCheckThread != null)
				this.netCheckThread.interrupt();

			this.netCheckThread = this.startNetCheckThread();
		}
		else
		{
			if (this.netCheckThread != null)
				this.netCheckThread.interrupt();

			this.netCheckThread = null;
		}
	}

	public void moved(PositionEvent event)
	{
		this.handleCursorPositionChange(event);
	}

	public WorldWindow getEventSource()
	{
		return this.eventSource;
	}

	public String getElevationUnit()
	{
		return this.elevationUnit;
	}

	public void setElevationUnit(String unit)
	{
		if (unit == null)
		{
			String message = Logging.getMessage("nullValue.StringIsNull");
			Logging.logger().severe(message);
			throw new IllegalArgumentException(message);
		}

		this.elevationUnit = unit;
	}

	public String getAngleFormat()
	{
		return this.angleFormat;
	}

	public void setAngleFormat(String format)
	{
		if (format == null)
		{
			String message = Logging.getMessage("nullValue.StringIsNull");
			Logging.logger().severe(message);
			throw new IllegalArgumentException(message);
		}

		this.angleFormat = format;
	}

	protected String makeCursorElevationDescription(double metersElevation)
	{
		String s;
		String elev = Logging.getMessage("term.Elev");
		if (UNIT_IMPERIAL.equals(elevationUnit))
			s = String.format(elev + " %,7d feet", (int) (WWMath.convertMetersToFeet(metersElevation)));
		else // Default to metric units.
			s = String.format(elev + " %,7d meters", (int) metersElevation);
		return s;
	}

	protected String makeEyeAltitudeDescription(double metersAltitude)
	{
		String s;
		String altitude = Logging.getMessage("term.Altitude");
		if (UNIT_IMPERIAL.equals(elevationUnit))
		{
			double miles = WWMath.convertMetersToMiles(metersAltitude);
			if (Math.abs(miles) >= 1)
				s = String.format(altitude + " %,7d mi", (int) Math.round(miles));
			else
				s = String.format(altitude + " %,7d ft", (int) Math.round(WWMath.convertMetersToFeet(metersAltitude)));
		}
		else if (Math.abs(metersAltitude) >= 1000) // Default to metric units.
			s = String.format(altitude + " %,7d km", (int) Math.round(metersAltitude / 1e3));
		else
			s = String.format(altitude + " %,7d m", (int) Math.round(metersAltitude));
		return s;
	}

	protected String makeAngleDescription(String label, Angle angle)
	{
		String s;
		if (Angle.ANGLE_FORMAT_DMS.equals(angleFormat))
			s = String.format("%s %s", label, angle.toDMSString());
		else
			s = String.format("%s %7.4f\u00B0", label, angle.degrees);
		return s;
	}

	protected void handleCursorPositionChange(PositionEvent event)
	{
		Position newPos = event.getPosition();
		if (newPos != null)
		{
			String las = makeAngleDescription("Lat", newPos.getLatitude());
			String los = makeAngleDescription("Lon", newPos.getLongitude());
			String els = makeCursorElevationDescription(
					eventSource.getModel().getGlobe().getElevation(newPos.getLatitude(), newPos.getLongitude()));
			latDisplay.setText(las);
			lonDisplay.setText(los);
			eleDisplay.setText(els);
		}
		else
		{
			latDisplay.setText("");
			lonDisplay.setText(Logging.getMessage("term.OffGlobe"));
			eleDisplay.setText("");
		}
	}

	public void dispose(){
		setShowNetworkStatus(false);
		downloadTimer.stop();
		setEventSource(null);
		WorldWind.getNetworkStatus().removePropertyChangeListener(NetworkStatus.HOST_UNAVAILABLE,listenerUnavaliable);
		WorldWind.getNetworkStatus().removePropertyChangeListener(NetworkStatus.HOST_AVAILABLE,listenerAvaliable);
	}

	public void stageChanged(RenderingEvent event)
	{
		if (!event.getStage().equals(RenderingEvent.BEFORE_BUFFER_SWAP))
			return;

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				if (eventSource.getView() != null && eventSource.getView().getEyePosition() != null)
					altDisplay.setText(makeEyeAltitudeDescription(
							eventSource.getView().getEyePosition().getElevation()));
				else
					altDisplay.setText(Logging.getMessage("term.Altitude"));
			}
		});
	}
}
