package api.command;

public class CommandResponse {

    private String response;
    private boolean valid;

    public CommandResponse(String response, boolean valid) {
        this.response = response;
        this.valid = valid;
    }

    public CommandResponse(boolean valid) {
        this.response = "";
        this.valid = valid;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
