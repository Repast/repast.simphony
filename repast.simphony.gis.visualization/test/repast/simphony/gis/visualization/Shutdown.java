package repast.simphony.gis.visualization;
/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */



import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;

/**
 * Shows how to shut down a {@link WorldWindow} and how to shut down all of World Wind.
 *
 * @author tag
 * @version $Id: Shutdown.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class Shutdown
{
    private static class AppFrame extends javax.swing.JFrame
    {
        private WorldWindow wwd;
        private ShutdownWindowAction shutdownAction;
        private CreateWindowAction createWindowAction;

        public AppFrame()
        {
            this.getContentPane().setLayout(new BorderLayout(10, 10));

            JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
            this.getContentPane().add(controlPanel, BorderLayout.WEST);

            JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            controlPanel.add(buttonPanel, BorderLayout.NORTH);

            this.shutdownAction = new ShutdownWindowAction();
            buttonPanel.add(new JButton(this.shutdownAction));
            this.shutdownAction.setEnabled(true);

            this.createWindowAction = new CreateWindowAction();
            buttonPanel.add(new JButton(this.createWindowAction));

            buttonPanel.add(new JButton(new ShutdownWorldWindAction()));

            this.createWindow();

            this.pack();
        }

        private void createWindow()
        {
            WorldWindowGLCanvas wwc = new WorldWindowGLCanvas();
            wwc.setPreferredSize(new java.awt.Dimension(800, 600));
            this.getContentPane().add(wwc, java.awt.BorderLayout.CENTER);
            wwc.setModel(new BasicModel());
            this.wwd = wwc;
        }

        private void destroyCurrentWindow()
        {
            if (this.wwd != null)
            {
                getContentPane().remove((Component) wwd);
                wwd = null;
            }
        }

        private class ShutdownWindowAction extends AbstractAction
        {
            public ShutdownWindowAction()
            {
                super("Shutdown Window");
            }

            public void actionPerformed(ActionEvent e)
            {
                if (wwd != null)
                {
                    wwd.shutdown();
                    destroyCurrentWindow();
                    this.setEnabled(false);
                    createWindowAction.setEnabled(true);
                }
            }
        }

        private class CreateWindowAction extends AbstractAction
        {
            public CreateWindowAction()
            {
                super("Create Window");
                this.setEnabled(false);
            }

            public void actionPerformed(ActionEvent e)
            {
                createWindow();
                pack();
                this.setEnabled(false);
                shutdownAction.setEnabled(true);
            }
        }

        private class ShutdownWorldWindAction extends AbstractAction
        {
            public ShutdownWorldWindAction()
            {
                super("Shutdown World Wind");
            }

            public void actionPerformed(ActionEvent e)
            {
                WorldWind.shutDown();
                destroyCurrentWindow();
                createWindowAction.setEnabled(true);
            }
        }
    }

    public static void main(String[] args)
    {
        if (Configuration.isMacOS())
        {
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Shutdown World Wind");
        }

        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                // Create an AppFrame and immediately make it visible. As per Swing convention, this
                // is done within an invokeLater call so that it executes on an AWT thread.
                AppFrame appFrame = new AppFrame();
                appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                appFrame.setVisible(true);
            }
        });
    }
}
