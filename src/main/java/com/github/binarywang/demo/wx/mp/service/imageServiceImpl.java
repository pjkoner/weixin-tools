package com.github.binarywang.demo.wx.mp.service;


import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpMaterialServiceImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
@Slf4j
public class imageServiceImpl {


    public String getPicUrl(String picUrl, String fromUser, WxMpService weixinService) throws IOException, WxErrorException {

        WxMpMaterialService wxMpMaterialService = new WxMpMaterialServiceImpl(weixinService);

        URL url = new URL(picUrl);
        InputStream file = url.openStream();
        BufferedImage image = ImageIO.read(file);
        BufferedImage destImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        ColorConvertOp cco = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        cco.filter(image, destImage);
        image.getGraphics().dispose();
        File destFile = new File("/root/picture/"+ fromUser + System.currentTimeMillis() + ".png");
        ImageIO.write(destImage, "png", destFile);
        image.getGraphics().dispose();
        WxMediaUploadResult result =  wxMpMaterialService.mediaUpload(WxConsts.MaterialType.IMAGE,destFile);
        return result.getMediaId();
    }







}
