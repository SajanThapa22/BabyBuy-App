package com.example.babybuy.DataModels;

public class AuthDatamodel {

    public int idauth;
    public String fname;
    public String lname;
    public String dbaseemail;
    String password;
    byte[] imgprofile;
    public byte[] getImgprofile() {
        return imgprofile;
    }
    public void setImgprofile(byte[] imgprofile) {
        this.imgprofile = imgprofile;
    }
    public int getIdauth() {
        return idauth;
    }
    public void setIdauth(int idauth) {
        this.idauth = idauth;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public String getDbaseemail() {
        return dbaseemail;
    }
    public void setDbaseemail(String dbaseemail) {
        this.dbaseemail = dbaseemail;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
