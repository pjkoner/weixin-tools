package com.github.binarywang.demo.wx.mp.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.demo.wx.mp.builder.ImageBuilder;
import com.github.binarywang.demo.wx.mp.builder.TextBuilder;
import com.github.binarywang.demo.wx.mp.service.VideoServiceImpl;
import com.github.binarywang.demo.wx.mp.service.imageServiceImpl;
import com.github.binarywang.demo.wx.mp.utils.JsonUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Resource
    private imageServiceImpl imageService;

    @Resource
    private VideoServiceImpl videoService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
//        try {
//            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
//                && weixinService.getKefuService().kfOnlineList()
//                .getKfOnlineList().size() > 0) {
//                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
//                    .fromUser(wxMessage.getToUser())
//                    .toUser(wxMessage.getFromUser()).build();
//            }
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }

        if(XmlMsgType.TEXT.equals(wxMessage.getMsgType())){
            //TODO 组装回复消息
            JSONObject jsonObject = JsonUtils.toJson(wxMessage);
            String content = jsonObject.getString("content") ;
            logger.info("收到信息内容：" + content);
            //取出包含http和https的内容
            if(content.contains("http")||content.contains("https")){
                String videoUrl = videoService.getVideoUrl(content);
                if(Objects.nonNull(videoUrl)){
                    return new TextBuilder().build(videoUrl, wxMessage, weixinService);
                }
            }
            return new TextBuilder().build("1.回复图片将会把图片置灰\n2.回复视频地址可以去除广告", wxMessage, weixinService);
        }
        if(XmlMsgType.IMAGE.equals(wxMessage.getMsgType())){
            //TODO 组装回复消息
            JSONObject jsonObject = JsonUtils.toJson(wxMessage);
            String content = jsonObject.getString("mediaId") ;
            String fromUser = jsonObject.getString("fromUser");
            String mediaId = null;
            try {
                mediaId =  imageService.getPicUrl(jsonObject.getString("picUrl"),fromUser, weixinService);
            } catch (IOException | WxErrorException e) {
                e.printStackTrace();
            }
            return new ImageBuilder().build(mediaId, wxMessage, weixinService);
        }

        return new TextBuilder().build("无法识别", wxMessage, weixinService);
    }

}
