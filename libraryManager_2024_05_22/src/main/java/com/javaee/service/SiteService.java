package com.javaee.service;

import com.javaee.pojo.Site;

import java.util.List;

public interface SiteService {
    List<Site> list();

    void insert(Site site);

    void update(Site site);

    void delete(List<String> sites);

    List<Site> showSites(Integer site);

}
