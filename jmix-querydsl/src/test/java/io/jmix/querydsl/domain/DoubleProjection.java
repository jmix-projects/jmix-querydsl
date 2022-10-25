package io.jmix.querydsl.domain;

import com.querydsl.core.annotations.QueryProjection;

public class DoubleProjection {

    public Double val;

    @QueryProjection
    public DoubleProjection(Double val) {
        this.val = val;
    }

}
