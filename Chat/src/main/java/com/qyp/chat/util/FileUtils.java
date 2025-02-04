package com.qyp.chat.util;

import com.qyp.chat.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Component
public class FileUtils {
    public void uplodFile(MultipartFile file,String path, String fileName) {
        File filePath = new File(path);
        if(!filePath.exists())
            filePath.mkdirs();

        try {
            file.transferTo(new File(filePath.getPath()+"/"+fileName));
        } catch (IOException e) {
            throw new BusinessException("文件上传失败！");
        }
    }


    public void downloadFile(HttpServletResponse response,String path,String fileName){
        File file = new File(path + "/" + fileName);
        if(!file.exists())
            throw new BusinessException("文件不存在！");

        response.setContentType("applicatin/x-msdownload;charset=UTF-8");
        response.setHeader("Content-Disposition","attachment;");
        response.setContentLengthLong(file.length());

        FileInputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = response.getOutputStream();
            byte[] b = new byte[1024*64];
            int len = 0;
            while((len = in.read(b)) != -1){
                out.write(b,0,len);
            }
        } catch (IOException e) {
            throw new BusinessException("文件下载失败！");
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
