/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repast.simphony.relogo.ide.image;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import repast.simphony.relogo.image.NLImage;

/**
 *
 * @author CBURKE
 */
public class ImageSelectionPanel extends JPanel {

    ImagePanel customSurface;
    private javax.swing.JComboBox imageSelector;
    private javax.swing.JButton nextButton;
    List<NLImage> imageList;
    int currentImageIndex;
    DefaultComboBoxModel imageSelectionModel;

    class ImagePanel extends JPanel {

        NLImage img;
        Rectangle rect;

        public ImagePanel() {
            super();
            rect = new Rectangle();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                img.render(g, getBounds(rect), null);
            }
        }

        public void setImage(NLImage i) {
            img = i;
            invalidate();
            repaint();
        }
    }

    static protected List<NLImage> extractSortedValues(HashMap<String, NLImage> imgMap) {
        List<NLImage> imgList = new LinkedList<NLImage>();
        imgList.addAll(imgMap.values());
        Collections.sort(imgList);
        return imgList;
    }
    
    public ImageSelectionPanel(HashMap<String, NLImage> imgMap) {
        this(extractSortedValues(imgMap));
    }
    
    /** Creates new form ImageTester */
    public ImageSelectionPanel(List<NLImage> imgList) {
        super();
        imageList = imgList;
        imageSelectionModel = new DefaultComboBoxModel();
        
        nextButton = new javax.swing.JButton();
        imageSelector = new javax.swing.JComboBox();
        setLayout(new FlowLayout());

        JPanel renderPanel = new JPanel();
        renderPanel.setBackground(new java.awt.Color(0, 0, 0));
        renderPanel.setForeground(new java.awt.Color(255, 255, 51));
        renderPanel.setPreferredSize(new java.awt.Dimension(300, 300));
        renderPanel.setMinimumSize(new java.awt.Dimension(300, 300));
        renderPanel.setMaximumSize(new java.awt.Dimension(300, 300));
        renderPanel.setLayout(new BorderLayout());
        customSurface = new ImagePanel();
        customSurface.setVisible(true);
        customSurface.setOpaque(false);
        renderPanel.add(customSurface);
        //add(BorderLayout.WEST, new JPanel());  // buffer
        //add(BorderLayout.EAST, new JPanel());  // buffer
        //JPanel centerBuffer = new JPanel();
        //centerBuffer.setLayout(new FlowLayout());
        //centerBuffer.add(customSurface);
        add(/*BorderLayout.NORTH, */renderPanel);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1));
        
        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        controlPanel.add(nextButton);

        imageSelector.setModel(imageSelectionModel);
        imageSelector.setAutoscrolls(true);
        imageSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageSelected(evt);
            }
        });
        controlPanel.add(imageSelector);
        add(/*BorderLayout.SOUTH, */controlPanel);

        for (NLImage img : imageList) {
            imageSelectionModel.addElement(img.getName());
        }
        imageSelectionModel.setSelectedItem(imageSelectionModel.getElementAt(currentImageIndex));
        customSurface.setImage(imageList.get(currentImageIndex));
    }

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {                                         
        currentImageIndex++;
        if (currentImageIndex >= imageList.size()) {
            currentImageIndex = 0;
        }
        imageSelectionModel.setSelectedItem(imageSelectionModel.getElementAt(currentImageIndex));
        customSurface.setImage(imageList.get(currentImageIndex));
    }                                        

    private void imageSelected(java.awt.event.ActionEvent evt) {
        currentImageIndex = imageSelectionModel.getIndexOf(imageSelectionModel.getSelectedItem());
        imageSelectionModel.setSelectedItem(imageSelectionModel.getElementAt(currentImageIndex));
        customSurface.setImage(imageList.get(currentImageIndex));
    }
}
