package com.tac.guns.item.attachment.impl;

public class ScopeZoomData {
    private float zoomMultiple;
    private float drCropZoom;
    private float zoomZTransition = 0.0f;

    public ScopeZoomData(float zoomMultiple, float drCropZoom) {
        this.zoomMultiple = zoomMultiple;
        this.drCropZoom = drCropZoom;
    }

    public ScopeZoomData(float zoomMultiple, float drCropZoom, float zoomZTransition) {
        this.zoomMultiple = zoomMultiple;
        this.drCropZoom = drCropZoom;
        this.zoomZTransition = zoomZTransition;
    }

    public float getZoomMultiple() {
        return zoomMultiple;
    }

    public float getDrCropZoom() {
        return drCropZoom;
    }

    public float getZoomZTransition() {
        return zoomZTransition;
    }
}
