package BeepoVacAttack.BeepoVacServer;
import BeepoVacAttack.Networking.Caller;
import BeepoVacAttack.Networking.Packet;

import java.util.LinkedList;

public class Observer {

    LinkedList<Caller> callers;

    public Observer() {
        this.callers = new LinkedList<Caller>();
    }

    public void add(Caller caller) {
        this.callers.add(caller);
    }

    // ensure that it is a packet
    public void send(Object message) {
        for (Caller caller : this.callers) {
            caller.push(((Packet) message));
        }
    }
}
