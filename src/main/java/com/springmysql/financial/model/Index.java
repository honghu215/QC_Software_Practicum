package com.springmysql.financial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


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

    @Column(name = "indexValue")
    private Double indexValue;

    @Column(name = "index_full_name")
    @NotEmpty(message = "*Please provide the full name of the Index")
    private String indexFullName;

    public Index(){}

    public Index(@NotEmpty(message = "*Please provide the name of the index")String indexName, Double indexValue,@NotEmpty(message = "*Please provide the name of the index")String indexFullName)
    {
        this.indexName = indexName;
        this.indexValue = indexValue;
        this.indexFullName = indexFullName;
    }

    public int getIndexId() {
        return indexId;
    }

    public String getIndexName() {
        return indexName;
    }

    public Double getIndexValue() {
        return indexValue;
    }

    public String getIndexFullName() {
        return indexFullName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public void setIndexFullName(String indexFullName) {
        this.indexFullName = indexFullName;
    }

    public void setIndexValue(Double indexValue) {
        this.indexValue = indexValue;
    }

    @Override
    public  String toString()
    {
        return "Index{" +
                "indexId=" + indexId +
                ", indexName='" + indexName + '\'' +
                ", indexValue=" + indexValue +
                ", indexFullName='" + indexFullName + '\'' +
                '}';
    }

}