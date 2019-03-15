package uk.ac.gre.comp1549.dashboard;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import uk.ac.gre.comp1549.dashboard.controls.BarPanel;
import uk.ac.gre.comp1549.dashboard.controls.DialPanel;
import uk.ac.gre.comp1549.dashboard.events.*;
import uk.ac.gre.comp1549.dashboard.scriptreader.DashboardEventGeneratorFromXML;

/**
 * DashboardDemoMain.java Contains the main method for the Dashboard demo
 * application. It: a) provides the controller screen which allows user input
 * which is passed to the display indicators, b) allows the user to run the XML
 * script which changes indicator values, c) creates the dashboard JFrame and
 * adds display indicators to it.
 *
 * @author COMP1549
 * @version 2.0
 */
public class DashboardDemoMain extends JFrame {

    /**
     * Name of the XML script file - change here if you want to use a different
     * filename
     */
    public static final String XML_SCRIPT = "dashboard_script.xml";

    // fields that appear on the control panel
    private JTextField txtSpeedValueInput;
    private JTextField txtPetrolValueInput;
    private JTextField txtKnotsValueInput; 
    private JTextField txtGearsValueInput;       
    private JButton btnScript;

    // fields that appear on the dashboard itself
    private DialPanel speedDial;
    private DialPanel petrolDial;
    private DialPanel knotsDial;
    private DialPanel gearsDial;
    private BarPanel petrolBar;
    private BarPanel knotsBar;
    private BarPanel gearsBar;

    /**
     * Constructor. Does maybe more work than is good for a constructor.
     */
    public DashboardDemoMain() {
        // Set up the frame for the controller
        setTitle("Dashboard demonstration controller");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        
        panel.add(new JLabel("Speed Value:"));
        txtSpeedValueInput = new JTextField("0", 3);
        panel.add(txtSpeedValueInput);
        DocumentListener speedListener = new SpeedValueListener();
        txtSpeedValueInput.getDocument().addDocumentListener(speedListener);
        
        
        panel.add(new JLabel("Petrol Value:"));
        txtPetrolValueInput = new JTextField("0", 3);
        panel.add(txtPetrolValueInput);
        DocumentListener petrolListener = new PetrolValueListener();
        txtPetrolValueInput.getDocument().addDocumentListener(petrolListener);
        
        
        panel.add(new JLabel("Knots Value:"));
        txtKnotsValueInput = new JTextField("0", 3);
        panel.add(txtKnotsValueInput);
        DocumentListener knotsListener = new KnotsValueListener();
        txtKnotsValueInput.getDocument().addDocumentListener(knotsListener);

        panel.add(new JLabel("Gears Value:"));
        txtGearsValueInput = new JTextField("0", 3);
        panel.add(txtGearsValueInput);
        DocumentListener gearsListener = new GearsValueListener();
        txtGearsValueInput.getDocument().addDocumentListener(gearsListener);


        
        
        btnScript = new JButton("Run XML Script");

        // When the button is read the XML script will be run
        btnScript.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        runXMLScript();
                    }
                }.start();
            }
        });
        panel.add(btnScript);
        add(panel);
        pack();

        setLocationRelativeTo(null); // display in centre of screen
        this.setVisible(true);

        
        
        // Set up the dashboard screen        
        JFrame dashboard = new JFrame("Demo dashboard");
        dashboard.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        dashboard.setLayout(new FlowLayout());

        // add the speed Dial
        speedDial = new DialPanel();
        speedDial.setLabel("SPEED");
        dashboard.add(speedDial);

        // add the petrol Dial
        petrolDial = new DialPanel();
        petrolDial.setLabel("PETROL");
        petrolDial.setValue(100);
        dashboard.add(petrolDial);

        // add the petrol Bar
        petrolBar = new BarPanel();
        petrolBar.setLabel("PETROL");
        petrolBar.setValue(100);
        dashboard.add(petrolBar);
        dashboard.pack();
        
        // add the knots Dial
        knotsDial = new DialPanel();
        knotsDial.setLabel("KNOTS");
        dashboard.add(knotsDial);

        // add the knots Bar
        knotsBar = new BarPanel();
        knotsBar.setLabel("KNOTS");
        knotsBar.setValue(100);
        dashboard.add(knotsBar);
        dashboard.pack();
        
        // add the gears Dial
        gearsDial = new DialPanel();
        gearsDial.setLabel("GEARS");
        gearsDial.setValue(100);
        dashboard.add(gearsDial);

        // add the gears Dial
        gearsBar = new BarPanel();
        gearsBar.setLabel("GEARS");
        gearsBar.setValue(100);
        dashboard.add(gearsBar);
        dashboard.pack();
        
        


        // centre the dashboard frame above the control frame
        Point topLeft = this.getLocationOnScreen(); // top left of control frame (this)
        int hControl = this.getHeight(); // height of control frame (this)
        int wControl = this.getWidth(); // width of control frame (this)
        int hDash = dashboard.getHeight(); // height of dashboard frame 
        int wDash = dashboard.getWidth(); // width of dashboard frame 
        // calculate where top left of the dashboard goes to centre it over the control frame
        Point p2 = new Point((int) topLeft.getX() - (wDash - wControl) / 2, (int) topLeft.getY() - (hDash + hControl));
        dashboard.setLocation(p2);
        dashboard.setVisible(true);
    }
    

    /**
     * Run the XML script file which generates events for the dashboard
     * indicators
     */
    private void runXMLScript() {
        try {
            DashboardEventGeneratorFromXML dbegXML = new DashboardEventGeneratorFromXML();

            // Register for speed events from the XML script file
            DashBoardEventListener dbelSpeed = new DashBoardEventListener() {
                @Override
                public void processDashBoardEvent(Object originator, DashBoardEvent dbe) {
                    speedDial.setValue(Integer.parseInt(dbe.getValue()));
                }
            };
            dbegXML.registerDashBoardEventListener("speed", dbelSpeed);

            
            
            // Register for petrol events from the XML script file
            DashBoardEventListener dbelPetril = new DashBoardEventListener() {
                @Override
                public void processDashBoardEvent(Object originator, DashBoardEvent dbe) {
                    petrolDial.setValue(Integer.parseInt(dbe.getValue()));
                    petrolBar.setValue(Integer.parseInt(dbe.getValue()));
                }
            };
            dbegXML.registerDashBoardEventListener("petrol", dbelPetril);

            // Process the script file - it willgenerate events as it runs
            dbegXML.processScriptFile(XML_SCRIPT);

                    // Register for Knots events from the XML script file
            DashBoardEventListener dbelKnots = new DashBoardEventListener() {
                @Override
                public void processDashBoardEvent(Object originator, DashBoardEvent dbe) {
                    knotsDial.setValue(Integer.parseInt(dbe.getValue()));
                    knotsBar.setValue(Integer.parseInt(dbe.getValue()));
                }
            };
            
            dbegXML.registerDashBoardEventListener("knots", dbelKnots);

            // Process the script file - it willgenerate events as it runs
            dbegXML.processScriptFile(XML_SCRIPT);
           
            
                    // Register for Gears events from the XML script file
            DashBoardEventListener dbelGears = new DashBoardEventListener() {
                @Override
                public void processDashBoardEvent(Object originator, DashBoardEvent dbe) {
                    gearsDial.setValue(Integer.parseInt(dbe.getValue()));
                    gearsBar.setValue(Integer.parseInt(dbe.getValue()));
                }
            };
            dbegXML.registerDashBoardEventListener("gears", dbelGears);

            // Process the script file - it willgenerate events as it runs
            dbegXML.processScriptFile(XML_SCRIPT);

        } catch (Exception ex) {
            Logger.getLogger(DashboardDemoMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * Set the speed value to the value entered in the textfield.
     */
    public void setSpeed() {
        try {
            int value = Integer.parseInt(txtSpeedValueInput.getText().trim());
            speedDial.setValue(value);
        } catch (NumberFormatException e) {
        }
        // don't set the speed if the input can't be parsed
    }

    /**
     * Set the petrol value to the value entered in the textfield.
     */
    public void setPetrol() {
        try {
            int value = Integer.parseInt(txtPetrolValueInput.getText().trim());
            petrolDial.setValue(value);
            petrolBar.setValue(value);
        } catch (NumberFormatException e) {
        }
        // don't set the speed if the input can't be parsed
    }
    
    public void setGears() {
        try {
            int value = Integer.parseInt(txtGearsValueInput.getText().trim());
            gearsDial.setValue(value);
            gearsBar.setValue(value);
        } catch (NumberFormatException e) {
        }
        // don't set the speed if the input can't be parsed
    }
    
    
        public void setKnots() {
        try {
            int value = Integer.parseInt(txtKnotsValueInput.getText().trim());
            knotsDial.setValue(value);
            knotsBar.setValue(value);
        } catch (NumberFormatException e) {
        }
        // don't set the speed if the input can't be parsed
    }



    /**
     * Respond to user input in the Speed textfield
     */
    private class SpeedValueListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent event) {
            setSpeed();
        }

        @Override
        public void removeUpdate(DocumentEvent event) {
            setSpeed();
        }

        @Override
        public void changedUpdate(DocumentEvent event) {
        }
    }
    
    

    /**
     * Respond to user input in the Petrol textfield
     */
    private class PetrolValueListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent event) {
            setPetrol();
        }

        @Override
        public void removeUpdate(DocumentEvent event) {
            setPetrol();
        }

        @Override
        public void changedUpdate(DocumentEvent event) {
        }
    }
    
    
    

        private class GearsValueListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent event) {
            setGears();
        }

        @Override
        public void removeUpdate(DocumentEvent event) {
            setGears();
        }

        @Override
        public void changedUpdate(DocumentEvent event) {
        }
    }
        
        
        
        
    private class KnotsValueListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent event) {
            setKnots();
        }

        @Override
        public void removeUpdate(DocumentEvent event) {
            setKnots();
        }

        @Override
        public void changedUpdate(DocumentEvent event) {
        }
    }

    
    
    /**
     *
     * @param args - unused
     */
    public static void main(String[] args) {
        final DashboardDemoMain me = new DashboardDemoMain();
    }
}
