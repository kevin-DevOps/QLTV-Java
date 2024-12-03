import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



public class Driver{

//Driver điều khiển hệ thống thư viện. là một IO Interface sử dụng và control hệ thống

    Scanner sc;					// 	Đ scan user input khi khởi chạy trong main
    Library myLibrary;		//	thể hiện của library khi khổi chạy trong hàm Main


    //Constructor cho Driver
    Driver(String libName){
        sc = new Scanner(System.in);
        myLibrary = Library.getInstance(libName);
    }
    /*****************************************/
    /******** USER Login IO Interface ********/
    /*****************************************/
    void loginIO(int type){

        String name,pass;
        // Template options cho Amdin Interface
        String template = "\n\nChoose from the Following options:\n\n" +
                "- For adding an administrator account to the system, press 1\n" + "- For adding a faculty account to the system, press 2\n" +
                "- For adding a student account to the system, press 3\n" + "- For adding a book to the system, press 4\n" +
                "- For adding a course pack to the system, press 5\n" + "- For adding a magazine to the system, press 6\n" +
                "- For removing a user account from the system, press 7\n" + "- For removing a resource from the system, press 8\n" +
                "- For logging out of the system, press 9";


        //Yêu cầu username và password của user
        System.out.println("Enter the username:");
        name = sc.nextLine();
        System.out.println("Enter the password:");
        pass = sc.nextLine();
        LibraryUser user = myLibrary.findUser(name);

        // Kiểm tra tìm user và thông báo
        if(user == null){
            System.out.println("The username or password was not correct!\n");
            return;
        }
        // Nếu user login fails
        else if(!user.login(name,pass)){
            return;
        }

        //Nếu faculty và student đăng nhập đúng username và password, nhưng không phải Admin,
        // Lúc này, bị từ chối
        if(user.type != type){
            System.out.println("The username or password was not correct!");
            return;
        }



        /********** The User Has Logged In ****************/
        boolean done = false;
        int input;
        String userInput;

        if(type == Constants.ADMIN){

            // Cho Admin login Interface

            System.out.println("\n***************************************\n\nWelcome "+ name+ "!" +template);

            while(!done){

                if(!sc.hasNextInt()){
                    userInput = sc.nextLine();
                    continue;
                }
                input = sc.nextInt();

                //hasNextInt() chỉ lấy số nguyên duy nhất, không lấy kí tự \n
                userInput = sc.nextLine();  // Đọc phần còn lại(\n)

                switch(input){
                    case 1:
                        addUserIO((Admin)user,Constants.ADMIN);		// Thêm admin
                        System.out.println(template);
                        break;
                    case 2:
                        addUserIO((Admin)user,Constants.FACULTY);	// Thêm user Faculty
                        System.out.println(template);
                        break;
                    case 3:
                        addUserIO((Admin)user,Constants.STUDENT);	// Thêm user Student
                        System.out.println(template);
                        break;
                    case 4:
                        addResourceIO((Admin)user,Constants.BOOK);	// Thêm tài nguyên Book
                        System.out.println(template);
                        break;
                    case 5:
                        addResourceIO((Admin)user,Constants.COURSE_PACK);	// Thêm tài nguyên Course Pack
                        System.out.println(template);
                        break;
                    case 6:
                        addResourceIO((Admin)user,Constants.MAGAZINE);	// Thêm tài nguyên Magazine
                        System.out.println(template);
                        break;
                    case 7:
                        removeUserIO((Admin)user);		// xóa User
                        System.out.println(template);
                        break;
                    case 8:
                        removeResourceIO((Admin)user);	// Xóa tài nguyên
                        System.out.println(template);
                        break;
                    case 9:
                        System.out.println("Do you really want to log out? y/n");		// Hỏi user nếu logout
                        userInput = sc.nextLine();
                        if((userInput.equals("y") || userInput.equals("Y"))){
                            System.out.println("Thanks... logging out!");
                            user.logout();
                            done = true;
                        }
                        else{
                            System.out.println(user.userName + " still Logged in...");
                        }
                        break;
                    default:
                        System.out.println("Give the correct input");
                        break;
                }
            }
        }
        // Phần cho Faculty and Student Login IO Interface.
        else{
            String template_2 = "\n\nChoose from the following options:\n\n"+
                    "- For borrowing a resource, press 1\n"+
                    "- For returning a resource, press 2\n"+
                    "- For deleting a request, press 3\n"+
                    "- For viewing issued books, press 4\n"+
                    "- For viewing pending requests, press 5\n"+
                    "- For viewing you fines, press 6\n"+
                    "- For logging out of the system, press 7\n"+
                    "- For renewing a resource, press 8\n";
            System.out.println("\n***************************************\n\nWelcome "+ name+ "!" +template_2);

            while(!done){

                if(!sc.hasNextInt()){
                    System.out.println("Give the correct input");
                    userInput = sc.nextLine();		// tránh input không cần thiết
                    continue;
                }
                input = sc.nextInt();

                userInput = sc.nextLine();
                switch(input){

                    case 1:
                        borrowIO((Borrower)user,type);		// borrow Interface
                        System.out.println(template_2);
                        break;
                    case 2:
                        returnResourceIO((Borrower)user);		// trả về resource Interface
                        System.out.println(template_2);
                        break;
                    case 3:
                        deleteRequestIO((Borrower)user);		// delete request Interface
                        System.out.println(template_2);
                        break;
                    case 4:
                        ((Borrower)user).viewIssued();			// views issued Resources bằng cách gọi viewIssued function
                        System.out.println(template_2);
                        break;
                    case 5:
                        ((Borrower)user).viewRequests();		// views requests của user
                        System.out.println(template_2);
                        break;
                    case 6:
                        ((Borrower)user).viewFines();				// xem phạt của user
                        System.out.println(template_2);
                        break;
                    case 8:
                        renewResourceIO((Borrower)user);		// gia hạn tài nguyên
                        System.out.println(template_2);
                        break;
                    case 7:
                        System.out.println("Do you really want to log out? y/n");		// hỏi lại user nếu muốn logout
                        userInput = sc.nextLine();
                        if((userInput.equals("y") || userInput.equals("Y"))){
                            System.out.println("Thanks... logging out!");
                            user.logout();
                            done = true;
                        }
                        else{
                            System.out.println(user.userName + " Still Logged in...");
                        }
                        break;
                    default:
                        System.out.println("Give the correct input");
                        break;
                }
            }
        }
    }
    /*****************************************/


    /*****************************************/
    /********* Add User IO Interface *********/
    /*****************************************/
    void addUserIO(Admin admin,int type){		// chỉ admin mới được nhâ method này

        // function này lấy userName và password của user mới và gọi addUser function của Admin
        String name,pass;

        System.out.println("Enter the new username:");
        name = sc.nextLine();
        System.out.println("Enter the password:");
        pass = sc.nextLine();

        // Biến lưu trữ id của user
        int id = admin.addUser(name,pass,type);
        if(id >= 0){
            System.out.println("The new userID is: " + id + "\n");
        }
        else{
            System.out.println("New user could not be created. Please try again with different username.\n");
        }
    }
    /*****************************************/


    /*****************************************/
    /******* Remove User IO Interface ********/
    /*****************************************/
    void removeUserIO(Admin admin){

        // function này hco ID hoặc userName của user được xóa và gọi removeUser function của Admin
        int id;
        String userInput;
        System.out.println("Enter the userID:");

        while(!sc.hasNextInt()){
            System.out.println("Give the correct integer input...");
            System.out.println("Enter the userID:");
            userInput = sc.nextLine();
            continue;
        }
        id = sc.nextInt();
        userInput = sc.nextLine();
        if(admin.removeUser(id))
        {
            System.out.println("User " + id + " has been successfully removed!");
        }
        else{
            System.out.println("User " + id + "was not removed!");
        }
    }
    /*****************************************/


    /*****************************************/
    /******** IO For Adding Resource *********/
    /*****************************************/

    void addResourceIO(Admin admin, int type){

        // Function này yêu cầu Name của Resource được thêm và gọi addResource function của Admin

        String name;
        System.out.println("Enter the name of Resource:");
        name = sc.nextLine();
        if(admin.addResource(name,type) > -1){
            System.out.println("The resource has been added successfully");
        }
        else{
            System.out.println("The resource could not be added.\nPlease try with another name!");
        }
    }
    /*****************************************/


    /*****************************************/
    /******* IO For Removing Resource ********/
    /*****************************************/
    void removeResourceIO(Admin admin){

        // Hàm này yêu cầu xóa Name hoặc ID của Tài nguyên và gọi hàm RemoveResource của ADMIN...
        ArrayList<LibraryResource> resources = new ArrayList<LibraryResource>();
        String name,userInput;
        int id = -1;
        System.out.println("Enter the Resource Name or ID:");
        if(sc.hasNextInt()){
            id = sc.nextInt();
            userInput = sc.nextLine();
        }
        else{
            name = sc.nextLine();
            if(myLibrary.findResource(name)!=null){
                resources = myLibrary.findResource(name);
            }
        }
        for(int i =0;i<resources.size();i++){
            if(admin.removeResource(resources.get(i).getResourceID())){
                System.out.println("The resource is successfully removed!");
            }
            else{
                System.out.println("The resource is not found/removed");
            }
        }
    }
    /*****************************************/


    /*****************************************/
    /************** Borrow IO ****************/
    /*****************************************/
    void borrowIO(Borrower borrower, int type){

        // Hàm này yêu cầu Name hoặc ID của tài nguyên cần mượn và gọi hàm tryIssue của Borrower.java
        ArrayList<LibraryResource> resources = new ArrayList<LibraryResource>();
        String name;
        int id = -1;
        System.out.println("Enter the name or ID of resource:");
        if(sc.hasNextInt()){
            id = sc.nextInt();
            name =  sc.nextLine();
        }
        else{
            name = sc.nextLine();
            if(myLibrary.findResource(name)!=null){
                resources = myLibrary.findResource(name);
            }
        }
        borrower.tryIssue(resources);
    }
    /*****************************************/


    /*****************************************/
    /******** IO for Resource Return *********/
    /*****************************************/

    void returnResourceIO(Borrower borrower){

        // Hàm này yêu cầu Name hoặc ID của tài nguyên được trả về và gọi hàm tryReturn của Borrower.java
        ArrayList<LibraryResource> resources = new ArrayList<LibraryResource>();
        String name;
        int id = -1;
        System.out.println("Enter the name or ID of resource:");
        if(sc.hasNextInt()){
            id = sc.nextInt();
            name = sc.nextLine();
        }
        else{
            name = sc.nextLine();
            if(myLibrary.findResource(name)!=null){
                resources = myLibrary.findResource(name);
            }
        }

        for(int i =0;i<resources.size();i++){
            id=resources.get(i).getResourceID();
            if(borrower.findIssued(resources.get(i).getResourceID())){
                if(borrower.tryReturn(resources.get(i).getResourceID())){
                    System.out.println("The requested resource has been successfully returned!");
                    return;
                }

            }
        }
        if(borrower.findIssued(id)){
            if(borrower.tryReturn(id)){
                System.out.println("The requested resource has been successfully returned!");
            }
        }



        System.out.println("The resource was not returned! or it was not found!");

    }
    /*****************************************/


    /*****************************************/
    /******** IO for Request Deletion ********/
    /*****************************************/
    void deleteRequestIO(Borrower borrower){

        // Hàm này yêu cầu Name hoặc ID của tài nguyên cần xóa và gọi lệnh withdrawRequest của Borrower.Java...
        ArrayList<LibraryResource> resources = new ArrayList<LibraryResource>();
        int id = -1;;
        String name;
        borrower.viewRequests();
        System.out.println("\nEnter the name or ID of the Resource:");
        if(sc.hasNextInt()){
            id = sc.nextInt();
            name = sc.nextLine();
        }
        else{
            name = sc.nextLine();
            if(myLibrary.findResource(name)!=null){
                resources = myLibrary.findResource(name);
                for(int i =0;i<resources.size(); i++){
                    if(borrower.withdrawRequest(resources.get(i).getResourceID())){
                        System.out.println("The request was withdrawn successfully!");
                        return;
                    }
                }
            }
            else if(borrower.withdrawRequest(id)){
                System.out.println("The request was withdrawn successfully!");
                return;
            }
        }

        System.out.println("The transaction was not completed!");
    }

    /*****************************************/

    /*****************************************/
    /********* IO for Renew Resource *********/
    /*****************************************/

    void renewResourceIO(Borrower borrower){

        // Hàm này yêu cầu name hoặc ID của tài nguyên được gia hạn và gọi tryRenew của Borrower.Java...
        ArrayList<LibraryResource> resources = new ArrayList<LibraryResource>();
        int id = -1;;
        String name;
        System.out.println("\nEnter the name or ID of the Resource to renew: ");
        if(sc.hasNextInt()){
            id = sc.nextInt();
            name = sc.nextLine();
        }
        else{
            name = sc.nextLine();
            for(int i =0;i<resources.size(); i++){
                if(borrower.tryRenew(resources.get(i).getResourceID())){
                    System.out.println("The request was withdrawn successfully!");
                    return;
                }
            }
        }

        if(borrower.tryRenew(id)){
            System.out.println("The resource was renewed successfully!");
        }
        else{
            System.out.println("The resource was not renewed!");
        }


    }

    /*****************************************/

    // Phương thức này ghi dữ liệu vào file.txt
    public void writeLibraryDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("LibraryData.txt"))) {
            // Viết thông tin chung của thư viện
            writer.write("Library Name: " + myLibrary.getLibraryName() + "\n\n");

            // Viết thông tin của user
            writer.write("Users:\n");
            for (LibraryUser user : myLibrary.users) {
                writer.write("ID: " + user.getUserID() + ", Name: " + user.userName + ", Type: " + user.type + "\n");
            }

            // Viết thông tin của tài nguyên
            writer.write("\nResources:\n");
            for (LibraryResource resource : myLibrary.resources) {
                writer.write("ID: " + resource.getResourceID() + ", Name: " + resource.getResourceName() + ", Type: " + resource.getType() + "\n");
            }

            writer.write("\nEnd of Library Data\n");

            System.out.println("Data successfully written to LibraryData.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing data to file: " + e.getMessage());
        }
    }




    /*****************************************/
    /************* MAIN FUNCTION *************/
    /*****************************************/

    public static void main(String[] argv){

        String libName = "LUMS Library";
        // Tạo đối tượng Library từ libName
        // Sử dụng phương thức static để lấy đối tượng Library
        Library library = Library.getInstance(libName);

        // Gọi phương thức loadLibraryFromFile trên đối tượng library
        library.loadLibraryFromFile("library_data.txt");

        Driver driver = new Driver(libName);

        System.out.println("\n\n***Welcome to Library Management System***\n\nSelect from the following options:\n\n" +
                "-  For viewing system stats press 1\n" + "-  For logging into the system as administrator, press 2\n" +
                "-  For faculty login press 3\n-  For student login press 4\n-  For exit, press 5\n");

        String userInput;
        int input;
        boolean done = false;
        String template = "\n\n***Welcome to Library Management System***\n\nSelect from the following options:\n\n" +
                "-  For viewing system stats, press 1\n" + "-  For logging into the system as administrator, press 2\n" +
                "-  For faculty login, press 3\n-  For student login, press 4\n-  For exit, press 5\n";

        // Lấy đầu vào của người dùng và gọi các hàm phù hợp... Giao diện IO chính cho Thư viện
        while(!done){

            if(!driver.sc.hasNextInt()){
                System.out.println("Give the correct input");
                userInput = driver.sc.nextLine();
                continue;
            }
            input = driver.sc.nextInt();
            userInput = driver.sc.nextLine();
            switch(input){
                case 1:
                    driver.myLibrary.getLibraryStats();
                    System.out.println(template);
                    break;
                case 2:
                    driver.loginIO(Constants.ADMIN);
                    System.out.println(template);
                    break;
                case 3:
                    driver.loginIO(Constants.FACULTY);
                    System.out.println(template);
                    break;
                case 4:
                    driver.loginIO(Constants.STUDENT);
                    System.out.println(template);
                    break;
                case 5:
                    System.out.println("Do you really want to lexit? y/n");
                    userInput = driver.sc.nextLine();
                    if((userInput.equals("y") || userInput.equals("Y"))){
                        System.out.println("Thanks... exiting!");
                        done = true;
                    }
                    else{
                        System.out.println(template);
                    }
                    break;
                default:
                    System.out.println("Give the correct input");
                    break;
            }
            driver.myLibrary.updateFines();
        }

        // Chức năng hiện có
        driver.writeLibraryDataToFile();  // Gọi method để ghi dữ liệu

        driver.sc.close();		// Đóng đối tượng Scanner
        // In ra ngày
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Library.calendar;
        System.out.println(dateFormat.format(cal.getTime()));
    }
}