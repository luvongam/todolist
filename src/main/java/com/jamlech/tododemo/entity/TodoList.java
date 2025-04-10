package com.jamlech.tododemo.entity;

import ch.qos.logback.core.status.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TodoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate dueDate;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.TODO;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
    @Lob
    private String attachmentType;
    //    relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum Status {
        TODO,
        IN_PROGRESS,
        COMPLETED,
        ARCHIVED, //for admin only
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }
}
