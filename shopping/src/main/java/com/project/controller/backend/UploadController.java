package com.project.controller.backend;

import com.project.common.ResponseCode;
import com.project.common.ServerResponse;
import com.project.vo.ImageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/manager/")
public class UploadController {

    @Value("${business.imageHost}")
    private String imageHost;

    @GetMapping(value = "/upload")
    public String upload(){

        return "upload";
    }
    @PostMapping(value = "/upload")
    @ResponseBody
    public ServerResponse upload(@RequestParam("uploadfile") MultipartFile uploadfile){
        if(uploadfile==null || uploadfile.getOriginalFilename().equals("")){

            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"图片必须上传");
        }

        //获取上传的图片的名称
        String oldFileName= uploadfile.getOriginalFilename();
        //获取文件扩展名
        String extendName=oldFileName.substring(oldFileName.lastIndexOf('.'));

        //生成新的文件名
        String newFilename= UUID.randomUUID().toString()+extendName;

        File mkdir=new File("d:/upload");
        if(!mkdir.exists()){
            mkdir.mkdirs();
        }

        File newFile=new File(mkdir,newFilename);

        try {
            uploadfile.transferTo(newFile);
            // http://localhost/filename
            ImageVO imageVO=new ImageVO(newFilename,imageHost+newFilename);
            return ServerResponse.serverResponseBySuccess(imageVO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.serverResponseByError();


        // return null;
    }
}
