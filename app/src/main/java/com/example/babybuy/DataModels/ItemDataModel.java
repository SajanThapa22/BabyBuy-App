package com.example.babybuy.DataModels;

public class ItemDataModel {


    Integer iditem;
    String nameitem;
    Integer quanitem;
    Double priceitem, latitem, longitem;
    String descripitem;
    Integer statusitem;
    byte[] imgitem;
    Integer idcatimg;
    String addressitem;

    public String getAddressitem() {
        return addressitem;
    }

    public void setAddressitem(String addressitem) {
        this.addressitem = addressitem;
    }
    public Integer getIditem() {
        return iditem;
    }
    public void setIditem(Integer iditem) {
        this.iditem = iditem;
    }
    public String getNameitem() {
        return nameitem;
    }
    public void setNameitem(String nameitem) {
        this.nameitem = nameitem;
    }
    public Integer getQuanitem() {
        return quanitem;
    }
    public void setQuanitem(Integer quanitem) {
        this.quanitem = quanitem;
    }
    public Double getPriceitem() {
        return priceitem;
    }
    public void setPriceitem(Double priceitem) {
        this.priceitem = priceitem;
    }
    public Double getLatitem() {
        return latitem;
    }
    public void setLatitem(Double latitem) {
        this.latitem = latitem;
    }
    public Double getLongitem() {
        return longitem;
    }
    public void setLongitem(Double longitem) {
        this.longitem = longitem;
    }
    public String getDescripitem() {
        return descripitem;
    }
    public void setDescripitem(String descripitem) {
        this.descripitem = descripitem;
    }
    public Integer getStatusitem() {
        return statusitem;
    }
    public void setStatusitem(Integer statusitem) {
        this.statusitem = statusitem;
    }
    public byte[] getImgitem() {
        return imgitem;
    }
    public void setImgitem(byte[] imgitem) {
        this.imgitem = imgitem;
    }
    public Integer getIdcatimg() {
        return idcatimg;
    }
    public void setIdcatimg(Integer idcatimg) {
        this.idcatimg = idcatimg;
    }
}