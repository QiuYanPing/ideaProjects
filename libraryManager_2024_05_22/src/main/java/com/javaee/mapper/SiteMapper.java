package com.javaee.mapper;

import com.javaee.pojo.Site;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SiteMapper {
    @Select("select * from site")
    List<Site> list();
    @Insert("insert into site (site) values (#{site})")
    void insert(Site site);

    void update(Site site);

    void delete(List<String> sites);
}
