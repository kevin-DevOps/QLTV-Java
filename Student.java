import java.util.*;
// Lớp Student kế thừa từ lớp Borrower, đại diện cho một sinh viên mượn tài nguyên
public class Student extends Borrower {

    // Constructor của lớp Student, khởi tạo sinh viên với tên và mật khẩu
    Student(String name, String pass) {
        this.userName = name;               // Gán tên người dùng
        this.password = pass;               // Gán mật khẩu
        this.type = Constants.STUDENT;      // Thiết lập loại người dùng là sinh viên (Student)
        this.maxResources = 3;              // Sinh viên có thể mượn tối đa 3 tài nguyên
        this.fines = new HashSet<Fine>();   // Khởi tạo bộ chứa khoản phạt
    }
}
