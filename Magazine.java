// Class này extends từ LibraryResource
public class Magazine extends LibraryResource {
    // constructor của magazine
    // Tham chiếu tên và ID
    public Magazine(String name, int resID) {
        this.resourceName = name;
        this.resourceID = resID;
        this.type = Constants.MAGAZINE;
    }
}
