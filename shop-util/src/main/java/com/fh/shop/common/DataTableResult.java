package com.fh.shop.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTableResult implements Serializable {

    private Long draw;

    private Long recordsTotal;

    private Long recordsFiltered;

    private List data;

}
