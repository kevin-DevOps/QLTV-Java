import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

// Lớp Book kế thừa từ lớp Borrowable, đại diện cho một tài nguyên mượn được (Sách)
public class Book extends Borrowable{
    // Phương thức cấp tài nguyên sách cho người dùng với userID
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

    // Phương thức gia hạn tài nguyên (sách)
    boolean renewResource(int days){
        // Kiểm tra nếu khoogn có yêu cầu chờ tài nguyên
        if(requests.isEmpty()){
            Calendar cal = Calendar.getInstance();  // Tạo đối tượng Calendar để làm việc với ngày tháng
            // gia hạn thời gian mượn thêm số ngày cho trước
            cal.add(Calendar.DAY_OF_YEAR, days);
            dueDate = cal.getTime();    // Cập nhật ngày hết hạn

            // In ra ngày hết hạn ới
            System.out.println("The new due date is: " + getReturnDate());
            return true;    // Trả về true khi gia hạn thành công
        }
        else {
            return false;   // Nếu có yêu cầu chờ, không ther gia hạn
        }
    }

    // Constructor của lớp Book
    Book(String resName, int resID){
        this.resourceID = resID;        // Khởi tạo ID tài nguyên
        this.resourceName = resName;    // Khởi tạo tên tài nguyên
        this.available = true;          // Đánh dấu tài nguyên có sẵn để mượn
        this.issuedTo = -1;             // Đánh dấu tài nguyên chưa được mượn
        this.type = Constants.BOOK;     // Đặt loại tài nguyên là BOOK
    }
}
