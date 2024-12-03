public class LibraryUser {
    // Đặt các thuộc tính là public để có thể truy cập từ Driver
    public String userName;
    public String password;
    public int userID;
    public int type;

    // Phương thức lấy ID người dùng
    public int getUserID(){
        return userID;
    }

    // Phương thức đăng nhập
    public boolean login(String userName, String password) {
        if ((userName.equals(this.userName) && password.equals(this.password))) {
            return true;
        } else {
            System.out.println("The username or password entered was not correct!");
            return false;
        }
    }

    // Phương thức đăng xuất
    public boolean logout() {
        return true;
    }

}
