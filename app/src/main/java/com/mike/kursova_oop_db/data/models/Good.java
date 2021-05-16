package com.mike.kursova_oop_db.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Good extends BaseDBModel implements Comparable<Good>, Parcelable {
    public static String TABLE_NAME = "goods";
    public static String NAME = "name";
    public static String VENDOR_CODE = "v_code";
    public static String DESCRIPTION = "description";
    public static String PHOTO = "photo";
    public static String PRICE = "price";
    public static String CATEGORY_ID = "category";
    public static String CATEGORY_NAME = "cat_name";
    public static String COUNT = "count";
    public static String ACTIVE = "active";

    private String name="";
    private String vendorCode="";
    private String photoURI="";//URL в реальной базе
    private double price=0;
    private String description="";
    private long catId=-1;
    private String catName="";
    private int count= 0;
    private Boolean active = false;
    private List<Review> reviews;

    public Good() {    }

    public Good(String name, String vendorCode, String photoURI, double price, String description, long catId, int count, Boolean active) {
        this.name = name;
        this.vendorCode = vendorCode;
        this.photoURI = photoURI;
        this.price = price;
        this.description = description;
        this.catId = catId;
        this.count = count;
        this.active = active;
    }

    protected Good(Parcel in) {
        name = in.readString();
        vendorCode = in.readString();
        photoURI = in.readString();
        price = in.readDouble();
        description = in.readString();
        catId = in.readLong();
        catName = in.readString();
        count = in.readInt();
        byte tmpActive = in.readByte();
        active = tmpActive == 0 ? null : tmpActive == 1;
    }

    public static final Creator<Good> CREATOR = new Creator<Good>() {
        @Override
        public Good createFromParcel(Parcel in) {
            return new Good(in);
        }

        @Override
        public Good[] newArray(int size) {
            return new Good[size];
        }
    };

    @NotNull
    public String toString() {
        return String.format("name:%s, code:%s [%s, %s] (id:%s)", this.name, this.vendorCode,  this.price, this.count, this.id);
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCatId() {
        return catId;
    }

    public void setCatId(long catId) {
        this.catId = catId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int compareTo(Good o) {
        return Double.compare(getPrice(), o.getPrice());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(vendorCode);
        dest.writeString(photoURI);
        dest.writeDouble(price);
        dest.writeString(description);
        dest.writeLong(catId);
        dest.writeString(catName);
        dest.writeInt(count);
        dest.writeByte((byte) (active == null ? 0 : active ? 1 : 2));
    }
}
