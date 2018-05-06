package sdu.cs.pichsinee.findfriend.utility;

import android.os.Parcel;
import android.os.Parcelable;

public class UserMode implements Parcelable {   //เป็นการสร้าง Model เพื่อโยนข้อมูลเป็น Model ขึ้ร Firebase เนื่องจาก Firebase ส่งข้อมูลตรงๆ ไม่ได้

    private String nameString, pathAvatarString;

    //Constructor Getter รับค่า
    public UserMode() {

    }

    //Constructor Setter เอาค่าไปใช้
    public UserMode(String nameString, String pathAvatarString) {
        this.nameString = nameString;
        this.pathAvatarString = pathAvatarString;

    }

    protected UserMode(Parcel in) {
        nameString = in.readString();
        pathAvatarString = in.readString();
    }

    public static final Creator<UserMode> CREATOR = new Creator<UserMode>() {
        @Override
        public UserMode createFromParcel(Parcel in) {
            return new UserMode(in);
        }

        @Override
        public UserMode[] newArray(int size) {
            return new UserMode[size];
        }
    };

    //Constructor Getter & Setter
    public String getNameString() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public String getPathAvatarString() {
        return pathAvatarString;
    }

    public void setPathAvatarString(String pathAvatarString) {
        this.pathAvatarString = pathAvatarString;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nameString);
        parcel.writeString(pathAvatarString);
    }

}//end Class
