package com.example.itime;

public class SelectButton {
    private String name;
    private String detail;
    private int imageId;

    public SelectButton(String name, String detail, int imageId) {
        this.name = name;
        this.detail = detail;
        this.imageId = imageId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
