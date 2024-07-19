package org.natlex.geo.repository;

import org.natlex.geo.entity.Section;
import org.natlex.geo.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 10:59 PM
 */
@Repository
public interface SectionRepository extends JpaRepository<Section , String> {

    Optional<Section> findSectionByNameAndStatusNot(String name,Status status);
    Optional<Section> findSectionBySectionIdAndStatusNot(Integer sectionId,Status status);
    Optional<Section> findSectionByIdAndStatusNot(String id,Status status);
    List<Section> findAllSectionByStatusNot(Status status);

    @Query("SELECT s FROM Section s JOIN s.geologicalClasses gc WHERE s.status <> 'DELETED' AND gc.status <> 'DELETED' ORDER BY s.sectionId ASC")
    List<Section> findActiveSectionsWithActiveGeologicalClasses();

    @Query("SELECT s FROM Section s JOIN s.geologicalClasses gc WHERE s.status <> 'DELETED' AND gc.status <> 'DELETED' AND gc.code = ?1")
    List<Section> findActiveSectionsWithActiveGeologicalClassesByGeoLocCode(String code);
}
