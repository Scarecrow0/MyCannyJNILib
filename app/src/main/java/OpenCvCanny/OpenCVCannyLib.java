package OpenCvCanny;

import android.graphics.Bitmap;
import android.os.Handler;


/**
 * Created by root on 17-10-31.
 * 直接封装jni部分
 * 负责从interface接受调用 构造 启动工作线程
 */

public class OpenCVCannyLib {

    static {
        System.loadLibrary("OpenCV"); // 加载编译好的.so动态库
    }

    /**
     * 声明native方法，调用OpenCV的边缘检测
     *
     * @param buf 图像
     * @param w   宽
     * @param h   高
     * @return 边缘图
     */


    //      jni 相关函数定义
    //todo 接口函数编写 修改
    public static native int[] canny(int[] buf, int w, int h);


    //      处理内部事务的线程
    private ProcessHandlerThread processingThread;

    //  负责从外部接受调用的方法
    public void startMatching(Handler callback, final Bitmap img1, final Bitmap img2) {
        processingThread = new ProcessHandlerThread(callback);
        processingThread.setLooperPreparedListenner(new ProcessHandlerThread.LooperPreparedListenner() {
            @Override
            public void onLooperPrepared() {
                processingThread.startCannyProcess(img1, img2);
                // 将匹配图片传入内部线程 发起匹配过程
            }
        });
        processingThread.start();
        processingThread.getLooper();
        //启动内部线程 准备进行canny

    }

    public static int[] bitmap2IntaArray(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pix = new int[width * height];
        bitmap.getPixels(pix, 0, width, 0, 0, width, height);
        return pix;
    }

    public static Bitmap intArray2Bitmap(int[] pix, int width, int height) {
        Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        resultBitmap.setPixels(pix, 0, width, 0, 0, width, height);
        return resultBitmap;
    }

}
