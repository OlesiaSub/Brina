package org.hse.brina.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Document {
    private final StringBuilder name;
    private final StringBuilder image = new StringBuilder("/org/hse/brina/assets/unlocked.png");
    private STATUS status;

    public enum STATUS{
        LOCKED,
        UNLOCKED
    }

    public Document(String name,  STATUS status) {
        this.name = new StringBuilder(name);
        this.status = status;
    }

    public String getName() {
        return name.toString();
    }

    public String getImage() {
        return image.toString();
    }

    public STATUS getStatus(){
        return status;
    }

    public void setName(String newName) {
        name.replace(0, name.length(), newName);
    }

    public void setStatus(String statusValue){
        if (statusValue.equals("unlocked")) {
            status = STATUS.UNLOCKED;
        } else {
            status = STATUS.LOCKED;
        }
    }

    public void updateImage(){
        if (status == STATUS.LOCKED){
            image.replace(0, image.length(),"/org/hse/brina/assets/locked.png");
        } else{
            image.replace(0, image.length(),"/org/hse/brina/assets/unlocked.png");
        }
    }
}
