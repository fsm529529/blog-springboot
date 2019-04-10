package net.stackoverflow.blog.dao;

import net.stackoverflow.blog.common.Page;
import net.stackoverflow.blog.pojo.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 分类表DAO
 *
 * @author 凉衫薄
 */
@Mapper
public interface CategoryDao {

    List<Category> selectByPage(Page page);

    List<Category> selectByCondition(Map<String, Object> searchMap);

    Category selectById(String id);

    int insert(Category category);

    int batchInsert(List<Category> list);

    int deleteById(String id);

    int batchDeleteById(List<String> list);

    int update(Category category);

    int batchUpdate(List<Category> list);

}