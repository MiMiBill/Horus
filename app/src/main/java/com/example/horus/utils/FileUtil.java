package com.example.horus.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.example.baselib.utils.CompatibleUtil;
import com.example.baselib.utils.LogUtil;
import com.example.horus.app.Constant;
import com.example.horus.app.MyApp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


/**
 * Created by Lin on 2017/12/21.
 */

public class FileUtil {


    public static final String THUMBNAIL = "thumbnail";

    private static String APP_DIR_NAME = Constant.FOLDER_NAME;
    private static String FILE_DIR_NAME = "files";
    private static String mRootDir;
    private static String mAppRootDir;
    private static String mFileDir;

    public static void init() {
        mRootDir = getRootPath();
        if (mRootDir != null && !"".equals(mRootDir)) {
            mAppRootDir = mRootDir + "/" + APP_DIR_NAME;
            mFileDir = mAppRootDir + "/" + FILE_DIR_NAME;
            File appDir = new File(mAppRootDir);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            File fileDir = new File(mAppRootDir + "/" + FILE_DIR_NAME);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

        } else {
            mRootDir = "";
            mAppRootDir = "";
            mFileDir = "";
        }
    }




    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.exists() && !file.isDirectory() && file.canRead() ? file.getName() : null;
    }

    public static String saveVideoThumbnail(Context context, String videoPath) {
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
        return saveVideoThumbnail(context, videoPath, thumbnail);
    }

    private static String saveVideoThumbnail(Context context, String videoPath, Bitmap bitmap) {
        String fileStartStr = "thumbnail" + MyApp.getInstance().getUserId();
        String thumbName = createFileName(fileStartStr, ".jpg");
        return saveBitmapInCache(context, thumbName, bitmap);
    }


    /**
     * 获取原始图片的缩略图
     * @param originalImgPath
     * @return
     */
    public static String getThumbnailPath(String originalImgPath){
        final int THUMBNAIL_DEFAULT_WIDTH_PX = 512;//px
        final int THUMBNAIL_DEFAULT_HEIGHT_PX = 1024;//px
        //创建名字
        String append = ".jpg";
        if (!TextUtils.isEmpty(originalImgPath)){
            append = originalImgPath.substring(originalImgPath.lastIndexOf("."));
        }
        String id = TextUtils.isEmpty(MyApp.getInstance().getUserId()) ? String.valueOf(new Random().nextInt(999)): MyApp.getInstance().getUserId();
        String fileName = "" + System.currentTimeMillis() + "_"  + id + "_" + THUMBNAIL + append;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不加载bitmap到内存中
        BitmapFactory.decodeFile(originalImgPath,options);

        if (!TextUtils.isEmpty(options.outMimeType) && options.outMimeType.toLowerCase().contains("gif")){
            return saveFileInCache(MyApp.getInstance(),originalImgPath,fileName);
        }
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inSampleSize = 1;
        ////inSampleSize的作用就是可以把图片的长短缩小inSampleSize倍，所占内存缩小inSampleSize的平方
        options.inSampleSize = Math.max(caculateSampleSize(options.outWidth, THUMBNAIL_DEFAULT_WIDTH_PX),caculateSampleSize(options.outHeight, THUMBNAIL_DEFAULT_HEIGHT_PX));
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(originalImgPath, options); // 解码文件
        return saveBitmapInCache(MyApp.getInstance(), fileName,bm);
    }


    /**
     * 计算出所需要压缩的大小
     * @param size
     * @param reqSize  我们期望的图片的宽，单位px
     * @return
     */
    private static int caculateSampleSize(int size, int reqSize) {
        int sampleSize = 1;
        int picWidth = size;
        if (picWidth > reqSize) {
           int value =  picWidth / reqSize;
           while (sampleSize < value){
               sampleSize *= 2;
           }
        }
        return sampleSize;
    }

    public static String saveBitmapInCache(Context context, String fileName, Bitmap bitmap) {
        String path = getDiskCacheDir(context) + File.separator + fileName;
        try {
            FileOutputStream fout = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveFileInCache(Context context, String originalImgPath, String desFileName) {
        String desPath = getDiskCacheDir(context) + File.separator + desFileName;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(originalImgPath);
            fileOutputStream = new FileOutputStream(desPath);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
            return desPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getDiskCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if (context.getExternalCacheDir() != null) {
                return context.getExternalCacheDir().getPath();
            }
        }
        return context.getCacheDir().getPath();
    }

    public static String getFileDir(){
        return mFileDir;
    }


    public static String getRootPath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return CompatibleUtil.getExternalStoragePath(MyApp.getInstance()).getAbsolutePath(); // filePath:  /sdcard/
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data"; // filePath:  /data/data/
        }
    }

    /**
     * 打开文件
     * 兼容7.0
     *
     * @param context     activity
     * @param file        File
     * @param contentType 文件类型如：文本（text/html）
     *                    当手机中没有一个app可以打开file时会抛ActivityNotFoundException
     */
    public static void startActionFile(Context context, File file, String contentType) throws ActivityNotFoundException {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
        intent.setDataAndType(getUriForFile(context, file), contentType);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * the traditional io way
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * 打开相机
     * 兼容7.0
     *
     * @param activity    Activity
     * @param file        File
     * @param requestCode result requestCode
     */
    public static void startActionCapture(Activity activity, File file, int requestCode) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(activity, file));
        activity.startActivityForResult(intent, requestCode);
    }


    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            // FIXME 未在Manifest中注册
            uri = FileProvider.getUriForFile(context.getApplicationContext(), ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    /**
     * 清空缓存
     */
    public static boolean clearDiskCache(Context context) {
        String cachePath = getDiskCacheDir(context);
        File file = new File(cachePath);
        return deleteDir(file);
    }

    private static boolean deleteDir(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {

                File[] childrens = file.listFiles();
                if (childrens != null) {
                    for (File children : childrens) {
                        boolean success = deleteDir(children);
                        if (!success) {
                            return false;
                        }
                    }
                }

            } else {
                return file.delete();
            }
        }

        return true;
    }


    public static void copy(String source, String target){
        copy(new File(source),new File(target));
    }

    /**
     * 复制文件
     *
     * @param source 输入文件
     * @param target 输出文件
     */
    public static void copy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制文件
     *
     *   输入文件
     *   输出文件
     */
    public static void copy(FileInputStream fileInputStream, FileOutputStream fileOutputStream) {
        try {
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveBitmapToGallery(File targetFile, Bitmap bitmap){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 最后通知图库更新
        MyApp.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(targetFile.getPath()))));
    }


    /**
     * 创建文件名称
     * @param fileStartStr 文件名前缀
     * @param fileEndStr 文件名后缀
     * @return 由时间戳产生的文件名
     */
    public static String createFileName(String fileStartStr, String fileEndStr) {
        long time = System.currentTimeMillis();
        String fileName = fileStartStr + String.valueOf(time) + fileEndStr;
        return fileName;
    }


    /**
     * 图片后缀 如果没有判断出类型 默认给jpg
     * @return png/jpg/gif
     */
    public static String getImageSuffix(String filePath) {
        String suffix = "jpg";

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        String mimeType = options.outMimeType;
        LogUtil.d("FileUtil", "解析图片类型：" + mimeType);

        switch (mimeType) {
            case "image/png":
            case "image/PNG":
                suffix = "png";
                break;
            case "image/webp":
            case "image/WEBP":
                suffix = "webp";
                break;
            case "image/gif":
            case "image/GIF":
                suffix = "gif";
                break;
            case "image/bmp":
            case "imagex-ms-bmp":
                suffix = "bmp";
                break;
            case "image/jpeg":
            case "image/JPEG":
            default:
                break;
        }

        return suffix;
    }

    public static String getFileSuffix(String filePath){
        if (TextUtils.isEmpty(filePath))
            return "";
        return filePath.substring(filePath.indexOf('.'));
    }


    /**
     * 生成一个避重的文件名 用于图片上传至腾讯COS免覆盖
     *
     * @param filePath 本地文件路径（用于获取后缀）
     * @return 时间戳+talkId + index（多个文件或多张图时的序列号，单张时随便传，建议传0）
     */
    public static String createRandomFileName(String filePath, int index) {
        String randomName = String.valueOf(System.currentTimeMillis());

        // 拼talkId时如果不存在 就补充一个8位随机数（固定44开头便于查错）
        if (!TextUtils.isEmpty(MyApp.getInstance().getUserId())) {
            randomName += MyApp.getInstance().getUserId();
        }

        int randomId = new Random().nextInt(899999) + 100000;
        randomName += "44" + String.valueOf(randomId);

        randomName += ("_" + index);

        // 从原始文件名截取后缀
        // 注意可能截到其他长字符串导致文件名超长 所以至少对后缀长度做下限制
        String localFileName = getFileName(filePath);
        if (localFileName != null && localFileName.contains(".")) {
            String[] namePart = localFileName.split("\\.");
            if (namePart[namePart.length - 1].length() < 6) {
                if (localFileName.contains(THUMBNAIL)){
                    randomName =  THUMBNAIL + "_" + randomName;
                }
                randomName += "." + namePart[namePart.length - 1];
            }
        }
        LogUtil.e("FileUtil", "RandomName:" + randomName);
        return randomName;
    }


    public static int getIndexFromFileName(String fileName){
        int index = -1;
        if (TextUtils.isEmpty(fileName))return index;
        int indexPot = -1;
        int indexUnderLine = -1;
        if (fileName.contains(".")){
            indexPot = fileName.lastIndexOf(".");
        }
        indexUnderLine = fileName.lastIndexOf("_");
        if (indexUnderLine <= 0)return index;
        String indexStr = null;
        if (indexPot > 0){
            indexStr = fileName.substring(indexUnderLine + 1,indexPot);
        }else {
            indexStr = fileName.substring(indexUnderLine + 1);
        }
        try {
            index = Integer.parseInt(indexStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return index;
    }


    /**
     * 获取缓存文件地址
     */
    public static String getCacheFilePath(String fileName){
        return getDiskCacheDir(MyApp.getInstance()) + File.separator + fileName;
    }


    public static String readAssetContent(String name){
        StringBuffer contentBuffer = new StringBuffer();
        try {
            InputStream inputStream = MyApp.getInstance().getAssets().open(name);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ( (line = bufferedReader.readLine() )!= null){
                contentBuffer.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return contentBuffer.toString();
    }


    /**
     *
     * @param filePath 文件路径
     * @return -1L表示文件不存在
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length();
        }

        return -1L;
    }
}
