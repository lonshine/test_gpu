package com.lonshine.test.gpuimage.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

/**
 * Created by lonshine on 15/11/4 下午3:00.
 */

public class FileUtil {

    /**
     * @param size
     * @return
     * @description 文件大小单位转换
     * @author by Fooyou
     * @date 2015年3月4日 上午9:34:49
     */
    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));
        return df.format(Float.valueOf(f).doubleValue()) + " MB";
    }


    /**
     * @param dir
     * @return
     * @description
     * @author by Fooyou
     * @date 2015年3月22日   下午2:01:26
     */
    public static long getDirSize(File dir) {
        long dirSize = 0;
        if (dir == null) {
            return 0;
        }
        if (dir.isFile()) {
            return dir.length();
        }
        File[] files = dir.listFiles();
        if (files == null) return 0;

        for (File file : files) {
            if(file == null) continue;

            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }

    /**
     * @param file
     * @return
     * @description 00 MB
     * @author by Fooyou
     * @date 2015年3月22日   下午2:01:32
     */
    public static String getFileSize(File file) {
        String filesize = "0";
        filesize = FileUtil.formatFileSize(getDirSize(file));
        return filesize;
    }

    /**
     * @param file
     * @description
     * @author by Fooyou
     * @date 2015年3月22日   下午2:15:47
     *
     * 这个方法有逻辑错误， 请使用{@link #deleteDirectory(File)}
     */
    @Deprecated
    public static boolean deleteFile(File file) {
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return file.delete();
            }
            for (File f : childFile) {
                deleteFile(f);
            }
        }
        return false;
    }

    public static boolean deleteDirectoryQuitely(File dir) {
        try {
            deleteDirectory(dir);
            return true;
        } catch (IOException e) {
        }
        return false;
    }
    /**
     * Recursively delete a directory.
     *
     * @param directory  directory to delete
     * @throws IOException in case deletion is unsuccessful
     */
    public static void deleteDirectory(File directory)
            throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message =
                    "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }
    public static void cleanDirectoryQuitely(File directory) {
        try {
            cleanDirectory(directory);
        } catch (Exception e) {

        }
    }
    /**
     * Clean a directory without deleting it.
     *
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }
    /**
     * Delete a file. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     *      (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file  file or directory to delete, not null
     * @throws NullPointerException if the directory is null
     * @throws IOException in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist: " + file);
            }
            if (!file.delete()) {
                String message =
                        "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * @param path
     * @return
     * @description
     * @author by Fooyou
     * @date 2015年3月4日 上午9:34:01
     */
    @SuppressWarnings("deprecation")
    public static long getFreeSpace(String path) {
        long freeSize = 0;
        StatFs statFs = new StatFs(path);
        long blocks = statFs.getAvailableBlocks();
        long size = statFs.getBlockSize();
        freeSize = blocks * size;
        return freeSize;
    }


    /**
     * @return
     * @description 判断 SD 卡 是否 可用
     * @author by Fooyou
     * @date 2015年3月4日 上午9:33:14
     */
    public static boolean checkSDEnabled() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @param context
     * @param fileName
     * @param content
     * @throws IOException
     * @description
     */
    public static void writeFile(Context context, String fileName,
                                 String content) throws IOException {
        // 在目录不存在，或者要读的文件是个目录时，会throw FileNotFoundException
        FileOutputStream stream = context.openFileOutput(fileName,
                Context.MODE_PRIVATE);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream);
        outputStreamWriter.write(content);
        outputStreamWriter.close();
    }
    public static boolean writeFileQuietly(Context context, String fileName,
                                           String content) {
        try {
            writeFile(context, fileName, content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     * @description
     */
    public static String readFile(Context context, String fileName)
            throws IOException {
        File file = context.getFileStreamPath(fileName);
        if (!file.exists()) {
            return null;
        }
        FileInputStream stream = context.openFileInput(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        stream.close();
        return stringBuilder.toString();
    }

    public static String readFileQuietly(Context context, String fileName) {
        try {
            return readFile(context, fileName);
        } catch (IOException e) {
            return null;
        }
    }
    /**
     * @param filepath
     * @return
     * @author sam
     * 添加此方法 功能是判断图片路径是否是  file://  开头
     */
    public static boolean filePathIsRight(String filepath) {
        return filepath == null ? false : filepath.startsWith("file://");
    }

    //保存对象到本地
    public static void saveObjectToLocal(Context context, String name, Object object) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            //保存文件时候异常

        } finally {
            if (fos != null) {
                fos.close();
            }
            if (oos != null) {
                oos.close();
            }
        }
    }

    //获取保存在本地的对象
    public static Object getObjectFromLocal(Context mContext, String name) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = mContext.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception ex) {
            //  ex.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return null;
    }







}

