import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * This interface should be implemented by the Book and CoursePack classes
 *
 */
public class Borrowable extends LibraryResource{

    Vector<Integer> requests = new Vector<Integer>();
    Date issueDate;
    Date dueDate;
    int issuedTo;
    int relatedFineID;

    boolean available;


    // nên in danh sách các yêu cầu đang chờ xử lý đã được thực hiện đối với tài nguyên đã cho
    void viewRequests(){
        if(requests.size() == 0){
            System.out.println("There are no pending requests!");
        }
        else{
            String sp = "\t\t\t";
            System.out.println("Following are the pending requests for this resource:\n\nNo."+sp+"userID");
            for(int i=0;i<requests.size();i++){
                System.out.println((i+1)+"."+sp+ (int)requests.elementAt(i));
            }
        }
    }

    // kiểm tra xem mục đã cho có sẵn trong thư viện không
    // @return sẽ trả về 0 nếu tài nguyên có sẵn, trả về 1 nếu được phát hành (không có sẵn)
    boolean checkStatus(){
        return available;
    }

    /*
     * sẽ cấp tài nguyên có ID đã cho cho người dùng có ID đã cho
     * @param userID id duy nhất của người dùng mà muốn cấp tài nguyên
     * @return trả về true nếu thành công nếu không trả về false
     */
    boolean issueResource(int userID){
        return true;
    }

    /*
     * trả lại tài nguyên cho thư viện
     * LƯU Ý: nếu có yêu cầu đang chờ xử lý đối với tài nguyên này, hãy tự động cấp nó cho người yêu cầu đầu tiên
     * @return trả về true nếu thành công nếu không trả về false
     */
    boolean returnResource(){


        if(requests.size() > 0){
            Library lib = Library.getInstance("LUMS Library");
            Borrower borrower =  (Borrower)(lib.findUser((int)requests.elementAt(0)) );
            requests.remove(0);
            borrower.issuedResources.addElement(this.resourceID);
            this.issueResource(borrower.userID);
            borrower.withdrawRequest(this.resourceID);
        }
        else{
            issueDate = null;
            dueDate = null;
            available = true;
            issuedTo = -1;
        }
        return true;
    }

    /*
     * đặt ngày phát hành của tài nguyên thành ngày đã cho, định dạng ngày là mm/dd/yyyy
     * @param ngày ngày ở định dạng mm/dd/yyyy
     */

    void setIssueDate(String date){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try{
            issueDate = dateFormat.parse(date);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    // @return trả về ngày phát hành của tài nguyên. trả về chuỗi trống không được phát hành
    String getIssueDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(issueDate);
    }

    // @return trả về ngày trả về của tài nguyên. trả về chuỗi trống không được phát hành

    String getReturnDate() {
        if (dueDate == null) {
            // Nếu dueDate là null, trả về một thông báo mặc định hoặc ngày hiện tại
            return "No due date available"; // Hoặc bạn có thể trả về ngày hiện tại nếu muốn
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(dueDate);
    }

    //*****************************//

    Date getReturnDate1(){
        if(dueDate == null)
            return null;
        else
            return dueDate;
    }

    Date getIssueDate1(){
        if(issueDate == null)
            return null;
        else
            return issueDate;
    }

    void setIssueDate(Date date){
        issueDate = date;
    }

    void setRelatedFine(int fineID){
        this.relatedFineID = fineID;
    }

    int getRelatedFine(){
        return relatedFineID;
    }

    boolean removeRequest(int userID){
        for(int i=0;i<requests.size();i++){
            if(requests.get(i) == userID){
                requests.remove(i);
                return true;
            }
        }
        return false;
    }
}
