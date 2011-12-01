/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package repast.simphony.visualization.visualization2D;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * $Id: ImageLoader.java,v 1.1 2006/01/06 22:35:19 collier Exp $
 *
 * @author Ian Turton
 */
class ImageLoader implements Runnable {
    /** The logger for the rendering module. */
    private static final Logger LOGGER = Logger.getLogger("org.geotools.rendering");

    /** The images managed by the loader */
    private static Map images = new HashMap();

    /** A canvas used as the image observer on the tracker */
    private static Canvas obs = new Canvas();

    /** Used to track the images loading status */
    private static MediaTracker tracker = new MediaTracker(obs);

    /** Currently loading image */
    private static int imageID = 1;

    /** A maximum time to wait for the image to load */
    private static long timeout = 10000;

    /** Location of the loading image */
    private URL location;

    /** Still waiting for the image? */
    private boolean waiting = true;

    /**
     * Returns the timeout for aborting an image loading sequence
     *
     * @return the timeout in milliseconds
     */
    public static long getTimeout() {
        return timeout;
    }

    /**
     * Sets the maximum time to wait for getting an external image. Set it to -1 to wait
     * undefinitely
     *
     * @param newTimeout the new timeout value in milliseconds
     */
    public static void setTimeout(long newTimeout) {
        timeout = newTimeout;
    }

    /**
     * Add an image to be loaded by the ImageLoader
     *
     * @param location the image location
     * @param interactive if true the methods returns immediatly, otherwise waits for the image to
     *        be loaded
     */
    private void add(URL location, boolean interactive) {
        int imgId = imageID;
        this.location = location;
        LOGGER.finest("adding image, interactive? " + interactive);

        Thread t = new Thread(this);
        t.start();

        if (interactive) {
            LOGGER.finest("fast return");

            return;
        } else {
            waiting = true;

            long elapsed = 0;
            final long step = 500;

            while (waiting && (elapsed < timeout || timeout < 0)) {
                LOGGER.finest("waiting..." + waiting);

                try {
                    Thread.sleep(step);
                    elapsed += step;

                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.finest("Waiting for image " + location + ", elapsed " + elapsed
                            + " milliseconds");
                    }
                } catch (InterruptedException e) {
                    LOGGER.warning(e.toString());
                }
            }

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(imgId + " complete?: " + (isFlagUp(imgId, MediaTracker.COMPLETE)));
                LOGGER.finest(imgId + " abort?: " + (isFlagUp(imgId, MediaTracker.ABORTED)));
                LOGGER.finest(imgId + " error?: " + (isFlagUp(imgId, MediaTracker.ERRORED)));
                LOGGER.finest(imgId + " loading?: " + (isFlagUp(imgId, MediaTracker.LOADING)));
                LOGGER.finest(imgId + "slow return " + waiting);
            }

            return;
        }
    }

    /**
     * Checks the state of the current tracker against a flag
     *
     * @param id the image id
     * @param flag the flag to be checked
     *
     * @return true if the flag is up
     */
    private boolean isFlagUp(int id, int flag) {
        return (tracker.statusID(id, true) & flag) == flag;
    }

    /**
     * Fetch a buffered image from the loader, if interactive is false then the loader will wait
     * for  the image to be available before returning, used by printers and file output
     * renderers. If interactive is true and the image is ready then return, if image is not ready
     * start loading it  and return null. The renderer is responsible for finding an alternative
     * to use.
     *
     * @param location the url of the image to be fetched
     * @param interactive boolean to signal if the loader should wait for the image to be ready.
     *
     * @return the buffered image or null
     */
    public BufferedImage get(URL location, boolean interactive) {
        if (images.containsKey(location)) {
            LOGGER.finest("found it");

            return (BufferedImage) images.get(location);
        } else {
            if (!interactive) {
                images.put(location, null);
            }

            LOGGER.finest("adding " + location);
            add(location, interactive);

            return (BufferedImage) images.get(location);
        }
    }

    /**
     * Runs the loading thread
     */
    public void run() {
        int myID = 0;
        Image img = null;

        try {
            img = Toolkit.getDefaultToolkit().createImage(location);
            myID = imageID++;
            tracker.addImage(img, myID);
        } catch (Exception e) {
            LOGGER.warning("Exception fetching image from " + location + "\n" + e);
            images.remove(location);
            waiting = false;

            return;
        }

        try {
            while ((tracker.statusID(myID, true) & MediaTracker.LOADING) != 0) {
                tracker.waitForID(myID, 500);
                LOGGER.finest(myID + "loading - waiting....");
            }
        } catch (InterruptedException ie) {
            LOGGER.warning(ie.toString());
        }

        int state = tracker.statusID(myID, true);

        if (state == MediaTracker.ERRORED) {
            LOGGER.finer("" + myID + " Error loading");
            // images.remove(location);
            waiting = false;

            return;
        }

        if ((state & MediaTracker.COMPLETE) == MediaTracker.COMPLETE) {
            LOGGER.finest("" + myID + "completed load");

            int iw = img.getWidth(obs);
            int ih = img.getHeight(obs);
            BufferedImage bi = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
            Graphics2D big = bi.createGraphics();
            big.drawImage(img, 0, 0, obs);
            images.put(location, bi);

            waiting = false;

            return;
        }

        LOGGER.finer("" + myID + " whoops - some other outcome " + state);
        waiting = false;

        return;
    }
    
    /**
     * Resets the image cache
     */
    public void reset() {
        images.clear();
    }
}
