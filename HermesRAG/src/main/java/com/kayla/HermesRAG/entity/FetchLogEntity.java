package com.kayla.HermesRAG.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "fetch_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_fetched_date")
    private LocalDate lastFetchedDate;

}
