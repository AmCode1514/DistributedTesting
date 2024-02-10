package node;

import java.util.Deque;
import java.util.LinkedList;

public class EventHandler extends Thread {
    //This class functions as a singleton event handler, the run method continually checks 
    //the data structures contained in NodeData objects, and executes the required functions.
    // Additionally, this class uses the event factory to build a basic event from byte arrays.
    private final EventFactory factory;
    private final NodeData nodeData;
    private Deque<Event> eventList;
    private EventHandler(NodeData nodeData) {
        //one eventHandler to every server and one event factory per handler.
        factory = EventFactory.getInstance();
        this.nodeData = nodeData;
        eventList = new LinkedList<Event>();
    }
    //instead of just returning the event this method will also add to the eventHandlers NodeData object. 
    //this method is synchronized essentially as an add method, the run method is synchronized at start but will wait
    //releasing the lock and allowing other classes to add events for processing, and then signaling the eventHandler to begin again. This means
    //adding and processing do not occur concurrently since the data structures will not be thread safe.
    public void parseAndAddEvent(byte[] message) {
        //in theory this parsing method in the factory is thread safe, so why not let the receiver threads do the work to maximize concurrency while waiting for the object lock.
        Event newEvent = factory.generateEvent(message);
        //acquire lock on eventList and then add the respective event. All other writes block at line 27
        synchronized(eventList) {
            eventList.addFirst(newEvent);
            //notify passes control of lock back to the thread in the run method.
            eventList.notify();
        }
    }
    //singleton instance getter
    public static EventHandler getInstance(NodeData nodeData) {
        return new EventHandler(nodeData);
    }
    //the runnning point of a thread which processes events.
    public void run() {
        while(true) {
            try {
                synchronized(eventList) {
                    //surrender lock and wait
                    eventList.wait();
                    // while the list isn't empty then process event and remove it from list.
                    while(eventList.size() != 0) {
                        eventList.pop().OnEvent(nodeData);
                    }
                }
            }
            catch(InterruptedException e) {
                System.out.println("If you're seeing this thats a big boo boo. This SHOULD NEVER BE INTERRUPTED.");
                e.printStackTrace();
            }
        }
    }

}
