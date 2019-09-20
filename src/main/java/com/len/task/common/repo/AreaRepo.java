package com.len.task.common.repo;

import com.len.task.common.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/6 15:33
 */
@Repository
public interface AreaRepo extends JpaRepository<Area, Integer>, JpaSpecificationExecutor<Area> {

    List<Area> findAllByAreaLevel(int level);

    List<Area> findAllByParentId(int parentId);


    String GET_SELECTED_AREAS = "select * from t_area where id in (:ids)";

    @Query(value = GET_SELECTED_AREAS, nativeQuery = true)
    List<Area> getSelectAreas(@Param("ids") String ids);
}
