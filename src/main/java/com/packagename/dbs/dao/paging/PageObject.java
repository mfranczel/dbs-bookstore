package com.packagename.dbs.dao.paging;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class PageObject {
    private long limit;
    private long offset;
    private long end;

    public PageObject() {
        this.limit = 20;
        this.offset = 0;
    }

    public void next(){
        if(this.offset + this.limit >= this.end){
            this.offset = end - this.limit;
        }
        else {
            this.offset += this.limit;
        }
    }

    public void prev(){
        if(this.offset - this.limit < 0){
            this.offset = 0;
        }
        else {
            this.offset -= this.limit;
        }
    }

    public long getLimit() {
        return this.limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
