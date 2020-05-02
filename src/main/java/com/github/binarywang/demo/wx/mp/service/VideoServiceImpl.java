package com.github.binarywang.demo.wx.mp.service;


import com.alibaba.fastjson.JSON;
import com.github.binarywang.demo.wx.mp.domain.VideoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class VideoServiceImpl {

    @Resource
    private RestTemplate restTemplate;

    public String getVideoUrl(String content){


        String url = "http://5.nmgbq.com/2/api.php";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("url",content);
        HttpEntity requestBody = new HttpEntity(map, headers);
        String result = restTemplate.postForObject(url,requestBody,String.class);
        if(Objects.nonNull(result)){
            VideoResponse videoResponse = JSON.parseObject(result,VideoResponse.class);
            return videoResponse.getUrl();
        }
        return null;
    }

}
