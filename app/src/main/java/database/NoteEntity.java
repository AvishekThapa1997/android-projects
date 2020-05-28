package database;

import android.os.Parcel;
import android.os.Parcelable;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notes")
public class NoteEntity implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;
    private String content;

    public NoteEntity(int id, Date date, String content) {
        this.id = id;
        this.date = date;
        this.content = content;
    }

    @Ignore
    public NoteEntity(Date date, String content) {
        this.date = date;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeString(this.content);
    }

    protected NoteEntity(Parcel in) {
        this.id = in.readInt();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.content = in.readString();
    }

    public static final Parcelable.Creator<NoteEntity> CREATOR = new Parcelable.Creator<NoteEntity>() {
        @Override
        public NoteEntity createFromParcel(Parcel source) {
            return new NoteEntity(source);
        }

        @Override
        public NoteEntity[] newArray(int size) {
            return new NoteEntity[size];
        }
    };
}
