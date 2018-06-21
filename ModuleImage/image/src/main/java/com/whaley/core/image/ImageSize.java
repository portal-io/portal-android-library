package com.whaley.core.image;

public  class ImageSize{
        private String url;
        private int width;
        private int height;
        private boolean isMax;

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getWidth() {
            return width;
        }

        public void setMax(boolean max) {
            isMax = max;
        }

        public boolean isMax() {
            return isMax;
        }
    }