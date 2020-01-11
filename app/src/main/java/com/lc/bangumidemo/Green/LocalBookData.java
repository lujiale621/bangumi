package com.lc.bangumidemo.Green;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LocalBookData {
    @Id(autoincrement = true)
    private Long _id;

    private String author;
    private String bookname;
    private String content;
    private int contentsize;
    private int pagecount;
    private int pageindex;
    @Generated(hash = 1426493021)
    public LocalBookData(Long _id, String author, String bookname, String content,
            int contentsize, int pagecount, int pageindex) {
        this._id = _id;
        this.author = author;
        this.bookname = bookname;
        this.content = content;
        this.contentsize = contentsize;
        this.pagecount = pagecount;
        this.pageindex = pageindex;
    }
    @Generated(hash = 483747958)
    public LocalBookData() {
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
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getContentsize() {
        return this.contentsize;
    }
    public void setContentsize(int contentsize) {
        this.contentsize = contentsize;
    }
    public int getPagecount() {
        return this.pagecount;
    }
    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }
    public int getPageindex() {
        return this.pageindex;
    }
    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
    }

}
