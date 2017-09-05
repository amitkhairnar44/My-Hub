package soft.art.myhub.model;

/**
 * Created by TUSHAR-AMIT on 02-Sep-17.
 */

public class NoticeModel {

    String facultyname;
    String noticeDate;
    String notice;

    public NoticeModel()
    {

    }

    public void setFacultyname(String facultyname) {
        this.facultyname = facultyname;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getFacultyname() {
        return facultyname;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public String getNotice() {
        return notice;
    }
}
