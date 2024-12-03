import java.util.*;
// Lớp Faculty kế thừa từ lớp Borrower, đại diện cho một giảng viên mượn tài nguyên
public class Faculty extends Borrower {

    // Constructor của lớp Faculty, khởi tạo giảng viên với tên và mật khẩu
    Faculty(String name, String pass) {
        this.userName = name;                   // Gán tên người dùng
        this.password = pass;                   // Gán mật khẩu
        this.type = Constants.FACULTY;          // Thiết lập loại người dùng là giảng viên (Faculty)
        this.maxResources = 6;                  // Giảng viên có thể mượn tối đa 6 tài nguyên
        this.fines = new HashSet<Fine>();       // Khởi tạo bộ chứa khoản phạt
    }
}
