// IBookManager.aidl
package im.hua.artofandroid.chapter_2.aidl;

import im.hua.artofandroid.chapter_2.aidl.Book;
// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
