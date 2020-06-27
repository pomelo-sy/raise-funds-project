//package org.shizhijian.raisefunds.controller;
//
//import com.github.tobato.fastdfs.domain.fdfs.MetaData;
//import com.github.tobato.fastdfs.domain.fdfs.StorePath;
//import com.github.tobato.fastdfs.service.FastFileStorageClient;
//import lombok.extern.slf4j.Slf4j;
//import org.shizhijian.raisefunds.bean.RespApi;
//import org.shizhijian.raisefunds.pojo.RaiseFileMaterial;
//import org.shizhijian.raisefunds.service.RaiseFileMaterialService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;
//
//@RestController
//@Slf4j
//public class FileController {
//
//    @Autowired
//    private FastFileStorageClient storageClient;
//
//    @Autowired
//    private RaiseFileMaterialService raiseFileMaterialService;
//
//    @PostMapping
//    public RespApi<Boolean> uploadFile(@RequestParam MultipartFile uploadFile, Integer descId, @RequestParam String openId) throws IOException {
//        descId = 2;
//        Set<MetaData> metaDataSet = new HashSet<MetaData>();
//        metaDataSet.add(new MetaData("Author", openId));
//        metaDataSet.add(new MetaData("CreateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
//
//        StorePath storePath = null;
//        storePath = storageClient.uploadFile(uploadFile.getInputStream(), uploadFile.getSize(), descId.toString(), metaDataSet);
//        log.info("storePath: {}, storeFullPath: {}", storePath.getPath(), storePath.getFullPath());
//        raiseFileMaterialService.save(RaiseFileMaterial.builder().url(storePath.getPath())
//                .createDate(new Date()).descId(descId).isFrontPage(false).build());
//
//        RespApi result  = RespApi.OK;
//        result.setData(storePath.getPath());
//        return result;
//    }
//
//
//}
