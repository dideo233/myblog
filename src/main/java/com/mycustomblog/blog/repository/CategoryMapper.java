package com.mycustomblog.blog.repository;

import com.mycustomblog.blog.dto.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    @Select("SELECT c.title, count(*) as 'count'\n"
            + "  FROM article a, category c\n"
            + "  where a.categorynum = c.categorynum\n"
            + "  group by title\n")
    List<CategoryVO> findCategoryInfo();

    @Select("SELECT count(*) FROM article WHERE categorynum = #{categorynum}")
    int getCategoryCount(@Param("categorynum") Long categorynum);
}
