package com.tequila.callbacks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by admin on 2017/3/8.
 */

public abstract class FileCallback implements Callback{

    private String dirName;
    private String fileName;

    public FileCallback(String dirName ,String fileName){
        this.dirName = dirName;
        this.fileName = fileName;
    }


    public abstract void inProgress(float progress , long total);

    public abstract void onError(Call call, Exception e);


    public File saveFile(Response response, String dirName , String fileName) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len ;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();

            long sum = 0;

            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;

                Executors.newCachedThreadPool().execute(new Runnable() {

                    @Override
                    public void run() {
                        inProgress(finalSum * 1.0f / total,total);
                    }
                });
            }
            fos.flush();

            return file;

        } finally {
            try {
                response.body().close();
                if (is != null) is.close();
            } catch (IOException e) {

            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {

            }

        }
    }


    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(!call.isCanceled()){
            saveFile(response,dirName,fileName);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if(!call.isCanceled()){
            onError(call,e);
        }
    }
}
