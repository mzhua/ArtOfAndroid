package im.hua.artofandroid.chapter_25;

import android.os.RemoteException;

/**
 * Created by hua on 2017/2/3.
 */

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
