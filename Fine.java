// Lớp Fine đại diện cho khoản tiền phạt đối với người dùng khi trả tài nguyên trễ
public class Fine {

    // Các thuộc tính của lớp Fine
    int fineID;      // ID của khoản tiền phạt
    int resourceID;  // ID tài nguyên mà người dùng đã mượn
    int userID;      // ID người dùng bị phạt
    int fine;        // Số tiền phạt phải trả

    // Constructor khởi tạo một khoản tiền phạt mới
    /* Khởi tạo một khoản tiền phạt cho tài nguyên và người dùng nhất định.
     * Tạo một khoản tiền phạt với ID tài nguyên, ID người dùng và số tiền phạt cho trước.
     * @param _resID ID tài nguyên bị trả trễ
     * @param userID ID người dùng bị phạt
     * @param _fine số tiền phạt
     */
    Fine(int _resID, int userID, int _fine){
        fineID = Library.nextFineID; // Gán ID tiền phạt từ biến toàn cục trong thư viện
        Library.nextFineID++; // Tăng ID tiền phạt để sử dụng cho khoản phạt tiếp theo
        this.resourceID = _resID; // Gán ID tài nguyên
        this.fine = _fine; // Gán số tiền phạt
        this.userID = userID; // Gán ID người dùng
    }

    // Phương thức cập nhật số tiền phạt
    /* Cập nhật số tiền phạt của tài nguyên đối với người dùng.
     * @param fine số tiền phạt mới
     */
    void updateFine(int fine){
        this.fine = fine; // Cập nhật số tiền phạt
    }
}
