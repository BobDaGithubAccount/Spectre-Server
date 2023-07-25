package api.events;

import lib.json.JSONObject;
import networking.NetworkWorkerThread;

public class Event {
    public NetworkWorkerThread nwt;
    public JSONObject json;

    public Event(NetworkWorkerThread nwt, JSONObject json) {
        this.nwt = nwt;
        this.json = json;
    }
}
