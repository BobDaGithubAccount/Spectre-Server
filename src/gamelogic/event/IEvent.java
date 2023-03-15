package gamelogic.event;

import lib.json.JSONObject;
import networking.NetworkWorkerThread;

public interface IEvent {

    public String name();
    public boolean run(JSONObject json, NetworkWorkerThread nwt);
}
