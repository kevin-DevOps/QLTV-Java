
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

// Giao diện Borrower phải được thực hiện bởi lớp Sinh viên và Khoa

public class Borrower extends LibraryUser {

    Set<Fine> fines; // Các điều khoản phạt

    Vector<Integer> requestedResources = new Vector<Integer>();	// Chứa ID của các tài nguyên đã yêu cầu!

    Vector<Integer> issuedResources = new Vector<Integer>();	//	Chứa ID của các Tài nguyên đã được cấp!

    int maxResources;

    //sẽ in các khoản tiền phạt trên màn hình cho người dùng hiện tại
    public void viewFines() {

        if (fines.size() == 0) {
            System.out.println("There are no fines.");
            return;
        }
        int totalFine = 0, i = 1;
        System.out.println("Following is the list of fines:\n\nNo. ---- resourceID ---- Fine");
        for (Fine fine : fines) {
            if (fine.fine == 0) {
                continue;
            }
            System.out.println((i) + ". ---- " + fine.resourceID + " ---- " + fine.fine);
            totalFine += fine.fine;
            i++;
        }

        System.out.println("Total fine = Rs. " + totalFine);
    }

    //sẽ in tất cả các yêu cầu đang chờ xử lý mà người dùng này đã thực hiện, các yêu cầu có thể dành cho nhiều tài nguyên
    public void viewRequests() {

        if (requestedResources.size() == 0) {
            System.out.println("There are no requests pending!");
        }

        LibraryResource res;
        Library lib = Library.getInstance("LUMS Library");
        System.out.println("Following are the pending requests:\n\nNo.\tResource --- ResourceID");
        for (int i = 0; i < requestedResources.size(); i++) {
            res = lib.findResource(requestedResources.get(i));
            System.out.println((i + 1) + ".\t" + res.resourceName + " --- " + res.resourceID);
        }
    }

    //in ra màn hình tên và ID của tất cả các tài nguyên mà người dùng này đã được cấp
    public void viewIssued() {
        if (issuedResources.size() == 0) {
            System.out.println("There are no resources issued!\n");
            return;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Borrowable res;
        Library lib = Library.getInstance("LUMS Library");
        System.out.println("Following are the issued resources:\n\nNo.\tResourceID --- Resource --- Issue Date --- Due Date");
        for (int i = 0; i < issuedResources.size(); i++) {
            res = (Borrowable) (lib.findResource(issuedResources.get(i)));
            System.out.println("\n" + (i + 1) + ".\t" + res.resourceID + " --- " + res.resourceName + " --- " + dateFormat.format(res.issueDate) + " --- " + res.getReturnDate());
        }
    }

    //xem ngày cấp cho tất cả các tài nguyên mà người dùng hiện tại đã được cấp
    public void viewIssueDates() {
        if (issuedResources.size() == 0) {
            System.out.println("There are no resources issued!\n");
            return;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Borrowable res;
        Library lib = Library.getInstance("LUMS Library");
        System.out.println("Following are the issued resources:\n\nNo.\tResourceID --- Resource --- Issue Date");
        for (int i = 0; i < issuedResources.size(); i++) {
            res = (Borrowable) (lib.findResource((int) issuedResources.get(i)));
            System.out.println("\n" + (i + 1) + ".\t" + res.resourceID + " --- " + res.resourceName + " --- " + res.getIssueDate());
        }
    }

    //xem ngày đến hạn cho tất cả các tài nguyên mà người dùng hiện tại đã được cấp
    //ngày đến hạn phải sau ngày cấp 15 ngày đối với sinh viên và 30 ngày đối với giảng viên
    public void viewDueDates() {
        if (issuedResources.size() == 0) {
            System.out.println("There are no resources issued!\n");
            return;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Borrowable res;
        Library lib = Library.getInstance("LUMS Library");
        System.out.println("Following are the issued resources:\n\nNo.\tResourceID --- Resource --- Issue Date --- Due Date");
        for (int i = 0; i < issuedResources.size(); i++) {
            res = (Borrowable) (lib.findResource((int) issuedResources.get(i)));
            System.out.println("\n" + (i + 1) + ".\t" + res.resourceID + " --- " + res.resourceName + " --- " + res.getReturnDate());
        }
    }

    //in tên và ID của tất cả các tài nguyên do người dùng này cấp hiện đang quá hạn (ngày trả đã qua)
    public void viewOverdue() {
        if (issuedResources.size() == 0) {
            System.out.println("There are no resources issued!\n");
            return;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Library.calendar;
        Date date = cal.getTime();
        Borrowable res;
        Library lib = Library.getInstance("LUMS Library");
        boolean check = true;
        for (int i = 0; i < issuedResources.size(); i++) {
            res = (Borrowable) (lib.findResource((int) issuedResources.get(i)));
            if (date.compareTo(res.getReturnDate1()) > 0) {
                if (check) {
                    System.out.println("Following are the issued resources:\n\nNo.\tResourceID --- Resource --- Issue Date --- Due Date");
                    check = false;
                }
                System.out.println("\n" + (i + 1) + ".\t" + res.resourceID + " --- " + res.resourceName + " --- " + res.getReturnDate());
            }
        }
        if (check) {
            System.out.println("There are no resources overdue!\n\n");
        }
    }

    /*
     * sẽ thử cấp tài nguyên có ID tài nguyên đã cho cho người dùng hiện tại.
     * không nên đưa ra yêu cầu nếu cùng một người đã cấp sách Ghi nhớ: để kiểm
     * tra tình trạng khả dụng, hàm này sẽ gọi phương thức checkAvailability()
     * trong giao diện Borrowable để thực sự cấp phát tài nguyên, hàm này sẽ gọi
     * phương thức issueResource() trong giao diện Borrowable
     *
     * @param resourceID cung cấp id tài nguyên của Sách/Gói đọc cần được cấp
     * @return nếu tài nguyên khả dụng, hãy cấp nó cho người dùng và trả về
     * true, nếu không, hãy tạo yêu cầu và trả về false
     */
    public boolean tryIssue(ArrayList<LibraryResource> res) {
        Library lib = Library.getInstance("LUMS Library");

        if (res == null || res.size() == 0) {
            System.out.println("There is no resource with this name or ID.");
            return false;
        }
        Borrowable borrowable = null;
        boolean check = false;
        int id = -1;
        for (int i = 0; i < res.size(); i++) {
            if (res.get(i).type == Constants.MAGAZINE) {
                System.out.println("A Magazine cannot be issued!");
                return false;
            }
            borrowable = (Borrowable) res.get(i);
            if (borrowable.issuedTo == this.userID) {
                System.out.println("The requested resource is already issued to you! Don't cheat:p");
                return false;
            }

            if (maxResources <= issuedResources.size() || !borrowable.checkStatus()) {

                if (maxResources <= issuedResources.size()) {
                    System.out.println("Your limit of issued resources is reached! Sorry cannot issue the resource!");
                } else {
                    System.out.println("The requested resource to another Library user.");
                }
                check = true;
            } else {
                check = false;
                id = borrowable.getResourceID();
                break;
            }
        }
        if (check && borrowable != null) {
            for (int j = 0; j < requestedResources.size(); j++) {
                if ((int) requestedResources.elementAt(j) == res.get(0).getResourceID()) {
                    System.out.println("A request has already been placed!");
                    return false;
                }
            }
            borrowable.requests.addElement(this.userID);
            requestedResources.addElement(res.get(0).getResourceID());
            System.out.println("Request has been added successfully");
            return false;
        }
        if (borrowable != null) {
            borrowable.issueResource(this.userID);
            this.issuedResources.addElement(id);
            Fine fine = new Fine(id, this.userID, 0);
            lib.addToBeFined(fine);
            borrowable.setRelatedFine(fine.fineID);
            fines.add(fine);
            return true;
        }

        return false;
    }

    /**
     *
     * @param resourceID   cung cấp id tài nguyên của Sách/Gói đọc cần được trả
     * lại
     * @return sẽ cố gắng trả lại tài nguyên có ID đã cho, nếu thành công, trả
     * về 1, nếu không trả về false một lần nữa, để kiểm tra xem tài nguyên có
     * được cấp phát cho người dùng cụ thể này hay không, bạn sẽ gọi phương thức
     * checkIssuer() của giao diện có thể mượn được để trả lại tài nguyên, bạn
     * phải gọi phương thức returnResource() của giao diện Borrowable
     *
     * ngoài ra, khi trả lại, hãy kiểm tra tiền phạt nếu mặt hàng được trả lại
     * sau ngày đến hạn
     */
    public boolean tryReturn(int resourceID) {

        Library lib = Library.getInstance("LUMS Library");
        LibraryResource res = lib.findResource(resourceID);
        Calendar cal = Library.calendar;
        Date d = cal.getTime();
        if (res == null) {
            return false;
        }
        if (res.type == Constants.MAGAZINE) {
            return false;
        }

        Borrowable borrowable = (Borrowable) res;
        if (borrowable.issuedTo != this.userID) {
            return false;
        }
        borrowable.returnResource();
        int relatedFineID = borrowable.getRelatedFine();
        for (Fine fine : fines) {
            if (fine.fineID == relatedFineID) {
                lib.removeToBeFined(fine);
                if (fine.fine == 0) {					// Nếu không có tiền phạt, thì hãy xóa đối tượng này <Fine>

                    fines.remove(fine);
                }
            }
        }
        for (int i = 0; i < issuedResources.size(); i++) {
            if ((int) issuedResources.elementAt(i) == resourceID) {
                issuedResources.remove(i);
                return true;
            }
        }
        return true;
    }

    // resourceID duy nhất của Sách/Gói khóa học mà chúng tôi muốn rút yêu cầu đang chờ xử lý
    // nếu yêu cầu tồn tại, hãy rút yêu cầu đó và trả về 1, nếu yêu cầu không tồn tại, hãy trả về false
    public boolean withdrawRequest(int resourceID) {

        Library lib = Library.getInstance("LUMS Library");
        LibraryResource res = lib.findResource(resourceID);
        if (res == null) {
            return false;
        }
        if (res.type != Constants.MAGAZINE) {
            Borrowable bor = (Borrowable) res;
            bor.removeRequest(this.userID);
            for (int i = 0; i < this.requestedResources.size(); i++) {
                if ((int) this.requestedResources.elementAt(i) == resourceID) {
                    this.requestedResources.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteRequest(int resID) {
        for (int i = 0; i < requestedResources.size(); i++) {
            if (requestedResources.get(i) == resID) {
                requestedResources.remove(i);
                return;
            }
        }
    }

    // Gia hạn tài nguyên cho người dùng
    public boolean tryRenew(int resID) {
        Library lib = Library.getInstance("LUMS Library");
        LibraryResource res = lib.findResource(resID);

        if (res == null) {
            return false;
        } else if (res.type == Constants.MAGAZINE || res.type == Constants.COURSE_PACK) {
            return false;
        }
        Book borrowable = (Book) res;
        if (borrowable.issuedTo != this.userID) {
            return false;
        }

        int relatedFineID = borrowable.getRelatedFine();
        for (Fine fine : fines) {
            if (fine.fineID == relatedFineID) {
                lib.removeToBeFined(fine);
                if (fine.fine == 0) {

                    fines.remove(fine);
                }
            }
        }
        Fine fine = new Fine(resID, this.userID, 0);
        lib.addToBeFined(fine);
        fines.add(fine);
        borrowable.setRelatedFine(fine.fineID);
        if (type == Constants.FACULTY) {
            return borrowable.renewResource(30);
        } else {
            return borrowable.renewResource(15);
        }
    }

    public boolean findIssued(int id) {
        for (int i = 0; i < issuedResources.size(); i++) {
            if (issuedResources.get(i) == id) {
                return true;
            }
        }
        return false;
    }
}
