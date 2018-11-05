package com.springmysql.financial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_index")
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "index_id")
    private int indexId;

    @Column(name = "index_name")
    @NotEmpty(message = "*Please provide the name of the Index")
    private String indexName;

    @Column(name = "index_full_name")
    @NotEmpty(message = "*Please provide the full name of the Index")
    private String indexFullName;
}