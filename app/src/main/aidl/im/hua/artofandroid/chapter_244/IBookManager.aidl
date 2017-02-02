// IBookManager.aidl
package im.hua.artofandroid.chapter_244;
import im.hua.artofandroid.chapter_244.Book;

// Declare any non-default types here with import statements

interface IBookManager {
    void addBook(in Book book);
    List<Book> getBookList();
}
