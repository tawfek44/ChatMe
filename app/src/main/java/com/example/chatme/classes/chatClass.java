package com.example.chatme.classes;

import android.net.Uri;

public class chatClass {
    private String fID,fName;
    private Uri fImage;

    public chatClass(String fID, String fName, Uri fImage) {
        this.fID = fID;
        this.fName = fName;
        this.fImage = fImage;
    }

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public Uri getfImage() {
        return fImage;
    }

    public void setfImage(Uri fImage) {
        this.fImage = fImage;
    }
}

