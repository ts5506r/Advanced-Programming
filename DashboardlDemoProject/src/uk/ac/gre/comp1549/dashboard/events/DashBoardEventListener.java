package uk.ac.gre.comp1549.dashboard.events;

/**
 * Interface implemented by classes that want to be notified of DashBoardEvents
 * @author COMP1549
 */
public interface DashBoardEventListener {
    public void processDashBoardEvent(Object originator, DashBoardEvent dbe);
    
}
