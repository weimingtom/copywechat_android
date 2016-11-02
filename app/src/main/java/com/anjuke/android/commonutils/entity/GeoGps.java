package com.anjuke.android.commonutils.entity;

/**
 * 定义自己的坐标类，不使用google或高德等定义的类。供以后扩展
 */
public class GeoGps {
    Double lat = 0D;
    Double lng = 0D;

    public GeoGps() {
    }

    public GeoGps(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        
        if (!(o instanceof GeoGps)) {
            return false;
        }

        GeoGps geoGps = (GeoGps) o;
        if( this.lat.equals(geoGps.getLat()) && this.lng.equals(geoGps.getLng()) ){
            return true;
        }else{
            return false;
        }
    }
}