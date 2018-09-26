package com.example.demo.to;

import java.io.Serializable;

public class Perception implements Serializable {

    private InputText inputText;

    private InputImage inputImage;

    private InputMedia inputMedia;

    private SelfInfo selfInfo;

    public static class InputText {
        private String text = "";

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class InputImage {
        private String url = "";

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class InputMedia {
        private String url = "";

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public InputText getInputText() {
        return inputText;
    }

    public void setInputText(InputText inputText) {
        this.inputText = inputText;
    }

    public InputImage getInputImage() {
        return inputImage;
    }

    public void setInputImage(InputImage inputImage) {
        this.inputImage = inputImage;
    }

    public InputMedia getInputMedia() {
        return inputMedia;
    }

    public void setInputMedia(InputMedia inputMedia) {
        this.inputMedia = inputMedia;
    }

    public SelfInfo getSelfInfo() {
        return selfInfo;
    }

    public void setSelfInfo(SelfInfo selfInfo) {
        this.selfInfo = selfInfo;
    }
}
