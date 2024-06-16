package com.javaee.controller;

import com.javaee.anno.Log;
import com.javaee.pojo.Result;
import com.javaee.pojo.Site;
import com.javaee.service.SiteService;
import com.javaee.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class SiteController {
    @Autowired
    SiteService siteService;
    @Value("myToken")
    String jwt;
    @GetMapping("/site")
    public Result list(){
        List<Site> siteList = siteService.list();
        log.info("查询所有座位");
        return Result.success(siteList);
    }
    @Log
    @PostMapping("/site")
    public Result insert(@RequestBody Site site){
        siteService.insert(site);
        log.info("添加座位");
        return Result.success();
    }
    @Log
    @PutMapping("/site")
    public Result update(@RequestBody Site site){
        siteService.update(site);
        log.info("修改座位状态");
        return Result.success();
    }
    @Log
    @DeleteMapping("/delete/{sites}")
    public Result delete(@PathVariable List<String> sites){
        siteService.delete(sites);
        log.info("删除座位");
        return Result.success();
    }

    @GetMapping("/site/{site}")
    public Result showSites(@PathVariable Integer site){
        List<Site> siteList = siteService.showSites(site);
        log.info("根据楼层查询位置："+site);
        return Result.success(siteList);
    }

}
