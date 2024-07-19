package org.natlex.geo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.natlex.geo.util.JobStatus;

import java.time.LocalDateTime;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 9:28 PM
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobCompletedMessage;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    @Lob
    private byte[] file;
}
