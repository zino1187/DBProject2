/*
 * �� Ŭ������ ������ �ۼ��ϱ� ������ �ƴ϶�, 
 * ���� �����ͺ��̽��� ���ڵ� 1���� �������
 * ��ü�̴�. �̷� �������� ����� Ŭ������ ����
 * �� ���� �о߿����� DTO(������ ���ް�ü), VO
 * */
package book;
public class Book {
	private int book_id;
	private int subcategory_id;
	private String book_name;
	private int price;
	private String img;
	
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public int getSubcategory_id() {
		return subcategory_id;
	}
	public void setSubcategory_id(int subcategory_id) {
		this.subcategory_id = subcategory_id;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
}
