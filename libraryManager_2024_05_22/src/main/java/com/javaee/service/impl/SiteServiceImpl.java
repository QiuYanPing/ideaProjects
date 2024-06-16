package com.javaee.service.impl;

import com.javaee.mapper.SiteMapper;
import com.javaee.mapper.UserMapper;
import com.javaee.pojo.Site;
import com.javaee.pojo.User;
import com.javaee.service.SiteService;
import com.javaee.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {
    @Autowired
    HttpServletRequest request;
    @Autowired
    SiteMapper siteMapper;
    @Autowired
    UserMapper userMapper;
    @Value("${myToken}")
    String jwt;

    @Override
    public List<Site> list() {
        List<Site> siteList = siteMapper.list();
        return siteList;
    }

    @Override
    public void insert(Site site) {
        siteMapper.insert(site);
    }

    @Transactional
    @Override
    public void update(Site site) {
        /*request.getHeader("token")*/
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer id = (Integer) claims.get("id");
        User user = userMapper.selectById(id);
        if (site.getState().equals("已预约")) {
            user.setSite(site.getSite());
        } else {
            user.setSite("");
        }
        userMapper.update(user.getUserName(), user.getPassword(), user.getName(), user.getGender(),
                user.getImage(), user.getSite(), user.getUpdateTime().toString(), user.getId());

        siteMapper.update(site);
    }

    @Override
    public void delete(List<String> sites) {
        siteMapper.delete(sites);
    }

    @Override
    public List<Site> showSites(Integer site) {
        List<Site> siteList = siteMapper.showSites(site);
        return siteList;
    }
}
