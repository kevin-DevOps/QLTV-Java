// Giao diện AdminInterface, phải được triển khai bởi lớp Admin để quản lý người dùng và tài nguyên trong thư viện
public interface AdminInterface {

    // Tạo người dùng mới với tên người dùng và mật khẩu cho trước.
    int addUser(String userName, String password, int type);

    // Xóa người dùng khỏi hệ thống.
    boolean removeUser(int userID);

    // Thêm tài nguyên mới vào thư viện với tên và loại tài nguyên cho trước.
    int addResource(String name, int type);

    // Xóa tài nguyên khỏi hệ thống thư viện.
    boolean removeResource(int resourceID);
}
