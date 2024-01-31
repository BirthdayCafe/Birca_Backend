package com.birca.bircabackend.query.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicBooleanBuilder {

    private final BooleanBuilder booleanBuilder = new BooleanBuilder();

    public static DynamicBooleanBuilder builder() {
        return new DynamicBooleanBuilder();
    }

    public DynamicBooleanBuilder and(Supplier<BooleanExpression> expressionSupplier) {
        try {
            booleanBuilder.and(expressionSupplier.get());
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        return this;
    }

    public DynamicBooleanBuilder or(Supplier<BooleanExpression> expressionSupplier) {
        try {
            booleanBuilder.or(expressionSupplier.get());
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        return this;
    }

    public BooleanBuilder build() {
        return booleanBuilder;
    }
}
