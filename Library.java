import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Lớp này chứa tất cả các thông tin liên quan đến người dùng,
// tài nguyene và các yêu cầu trong thư viện
public class Library implements LibraryInfo {
    // Biến lưu tên thư viên
    String libraryName;
    // Khai báo một đối tượng Calendar toàn cục, dùng để thao tác với ngày giờ
    public static Calendar calendar = new GregorianCalendar();

    // Đối tượng thư viện chính. Chỉ có một đối tượng này được khởi tạo duy nhất.
    static Library lib;

    static int nextResID = 1001;        // Mã tài nguyên tiếp theo sẽ được gán. Đảm bảo ID là duy nhất.
    static int nextUserID = 14100180;   // Mã người dùng tiếp theo sẽ được gán. Đảm bảo ID là duy nhất.
    static int nextFineID = 0;		    // Mã tiền phạt tiếp theo sẽ được gán.

    // Tập hợp các khoản phạt cần kiểm tra bởi thư viện.
    // Chỉ liên quan đến những tài nguyên đã được mượn (issued) bởi người dùng.
    Set<Fine> toBeFined;

    // Danh sách lưu trữ tất cả tài nguyên (Sách, Gói khóa học, Tạp chí) trong thư viện.
    public ArrayList<LibraryResource> resources;

    // Danh sách lưu trữ tất cả người dùng của hệ thống thư viện (Sinh viên, Giảng viên, Quản trị viên).
    public ArrayList<LibraryUser> users;

    // Constructor của lớp Library
    private Library(String name) {
        // Lấy thời gian hiện tại từ hệ thống.
        calendar = Calendar.getInstance();
        // Gán tên thư viện từ tham số đầu vào.
        this.libraryName = name;
        // Khởi tạo tập hợp các khoản phạt chưa kiểm tra.
        toBeFined = new HashSet<Fine>();

        // Khởi tạo các danh sách tài nguyên và người dùng (bao gồm cả quản trị viên).
        resources = new ArrayList<LibraryResource>();
        users = new ArrayList<LibraryUser>();

        // Tạo một đối tượng quản trị viên mặc định với tên đăng nhập "admin" và mật khẩu "admin123".
        Admin admin = new Admin("admin", "admin123", Constants.ADMIN, true);
        users.add(admin);  // Thêm người dùng quản trị viên vào danh sách người dùng của thư viện.

        // Default student
        // Student student = new Student("student1", "student123");
        // users.add(student);
    }

    // Phương thức để đọc người dùng và tài nguyên từ tệp tin
    public void loadLibraryFromFile(String filename) {
        Library lib = Library.getInstance("LUMS Library");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            // Đọc từng dòng trong file
            while ((line = br.readLine()) != null) {
                // Tách dòng theo dấu phẩy ","
                String[] parts = line.split(", ");

                // Kiểm tra xem dòng có đủ 3 phần hay không (User/Resource, Name, Type)
                if (parts.length >= 3) {

                    if (parts[0].equals("User")) {
                        // Parse thông tin người dùng
                        String type = parts[1];
                        String name = parts[2];
                        String pass = parts[3];

                        if (type.equals("Student")) {
                            // Tạo người dùng Student và tăng ID
                            Student student = new Student(name, pass);
                            student.userID = Library.nextUserID;  // Gán ID cho student
                            Library.nextUserID++;  // Tăng ID tiếp theo cho người dùng
                            lib.users.add(student);  // Thêm vào danh sách người dùng

                        } else if (type.equals("Faculty")) {
                            // Tạo người dùng Faculty và tăng ID
                            Faculty faculty = new Faculty(name, pass);
                            faculty.userID = Library.nextUserID;  // Gán ID cho faculty
                            Library.nextUserID++;  // Tăng ID tiếp theo cho người dùng
                            lib.users.add(faculty);  // Thêm vào danh sách người dùng

                        } else if (type.equals("Admin")) {
                            // Tạo người dùng Admin và tăng ID
                            Admin admin = new Admin(name, pass, Constants.ADMIN, true);
                            admin.userID = Library.nextUserID;  // Gán ID cho admin
                            Library.nextUserID++;  // Tăng ID tiếp theo cho người dùng
                            lib.users.add(admin);  // Thêm vào danh sách người dùng
                        }

                    } else if (parts[0].equals("Resource")) {
                        // Parse thông tin tài nguyên
                        String type = parts[1];
                        String title = parts[2];
                        int resID = (parts.length > 3) ? Integer.parseInt(parts[3]) : 0; // Nếu có resID, lấy nó, nếu không thì gán giá trị mặc định

                        if (type.equals("Book")) {
                            // Tạo tài nguyên Book và tăng ID
                            Book book = new Book(title, resID);
                            book.resourceID = Library.nextResID;  // Gán ID cho Book
                            Library.nextResID++;  // Tăng ID tiếp theo cho tài nguyên
                            lib.resources.add(book);  // Thêm vào danh sách tài nguyên

                        } else if (type.equals("CoursePack")) {
                            // Tạo tài nguyên CoursePack và tăng ID
                            CoursePack coursePack = new CoursePack(title, resID);
                            coursePack.resourceID = Library.nextResID;  // Gán ID cho CoursePack
                            Library.nextResID++;  // Tăng ID tiếp theo cho tài nguyên
                            lib.resources.add(coursePack);  // Thêm vào danh sách tài nguyên

                        } else if (type.equals("Magazine")) {
                            // Tạo tài nguyên Magazine và tăng ID
                            Magazine magazine = new Magazine(title, resID);
                            magazine.resourceID = Library.nextResID;  // Gán ID cho Magazine
                            Library.nextResID++;  // Tăng ID tiếp theo cho tài nguyên
                            lib.resources.add(magazine);  // Thêm vào danh sách tài nguyên
                        }
                    }

                } else {
                    // In thông báo nếu dòng không hợp lệ
                    System.out.println("Invalid line: " + line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức này đảm bảo chỉ có một đối tượng duy nhất của lớp `Library` được tạo ra.
    public static Library getInstance(String name){
        // Kiểm tra nếu đối tượng `lib` chưa được khởi tạo
        if (lib == null){
            // Khởi tạo đối tượng `Library` mới nếu chưa có
            lib = new Library(name);
        }
        return lib;
    }

    public String getLibraryName(){
        return this.libraryName;    // Trả về tên thư viện
    }

    public void getLibraryStats(){
        // Khởi tạo các biến đếm số lượng người dùng theo loại
        int admin=0,faculty=0,students=0;

        // Duyệt qua danh sách người dùng để đếm số lượng quản trị viên (admin), giảng viên (faculty), và sinh viên (students)
        for(int i = 0; i < users.size(); i++) {
            // Kiểm tra loại người dùng và tăng số lượng tương ứng
            if(users.get(i).type == Constants.ADMIN)
                admin++;
            else if(users.get(i).type == Constants.FACULTY)
                faculty++;
            else
                students++;
        }

        // Khởi tạo các biến đếm tài nguyên (sách, tạp chí, khóa học) và các tài nguyên đã mượn
        int books = 0, magazines = 0, course_packs = 0;
        int issued = 0, overdue = 0, request = 0;
        Borrowable borrowable;  // Biến này sẽ được dùng để lưu trữ các tài nguyên có thể mượn

        // Lấy ngày hiện tại từ hệ thống
        Date date = calendar.getTime();

        // Duyệt qua danh sách tài nguyên và tính toán các thống kê liên quan đến tài nguyên
        for(int i = 0; i < resources.size(); i++) {
            // Kiểm tra loại tài nguyên là sách, khóa học hay tạp chí
            if(resources.get(i).type == Constants.BOOK) {
                books++;  // Đếm số lượng sách
                borrowable = (Borrowable) resources.get(i);  // Chuyển kiểu sang Borrowable để kiểm tra trạng thái mượn
                if(borrowable.requests.size() > 0)  // Kiểm tra nếu có yêu cầu mượn
                    request++;
                if(borrowable.getReturnDate1() != null && date.compareTo(borrowable.getReturnDate1()) > 0)  // Kiểm tra tài nguyên quá hạn
                    overdue++;
                if(!borrowable.checkStatus())  // Kiểm tra tài nguyên có đang được mượn hay không
                    issued++;
            }
            else if(resources.get(i).type == Constants.COURSE_PACK){
                course_packs++;     // Đếm số lượng khóa học
                borrowable = (Borrowable)resources.get(i);
                if(borrowable.requests.size() > 0)
                    request++;
                if(borrowable.getReturnDate1() != null && date.compareTo(borrowable.getReturnDate1()) > 0)
                    overdue++;
                if(!borrowable.checkStatus())
                    issued++;
            }
            else
                magazines++;        // Đếm số lượng tạp chí
        }
        // In ra các thống kê về thư viện
        System.out.println("\t\t\t\t*** Library System Stats ***\n\nNo. of Users:\t" + users.size());
        System.out.println("\tAdministrators: " + admin + "\n\tFaculty: " + faculty + "\n\tStudents: " + students);
        System.out.println("\nNumber of Resources:\n\tBooks: " + books + "\n\tCourse Packs: " + course_packs + "\n\tMagazines: " + magazines);
        System.out.println("\nNumber of Borrowed Resources:\n\tIssued: " + issued + "\n\tOverdue: " + overdue + "\n\tRequest: " + request);
    }


    /*******************************************/
    /*****Library users search functions********/
    /*******************************************/

    public LibraryUser findUser(String name){
        // Kiểm tra tất cả người dùng theo tên người dùng (userName)
        for(int i = 0; i < this.users.size(); i++) {
            if(users.get(i).userName.equals(name)) {
                return users.get(i);  // Trả về người dùng nếu tên trùng khớp
            }
        }
        return null;  // Trả về null nếu không tìm thấy người dùng
    }

    public LibraryUser findUser(int userID){
        // Kiểm tra tất cả người dùng theo mã người dùng (userID)
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).userID == userID) {
                return users.get(i);  // Trả về người dùng nếu mã trùng khớp
            }
        }
        return null;  // Trả về null nếu không tìm thấy người dùng
    }
    /*******************************************/


    /*******************************************/
    /*******Library user removal function*******/
    /*******************************************/
    boolean removeUser(int userID){

        // Kiểm tra xem người dùng có tài nguyên nào không...
        // Nếu có, phương thức sẽ giả định người dùng đã mượn tất cả tài nguyên và sẽ xóa tất cả tài nguyên khỏi thư viện.
        LibraryUser user = findUser(userID);

        // Kiểm tra nếu không tìm thấy người dùng (user là null)
        if (user == null) {
            return false;  // Trả về false nếu người dùng không tồn tại trong hệ thống
        }

        // Kiểm tra nếu người dùng không phải là quản trị viên (ADMIN)
        if(user.type != Constants.ADMIN){
            Borrower borrower = (Borrower)user;  // Ép kiểu người dùng thành Borrower (người mượn tài nguyên)
            // Duyệt qua các tài nguyên đã mượn và xóa chúng khỏi thư viện
            for(int i = 0; i < borrower.issuedResources.size(); i++){
                lib.removeResource(borrower.issuedResources.get(i));  // Xóa tài nguyên khỏi thư viện
            }
        }

        // Duyệt qua danh sách người dùng và tìm người dùng có userID tương ứng
        for(int i = 0; i < this.users.size(); i++){
            // Nếu tìm thấy người dùng có userID khớp, tiến hành xóa người dùng khỏi danh sách
            if(users.get(i).userID == userID){
                users.remove(i);  // Loại bỏ người dùng khỏi danh sách
                return true;  // Trả về true nếu người dùng đã được xóa thành công
            }
        }
        return false;  // Trả về false nếu không tìm thấy người dùng trong danh sách
    }
    /********************************************/


    /*******************************************/
    /*******Resource Search Functions***********/
    /*******************************************/

    public LibraryResource findResource(int resID){
        // Tìm tài nguyên theo resourceID
        for(int i = 0; i < resources.size(); i++){
            // Kiểm tra xem tài nguyên tại vị trí i có ID trùng với resID không
            if(resources.get(i).resourceID == resID){
                return resources.get(i);  // Nếu trùng, trả về tài nguyên đó
            }
        }
        return null;  // Nếu không tìm thấy tài nguyên với ID tương ứng, trả về null
    }


    ArrayList<LibraryResource> findResource(String resName){
        // Tìm tài nguyên theo tên resourceName
        ArrayList<LibraryResource> list = new ArrayList<LibraryResource>();  // Khởi tạo một danh sách chứa các tài nguyên có tên khớp
        for(int i = 0; i < resources.size(); i++){
            // Kiểm tra xem tên tài nguyên tại vị trí i có khớp với tên tìm kiếm không
            if(resources.get(i).resourceName.equals(resName)){
                list.add(resources.get(i));  // Nếu trùng, thêm tài nguyên vào danh sách kết quả
            }
        }
        return list;  // Trả về danh sách các tài nguyên có tên khớp
    }
    /*******************************************/


    /*******************************************/
    /*********Resource Removal Function*********/
    /*******************************************/
    boolean removeResource(int resourceID) {
        // Tìm tài nguyên theo resourceID
        LibraryResource res = findResource(resourceID);

        // Kiểm tra tài nguyên có tồn tại không
        if (res == null) {
            System.out.println("Resource with ID " + resourceID + " not found.");
            return false;  // Tài nguyên không tồn tại, trả về false
        }

        // Tiến hành xử lý tài nguyên (ví dụ: xóa các yêu cầu, cập nhật người mượn, v.v.)
        if (res.type != Constants.MAGAZINE) {
            Borrowable bor = (Borrowable) res;
            for (int i = 0; i < bor.requests.size(); i++) {
                // Xóa các yêu cầu của người mượn
                Borrower borrower = (Borrower) findUser(bor.requests.get(i));
                if (borrower != null) {
                    borrower.deleteRequest(resourceID);
                }
            }
            if (bor.checkStatus()) {
                Borrower borrower = (Borrower) findUser(bor.issuedTo);
                if (borrower != null) {
                    borrower.tryReturn(bor.getResourceID());
                }
            }
        }

        // Xóa tài nguyên khỏi danh sách resources
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i).resourceID == resourceID) {
                resources.remove(i);
                return true;  // Xóa thành công
            }
        }
        return false;  // Nếu không tìm thấy tài nguyên trong danh sách
    }


    /*******************************************/


    /*****************************************/
    /************ To Update Fines ************/
    /*****************************************/

    public void updateFines() {
        // Cập nhật phạt bằng cách kiểm tra danh sách cần xử lý (toBeFined).
        Date today = Library.calendar.getTime();
        int amount = 0, days = 0;
        Borrower borrower;
        Borrowable borrowable;

        for (Fine fine : toBeFined) {
            LibraryResource res = findResource(fine.resourceID);
            if (res == null) {
                removeToBeFined(fine);
                continue;
            }
            borrowable = (Borrowable) res;

            // Kiểm tra nếu ngày trả lại (dueDate) không phải là null
            if (borrowable.getReturnDate1() != null) {
                if (today.compareTo(borrowable.getReturnDate1()) > 0) {
                    borrower = (Borrower) findUser(fine.userID);
                    days = today.getDate() - borrowable.getReturnDate1().getDate();

                    if (borrowable.type == Constants.BOOK) {
                        amount = days * 100;
                        fine.updateFine(amount);
                    } else if (borrowable.type == Constants.COURSE_PACK) {
                        amount = days * 500;
                        fine.updateFine(amount);
                    }
                }
            }
        }
    }


    boolean removeToBeFined(Fine fine){

        // Loại bỏ khoản phạt khỏi danh sách toBeFined nếu tài nguyên đã được trả lại
        if(toBeFined.contains(fine)){
            toBeFined.remove(fine);  // Xóa khoản phạt khỏi danh sách
            return true;  // Trả về true nếu xóa thành công
        }
        else
            return false;  // Trả về false nếu không tìm thấy khoản phạt trong danh sách
    }

    boolean addToBeFined(Fine fine){

        //adds Fine to toBeChecked collection
        if(!toBeFined.contains(fine)){
            toBeFined.add(fine);
            return true;
        }
        else
            return false;
    }
}
