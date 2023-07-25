package api.events;

import lib.json.JSONObject;
import networking.NetworkWorkerThread;

public class Event {
    private NetworkWorkerThread nwt;
    private JSONObject json;

    public Event(NetworkWorkerThread nwt, JSONObject json) {
        this.nwt = nwt;
        this.json = json;
    }

    public NetworkWorkerThread getNwt() {
        return nwt;
    }

    public void setNwt(NetworkWorkerThread nwt) {
        this.nwt = nwt;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
}
