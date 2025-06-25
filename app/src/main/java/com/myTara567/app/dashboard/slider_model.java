package com.myTara567.app.dashboard;

public class slider_model {
    public String image_id;
    public String slider_image;

    public slider_model() {
    }

    public slider_model(String image_id, String slider_image) {
        this.image_id = image_id;
        this.slider_image = slider_image;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getSlider_image() {
        return slider_image;
    }

    public void setSlider_image(String slider_image) {
        this.slider_image = slider_image;
    }
}
