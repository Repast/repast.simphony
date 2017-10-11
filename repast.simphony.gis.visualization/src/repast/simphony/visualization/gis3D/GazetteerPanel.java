/*
Copyright (C) 2001, 2006 United States Government
as represented by the Administrator of the
National Aeronautics and Space Administration.
All Rights Reserved.
*/
package repast.simphony.visualization.gis3D;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.exception.NoItemException;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.poi.BasicPointOfInterest;
import gov.nasa.worldwind.poi.Gazetteer;
import gov.nasa.worldwind.poi.PointOfInterest;
import gov.nasa.worldwind.poi.YahooGazetteer;
import gov.nasa.worldwind.view.orbit.OrbitView;

/**
 * Modified WW Gazetter that fits better into Repast GIS displays
 * 
 * @author Eric Tatara
 *
 */
public class GazetteerPanel extends JPanel
{
    private final WorldWindow wwd;
    private Gazetteer gazeteer;
    private JPanel resultsPanel;
    private JComboBox resultsBox;

    public static final String DEFAULT_SEARCH_TEXT = "Search";
    
    public GazetteerPanel(final WorldWindow wwd, String gazetteerClassName)
        throws IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        super(new BorderLayout());

        if (gazetteerClassName != null)
            this.gazeteer = this.constructGazetteer(gazetteerClassName);
        else
            this.gazeteer = new YahooGazetteer();

        this.wwd = wwd;

        // The text field
        final JTextField field = new JTextField(DEFAULT_SEARCH_TEXT);
        field.setFont(new Font("Arial",Font.ITALIC, 12));
        
        // Clear the Search text hint on first click
        field.addMouseListener(new MouseAdapter(){
          @Override
          public void mouseClicked(MouseEvent e){
          	if (field.getText().equals(DEFAULT_SEARCH_TEXT))
          		field.setText("");
          	field.setFont(new Font("Arial",Font.PLAIN, 12));
          }
      });

        
        field.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent actionEvent)
            {
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            handleEntryAction(actionEvent);
                        }
                        catch (NoItemException e)
                        {
                            JOptionPane.showMessageDialog(GazetteerPanel.this,
                                "Location not available \"" + (field.getText() != null ? field.getText() : "") + "\"\n"
                                    + "(" + e.getMessage() + ")",
                                "Location Not Available", JOptionPane.ERROR_MESSAGE);
                        }
                        catch (IllegalArgumentException e)
                        {
                            JOptionPane.showMessageDialog(GazetteerPanel.this,
                                "Error parsing input \"" + (field.getText() != null ? field.getText() : "") + "\"\n"
                                    + e.getMessage(),
                                "Lookup Failure", JOptionPane.ERROR_MESSAGE);                  
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(GazetteerPanel.this,
                                "Error looking up \"" + (field.getText() != null ? field.getText() : "") + "\"\n"
                                    + e.getMessage(),
                                "Lookup Failure", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        });

        // Enclose entry field in an inner panel in order to control spacing/padding
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.add(field, BorderLayout.CENTER);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        // Put everything together
        this.add(fieldPanel, BorderLayout.CENTER);

        resultsPanel = new JPanel(new GridLayout(1,2));
        resultsPanel.add(new JLabel("Results: "));
        resultsBox = new JComboBox();
        resultsBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent actionEvent)
            {
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        JComboBox cb = (JComboBox)actionEvent.getSource();
                        PointOfInterest selectedPoi = (PointOfInterest)cb.getSelectedItem();
                        moveToLocation(selectedPoi);
                    }
                });
            }
        });
        resultsPanel.add(resultsBox);
        resultsPanel.setVisible(false);
        
        // Don't include ther results panel
//        this.add(resultsPanel, BorderLayout.EAST);
    }

    private Gazetteer constructGazetteer(String className)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        if (className == null || className.length() == 0)
        {
            throw new IllegalArgumentException("Gazetteer class name is null");
        }

        Class c = Class.forName(className.trim());
        Object o = c.newInstance();

        if (!(o instanceof Gazetteer))
            throw new IllegalArgumentException("Gazetteer class name is null");

        return (Gazetteer) o;
    }

    private void handleEntryAction(ActionEvent actionEvent) throws IOException, ParserConfigurationException,
        XPathExpressionException, SAXException, NoItemException, IllegalArgumentException
    {
        String lookupString = null;

        //hide any previous results
        resultsPanel.setVisible(false);
        if (actionEvent.getSource() instanceof JTextComponent)
            lookupString = ((JTextComponent) actionEvent.getSource()).getText();

        if (lookupString == null || lookupString.length() < 1)
            return;

        java.util.List<PointOfInterest> poi = parseSearchValues(lookupString);

        if (poi != null)
        {
            if (poi.size() == 1)
            {
                this.moveToLocation(poi.get(0));
            }
            else
            {
                resultsBox.removeAllItems();
                for ( PointOfInterest p:poi)
                {
                    resultsBox.addItem(p);
                }
                resultsPanel.setVisible(true);
            }
        }
    }

    /*
    Sample imputs
    Coordinate formats:
    39.53, -119.816  (Reno, NV)
    21 10 14 N, 86 51 0 W (Cancun)
    -31¡ 59' 43", 115¡ 45' 32" (Perth)
     */
    private java.util.List<PointOfInterest> parseSearchValues(String searchStr)
    {
        String sepRegex = "[,]"; //other seperators??
        searchStr = searchStr.trim();
        String[] searchValues = searchStr.split(sepRegex);
        if (searchValues.length == 1) 
        {
            return queryService(searchValues[0].trim());
        }
        else if (searchValues.length == 2) //possible coordinates
        {
            //any numbers at all?
            String regex = "[0-9]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(searchValues[1]); //Street Address may have numbers in first field so use 2nd
            if (matcher.find())
            {
                java.util.List<PointOfInterest> list = new ArrayList<PointOfInterest>();
                list.add(parseCoordinates(searchValues));
                return list;
            }
            else
            {
                return queryService(searchValues[0].trim() + "+" + searchValues[1].trim());
            }
        }
        else
        {
            //build search string and send to service
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<searchValues.length; i++)
            {
                sb.append(searchValues[i].trim());
                if (i < searchValues.length-1)
                    sb.append("+");

            }

            return queryService(sb.toString());
        }
    }

    private java.util.List<PointOfInterest> queryService(String queryString)
    {
        java.util.List<PointOfInterest> results = this.gazeteer.findPlaces(queryString);
        if (results == null || results.size() == 0)
            return null;
        else
            return results;
    }

    //throws IllegalArgumentException
    private PointOfInterest parseCoordinates(String coords[])
    {
        if (isDecimalDegrees(coords))
        {
            Double d1 = Double.parseDouble(coords[0].trim());
            Double d2 = Double.parseDouble(coords[1].trim());

            return new BasicPointOfInterest(LatLon.fromDegrees(d1, d2));
        }
        else //may be in DMS
        {
            Angle aLat = Angle.fromDMS(coords[0].trim());
            Angle aLon = Angle.fromDMS(coords[1].trim());

            return new BasicPointOfInterest(LatLon.fromDegrees(aLat.getDegrees(), aLon.getDegrees()));
        }
    }

    private boolean isDecimalDegrees(String[] coords)
    {
        try{
            Double.parseDouble(coords[0].trim());
            Double.parseDouble(coords[1].trim());
        } catch(NumberFormatException nfe)
        {
            return false;
        }

        return true;
    }

    public void moveToLocation(PointOfInterest location)
    {
            // Use a PanToIterator to iterate view to target position
        this.wwd.getView().goTo(new Position(location.getLatlon(), 0), 25e3);
    }

    public void moveToLocation(Sector sector, Double altitude)
    {
        OrbitView view = (OrbitView) this.wwd.getView();
        Globe globe = this.wwd.getModel().getGlobe();

        if (altitude == null || altitude == 0)
        {
        	double t = sector.getDeltaLonRadians() > sector.getDeltaLonRadians()
        		? sector.getDeltaLonRadians() : sector.getDeltaLonRadians();
        	double w = 0.5 * t * 6378137.0;
        	altitude = w / this.wwd.getView().getFieldOfView().tanHalfAngle();
        }

        if (globe != null && view != null)
        {
            this.wwd.getView().goTo(new Position(sector.getCentroid(), 0), altitude);
        }
    }
}
