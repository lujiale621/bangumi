package com.lc.bangumidemo.Green;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LocalBookReadClass {
    @Id(autoincrement = true)
    private Long _id;
    
    private String author;
    private String bookname;
    private int pagecount;
    private String bookdata;
    private int pageindex;
    private int contentindex;
    private int start;
    private int end;
    private int indexx;
    @Generated(hash = 1770986041)
    public LocalBookReadClass(Long _id, String author, String bookname,
            int pagecount, String bookdata, int pageindex, int contentindex,
            int start, int end, int indexx) {
        this._id = _id;
        this.author = author;
        this.bookname = bookname;
        this.pagecount = pagecount;
        this.bookdata = bookdata;
        this.pageindex = pageindex;
        this.contentindex = contentindex;
        this.start = start;
        this.end = end;
        this.indexx = indexx;
    }
    @Generated(hash = 1534428871)
    public LocalBookReadClass() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getBookname() {
        return this.bookname;
    }
    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
    public int getPagecount() {
        return this.pagecount;
    }
    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }
    public String getBookdata() {
        return this.bookdata;
    }
    public void setBookdata(String bookdata) {
        this.bookdata = bookdata;
    }
    public int getPageindex() {
        return this.pageindex;
    }
    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
    }
    public int getContentindex() {
        return this.contentindex;
    }
    public void setContentindex(int contentindex) {
        this.contentindex = contentindex;
    }
    public int getStart() {
        return this.start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getEnd() {
        return this.end;
    }
    public void setEnd(int end) {
        this.end = end;
    }
    public int getIndexx() {
        return this.indexx;
    }
    public void setIndexx(int indexx) {
        this.indexx = indexx;
    }

}
