// Đây là parent class của tất cả Library Resource
public class LibraryResource {
    // Tên của một Library resource, phải unique
    protected String resourceName;
    // Tạo phương thức Getter để lấy tên resource
    public String getResourceName() {
        return resourceName;
    }

    // id của resource phải unique
    protected int resourceID;
    //Tạo phương thức Getter để lấy ID
    public int getResourceID() {
        return resourceID;
    }

    // Tạo type của resource để phân loại
    protected int type;
    // Tạo phương thức Getter để lấy type của resource
    public int getType() {
        return type;
    }
}
