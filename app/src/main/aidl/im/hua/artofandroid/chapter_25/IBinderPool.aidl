// IBinderPool.aidl
package im.hua.artofandroid.chapter_25;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
