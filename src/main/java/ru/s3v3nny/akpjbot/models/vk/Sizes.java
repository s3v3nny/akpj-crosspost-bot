package ru.s3v3nny.akpjbot.models.vk;

public class Sizes {
    public int height;
    public String url;
    public String type;
    public int width;
    public int resolution;

    public void setResolution(int width, int height) {
        this.resolution = width * height;
    }

    public int getResolution() {
        return resolution;
    }

}
