// Lớp Admin kế thừa từ lớp LibraryUser và triển khai giao diện AdminInterface,
// đại diện cho quản trị viên của thư viện, có quyền thêm, xóa người dùng và tài nguyên
public class Admin extends LibraryUser implements AdminInterface {
    // Phương thức thêm người dùng mới vào thư viện
    @Override
    public int addUser(String userName, String password, int type) {
        Library lib = Library.getInstance("LUMS");

        // Kiểm tra xem tên người dùng đã tồn tại chưa
        if(lib.findUser(userName) != null) {
            System.out.println("This username already exists! Choose Another one.");
            return -1; // Trả về -1 nếu tên người dùng đã tồn tại
        }

        int result = -1;
        // Thêm người dùng dựa trên loại
        if(type == Constants.ADMIN) {
            Admin admin = new Admin(userName, password, type, false);
            result = admin.userID;
            lib.users.add(admin);
        }
        else if(type == Constants.STUDENT) {
            Student student = new Student(userName, password);
            student.userID = Library.nextUserID;
            Library.nextUserID++;
            result = student.userID;
            lib.users.add(student);
        }
        else if(type == Constants.FACULTY) {
            Faculty faculty = new Faculty(userName, password);
            faculty.userID = Library.nextUserID;
            Library.nextUserID++;
            result = faculty.userID;
            lib.users.add(faculty);
        }

        return result; // Trả về ID của người dùng mới nếu thêm thành công
    }

    // Phương thức xóa người dùng khỏi thư viện
    @Override
    public boolean removeUser(int userID) {
        Library lib = Library.getInstance("LUMS Library");
        LibraryUser user = lib.findUser(userID);
        if(user == null) {
            return false; // Nếu không tìm thấy người dùng, trả về false
        }

        // Xóa người dùng khỏi thư viện
        return lib.removeUser(userID); // Trả về true nếu xóa thành công
    }

    // Phương thức thêm tài nguyên vào thư viện
    @Override
    public int addResource(String name, int type) {
        Library lib = Library.getInstance("LUMS Library");
        int result = -1;

        // Thêm tài nguyên dựa trên loại
        if(type == Constants.BOOK) {
            Book book = new Book(name, Library.nextResID);
            lib.resources.add(book); // Thêm sách vào danh sách tài nguyên
            result = book.resourceID;
        }
        else if(type == Constants.COURSE_PACK) {
            CoursePack pack = new CoursePack(name, Library.nextResID);
            lib.resources.add(pack); // Thêm khóa học vào danh sách tài nguyên
            result = pack.resourceID;
        }

        return result; // Trả về ID của tài nguyên mới nếu thêm thành công
    }

    // Phương thức xóa tài nguyên dựa trên ID
    @Override
    public boolean removeResource(int resourceID) {
        Library lib = Library.getInstance("LUMS Library");
        return lib.removeResource(resourceID);
    }


    // Constructor cho Admin
    Admin(String user,String pass, int typ,boolean firstInstance){

        this.userName = user;
        this.password = pass;
        this.type = typ;
        this.userID = Library.nextUserID;
        Library.nextUserID++;
    }
}
