// ISecurityCenter.aidl
package im.hua.artofandroid.chapter_25;

// Declare any non-default types here with import statements

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}
