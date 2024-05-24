package com.javaee.service.impl;

import com.javaee.mapper.SiteMapper;
import com.javaee.pojo.Site;
import com.javaee.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {
    @Autowired
    SiteMapper siteMapper;
    @Override
    public List<Site> list() {
        List<Site> siteList = siteMapper.list();
        return siteList;
    }

    @Override
    public void insert(Site site) {
        siteMapper.insert(site);
    }

    @Override
    public void update(Site site) {
        siteMapper.update(site);
    }

    @Override
    public void delete(List<String> sites) {
        siteMapper.delete(sites);
    }
}
