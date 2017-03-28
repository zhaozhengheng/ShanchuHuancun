package com.bawei.ShanchuHuancunn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button mButton;
    private File cacheDir;
    private long totalSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        cacheDir = getCacheDir();


        writeCache(cacheDir);
        //显示当前缓存的大小
        try {
            String size = DataClearManager.getCacheSize(cacheDir);
            mTextView.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initDate();
    }

    private void initDate()
    {
        //监听事件
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataClearManager.deleteFolderFile(cacheDir.getAbsolutePath(), true);
                String size = null;
                try {
                    size = DataClearManager.getCacheSize(cacheDir);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mTextView.setText(size);
            }
        });
    }

    private void initView()
    {
        mTextView = (TextView) findViewById(R.id.TextView);
        mButton = (Button) findViewById(R.id.Button);
    }
    private void clearAllCache(File cacheDir) {
        File[] files = cacheDir.listFiles();
        //uuuu列出所有  bbb.txt
        for (File file : files) {
            if (file.isDirectory()) {
                //如果是文件夹，还是得判断
                if (file.listFiles().length == 0) {
                    //空文件夹
                    file.delete();
                } else {
                    //继续递归删除文件
                    clearAllCache(file);
                }
            } else {
                file.delete();
            }
        }
        //删除uuu
        cacheDir.delete();
    }
    private void writeCache(File cacheDir) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(cacheDir, "aaaa.txt"));
            fileOutputStream.write("http://img.tuku.cn/file_thumb/201703/m2017032811471747.jpg".getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(cacheDir, "uuuu");
        Log.d("zzz","=========="+cacheDir.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file, "bbbb.txt"));
            fileOutputStream.write("http://img.tuku.cn/file_thumb/201703/m2017032811471747.jpg;aeo".getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void getCacheSizeMethod(File cacheDir) {
        //获取当前所有的大小
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                //是文件夹
                if (file.isDirectory()) {
                    //继续获取
                    getCacheSizeMethod(file);
                } else {
                    //是文件---获取当前文件的大小
                    long length = file.length();
                    totalSize = totalSize + length;
                }
            }

        }
    }
    private String formartSize(long totalSize) {
        //小于1K
        if (totalSize < 1024) {
            return totalSize + "字节";
        } else {
            if ((totalSize / 1024) < 1024) {
                //kb范围以内
                return totalSize / 1024 + "kb";
            }
        }
        return null;
    }
    private String getCacheSize(File cacheDir) {
        //先置位
        totalSize = 0;

        getCacheSizeMethod(cacheDir);
        //字节大小转成字符串  222222     12KB
        return formartSize(totalSize);
    }

}
