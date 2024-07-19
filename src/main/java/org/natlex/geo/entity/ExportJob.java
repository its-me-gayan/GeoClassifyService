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
public class ExportJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String jobCompletedMessage;
    private String fileName;
    @Lob
    private byte[] file;
}
