package com.lc.bangumidemo.Green;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 *     val CREATE_BOOKINDEX = ("create table BookDetailActivity ("
 + "id integer primary key autoincrement, "
 + "author text, "
 + "bookname text, "
 + "hardpageindex interger, "
 + "hardcontentindex interger, "
 + "pagecount integer, "
 + "pageindex interger, "
 + "contentindex interger)"
 )
 */
@Entity
public class LocalBookIndex {
    @Id(autoincrement = true)
    private Long _id;
    private String author;
    private String bookname;
    private int hardpageindex;
    private int hardcontentindex;
    private int pagecount;
    private int pageindex;
    private int contentindex;
    @Generated(hash = 1601774322)
    public LocalBookIndex(Long _id, String author, String bookname,
            int hardpageindex, int hardcontentindex, int pagecount, int pageindex,
            int contentindex) {
        this._id = _id;
        this.author = author;
        this.bookname = bookname;
        this.hardpageindex = hardpageindex;
        this.hardcontentindex = hardcontentindex;
        this.pagecount = pagecount;
        this.pageindex = pageindex;
        this.contentindex = contentindex;
    }
    @Generated(hash = 696668263)
    public LocalBookIndex() {
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
    public int getHardpageindex() {
        return this.hardpageindex;
    }
    public void setHardpageindex(int hardpageindex) {
        this.hardpageindex = hardpageindex;
    }
    public int getHardcontentindex() {
        return this.hardcontentindex;
    }
    public void setHardcontentindex(int hardcontentindex) {
        this.hardcontentindex = hardcontentindex;
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
    public int getContentindex() {
        return this.contentindex;
    }
    public void setContentindex(int contentindex) {
        this.contentindex = contentindex;
    }

}
