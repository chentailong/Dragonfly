package net.along.dragonflyfm.record;

import com.orm.SugarRecord;

/**
 * 创建者 by:陈泰龙
 * <p> 最受欢迎的节目
 * 2020/8/16
 **/

public class ProgramGreet extends SugarRecord {
    private int programId;
    //id
    private String programName;
    //名称
    private int count;
    //次数
    private long timeStamp;


    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "ProgramGreet{" +
                "programId=" + programId +
                ", programName='" + programName + '\'' +
                ", count=" + count +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
