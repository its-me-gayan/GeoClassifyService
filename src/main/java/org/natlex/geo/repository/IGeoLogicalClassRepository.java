package org.natlex.geo.repository;

import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 11:14 AM
 */
@Repository
public interface IGeoLogicalClassRepository extends JpaRepository<GeologicalClass ,String> {
    @Query("SELECT CASE WHEN COUNT(gc) > 0 THEN TRUE ELSE FALSE END FROM GeologicalClass gc WHERE gc.code = :code AND gc.status <> :status")
    boolean existsGeologicalClassByCodeAndStatusNot(@Param("code") String code, @Param("status") Status status);
    Optional<GeologicalClass> findGeologicalClassByCodeAndStatusNot(String code, Status status);
    Optional<GeologicalClass> findByIdAndStatusNot(String id, Status status);

    List<GeologicalClass> findAllByStatusNot(Status status);
}
