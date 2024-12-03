import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

// Class CoursePack keeys thừa từ lớp Borrowable, đại diện cho một tài nguyên được mượn
public class CoursePack extends Borrowable {
    // Phương thức cấp tài nguyên Course Pack cho người dùng có user ID
    boolean issueResource(int userID) {
        Library lib = Library.getInstance("LUMS Library");
        LibraryUser user = lib.findUser(userID);
        int daysToIssue;

        //đặt giới hạn ngày cho từng đối tượng
        if (user.type == Constants.FACULTY) {
            daysToIssue = 30;
        } else if (user.type == Constants.STUDENT) {
            daysToIssue = 15;
        } else {
            return false;
        }

        issuedTo = userID;
        available = false;
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_YEAR, 0);
        issueDate = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, daysToIssue);
        dueDate = cal.getTime();
        System.out.println(issueDate);
        return true;
    }

    // Constructor của lớp CoursePack

    public CoursePack(String resName, int resID) {
        this.resourceName = resName;            // Thiết lập tên tài nguyên
        this.resourceID = resID;                // Thiết ập ID tài nguyên
        this.available = true;                  // ĐDdasnh dấu tài nguyên có sẵndđể mượn
        this.issuedTo = -1;                     // Không có người mượn ban đầu
        this.type = Constants.COURSE_PACK;      // Đặt loại tài nguyên là Course Pack
    }
}
