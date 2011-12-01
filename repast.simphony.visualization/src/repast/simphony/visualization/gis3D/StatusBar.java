/*
Copyright (C) 2001, 2006 United States Government
as represented by the Administrator of the
National Aeronautics and Space Administration.
All Rights Reserved.
*/
package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.geom.Position;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * @author tag
 * @version $Id: StatusBar.java 3735 2007-12-06 02:20:43Z tgaskins $
 */
public class StatusBar extends JPanel implements PositionListener, RenderingListener
{
    private WorldWindow eventSource;
    private final JLabel latDisplay = new JLabel("");
    private final JLabel lonDisplay = new JLabel("Off globe");
    private final JLabel altDisplay = new JLabel("");
    private final JLabel eleDisplay = new JLabel("");
    private boolean showNetworkStatus = true;

    public StatusBar()
    {
        super(new GridLayout(1, 0));

        final JLabel heartBeat = new JLabel("Downloading");

        altDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        latDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        lonDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        eleDisplay.setHorizontalAlignment(SwingConstants.CENTER);

//        this.add(new JLabel("")); // dummy label to visually balance with heartbeat
        this.add(altDisplay);
        this.add(latDisplay);
        this.add(lonDisplay);
        this.add(eleDisplay);
        this.add(heartBeat);

        heartBeat.setHorizontalAlignment(SwingConstants.CENTER);
        heartBeat.setForeground(new java.awt.Color(255, 0, 0, 0));

        Timer downloadTimer = new Timer(100, new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent actionEvent)
            {
                if (!showNetworkStatus)
                {
                    if (heartBeat.getText().length() > 0)
                        heartBeat.setText("");
                    return;
                }

                if (WorldWind.getNetworkStatus().isNetworkUnavailable())
                {
                    heartBeat.setText("No Network");
                    heartBeat.setForeground(new java.awt.Color(255, 0, 0, 255));
                    return;
                }

                java.awt.Color color = heartBeat.getForeground();
                int alpha = color.getAlpha();
                if (WorldWind.getRetrievalService().hasActiveTasks())
                {
                    heartBeat.setText("Downloading");
                    if (alpha == 255)
                        alpha = 255;
                    else
                        alpha = alpha < 16 ? 16 : Math.min(255, alpha + 20);
                }
                else
                {
                    alpha = Math.max(0, alpha - 20);
                }
                heartBeat.setForeground(new java.awt.Color(255, 0, 0, alpha));
            }
        });
        downloadTimer.start();
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
        return showNetworkStatus;
    }

    public void setShowNetworkStatus(boolean showNetworkStatus)
    {
        this.showNetworkStatus = showNetworkStatus;
    }

    public void moved(PositionEvent event)
    {
        this.handleCursorPositionChange(event);
    }

    public WorldWindow getEventSource()
    {
        return this.eventSource;
    }

    private void handleCursorPositionChange(PositionEvent event)
    {
        Position newPos = event.getPosition();
        if (newPos != null)
        {
            String las = String.format("Lat %7.4f\u00B0", newPos.getLatitude().getDegrees());
            String los = String.format("Lon %7.4f\u00B0", newPos.getLongitude().getDegrees());
            String els = String.format("Elev %,7d meters", (int)
                (eventSource.getModel().getGlobe().getElevation(newPos.getLatitude(), newPos.getLongitude())));
            latDisplay.setText(las);
            lonDisplay.setText(los);
            eleDisplay.setText(els);
        }
        else
        {
            latDisplay.setText("");
            lonDisplay.setText("Off globe");
            eleDisplay.setText("");
        }
    }

    public void stageChanged(RenderingEvent event)
    {
        if (eventSource.getView() != null && eventSource.getView().getEyePosition() != null)
            altDisplay.setText(String.format("Altitude %,7d km",
                (int) Math.round(eventSource.getView().getEyePosition().getElevation() / 1e3)));
        else
            altDisplay.setText("Altitude");
    }
}
