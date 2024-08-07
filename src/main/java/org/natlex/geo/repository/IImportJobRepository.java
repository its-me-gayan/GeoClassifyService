package org.natlex.geo.repository;

import org.natlex.geo.entity.ImportJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 9:26 PM
 */
@Repository
public interface IImportJobRepository extends JpaRepository<ImportJob ,Long> {
}
