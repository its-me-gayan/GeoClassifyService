package org.natlex.geo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.natlex.geo.util.Status;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/11/24
 * Time: 10:45 PM
 */
@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeologicalClass {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String code;

    private String name;
    private int classNumber;

    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToMany(mappedBy = "geologicalClasses")
    private Set<Section> sections = new HashSet<>();
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeologicalClass that = (GeologicalClass) o;
        return code != null ? code.equalsIgnoreCase(that.code) : that.code == null;
    }

    @Override
    public int hashCode() {
        return code != null ? code.toLowerCase().hashCode() : 0;
    }
    @Override
    public String toString() {
        return "GeologicalClass{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", classNumber='" + classNumber + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
