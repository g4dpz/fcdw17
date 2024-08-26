package uk.me.g4dpz.fcdwrawdata.domain;

public class Satellite {

    private String name;
    private String url;
    private String imageUrl;
    private Boolean active;

    public Satellite() {
    }

    public Satellite(String name, String url, String imageUrl, Boolean active) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
