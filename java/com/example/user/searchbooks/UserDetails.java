package com.example.user.searchbooks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetails implements Parcelable,Serializable{


    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("lastName")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("contact")
    @Expose
    public String contact;
    @SerializedName("adress")
    @Expose
    public String adress;
    @SerializedName("password")
    @Expose
    public String password;


    public UserDetails()
    {
        uid = "";
        firstName = "";
        lastName = "";
        email = "";
        password = "";
        contact="";
        adress="";

    }

    public UserDetails(String firstName, String lastName, String email,String contact,String adress,String password)
    {
       this.uid=uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adress=adress;
        this.email=email;
        this.contact=contact;
        this.password=password;




    }

    protected UserDetails(Parcel in)
    {
        uid = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        contact=in.readString();
        adress=in.readString();
        password = in.readString();

        //address = (Address)in.readParcelable(Address.class.getClassLoader());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(firstName);
        dest.writeString(lastName);

        dest.writeString(email);
        dest.writeString(contact);
        dest.writeString(adress);
        dest.writeString(password);

    }

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };

}
