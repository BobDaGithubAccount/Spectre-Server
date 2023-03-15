package gamelogic.event;

public class Plugin {
    private String name;
    private String mainClass;
    private String author;
    private String version;
    private String spectre_version;
    private String description;

    public Plugin(String name, String mainClass, String author, String version, String spectre_version, String description) {
        this.name = name;
        this.mainClass = mainClass;
        this.author = author;
        this.version = version;
        this.spectre_version = spectre_version;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSpectre_version() {
        return spectre_version;
    }

    public void setSpectre_version(String spectre_version) {
        this.spectre_version = spectre_version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
