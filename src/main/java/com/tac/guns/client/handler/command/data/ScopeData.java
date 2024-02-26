package com.tac.guns.client.handler.command.data;

public class ScopeData {
    private float additionalZoomMod;
    private float centerOffsetMod;
    private float stabilityOffsetMod;
    private double viewFinderOffsetMod;
    private double viewFinderOffsetSpecial;

    private float DrZoomCropMod;
    private float DrZoomSizeMod;
    private double DrXZoomMod;
    private double DrYZoomMod;
    private double DrZZoomMod;

    private float ReticleSizeMod;
    private double ReticleXMod;
    private double ReticleYMod;
    private double ReticleZMod;
    private String tagName;

    public ScopeData(String tagName) {
        this.additionalZoomMod = 0;
        this.centerOffsetMod = 0;
        this.stabilityOffsetMod = 0;
        this.viewFinderOffsetMod = 0;
        this.viewFinderOffsetSpecial = 0;
        this.DrZoomCropMod = 0;
        this.DrZoomSizeMod = 0;
        this.DrXZoomMod = 0;
        this.DrYZoomMod = 0;
        this.DrZZoomMod = 0;
        this.ReticleSizeMod = 0;
        this.ReticleXMod = 0;
        this.ReticleYMod = 0;
        this.ReticleZMod = 0;
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public float getReticleSizeMod() {
        return ReticleSizeMod;
    }

    public void setReticleSizeMod(float reticleSizeMod) {
        ReticleSizeMod = reticleSizeMod;
    }

    public double getReticleXMod() {
        return ReticleXMod;
    }

    public void setReticleXMod(double reticleXMod) {
        ReticleXMod = reticleXMod;
    }

    public double getReticleYMod() {
        return ReticleYMod;
    }

    public void setReticleYMod(double reticleYMod) {
        ReticleYMod = reticleYMod;
    }

    public double getReticleZMod() {
        return ReticleZMod;
    }

    public void setReticleZMod(double reticleZMod) {
        ReticleZMod = reticleZMod;
    }

    public float getDrZoomCropMod() {
        return DrZoomCropMod;
    }

    public void setDrZoomCropMod(float drZoomCropMod) {
        DrZoomCropMod = drZoomCropMod;
    }

    public float getDrZoomSizeMod() {
        return DrZoomSizeMod;
    }

    public void setDrZoomSizeMod(float drZoomSizeMod) {
        DrZoomSizeMod = drZoomSizeMod;
    }

    public double getDrXZoomMod() {
        return DrXZoomMod;
    }

    public void setDrXZoomMod(double drXZoomMod) {
        DrXZoomMod = drXZoomMod;
    }

    public double getDrYZoomMod() {
        return DrYZoomMod;
    }

    public void setDrYZoomMod(double drYZoomMod) {
        DrYZoomMod = drYZoomMod;
    }

    public double getDrZZoomMod() {
        return DrZZoomMod;
    }

    public void setDrZZoomMod(double drZZoomMod) {
        DrZZoomMod = drZZoomMod;
    }

    public float getAdditionalZoomMod() {
        return additionalZoomMod;
    }

    public void setAdditionalZoomMod(float additionalZoomMod) {
        this.additionalZoomMod = additionalZoomMod;
    }

    public float getCenterOffsetMod() {
        return centerOffsetMod;
    }

    public void setCenterOffsetMod(float centerOffsetMod) {
        this.centerOffsetMod = centerOffsetMod;
    }

    public float getStabilityOffsetMod() {
        return stabilityOffsetMod;
    }

    public void setStabilityOffsetMod(float stabilityOffsetMod) {
        this.stabilityOffsetMod = stabilityOffsetMod;
    }

    public double getViewFinderOffsetMod() {
        return viewFinderOffsetMod;
    }

    public void setViewFinderOffsetMod(double viewFinderOffsetMod) {
        this.viewFinderOffsetMod = viewFinderOffsetMod;
    }

    public double getViewFinderOffsetSpecial() {
        return viewFinderOffsetSpecial;
    }

    public void setViewFinderOffsetSpecial(double viewFinderOffsetSpecial) {
        this.viewFinderOffsetSpecial = viewFinderOffsetSpecial;
    }

    @Override
    public int hashCode() {
        return (int) (Math.PI * (Math.random() * 10));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ScopeData && tagName == ((ScopeData) obj).tagName;
    }
}