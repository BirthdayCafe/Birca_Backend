package com.birca.bircabackend.command.like.domain;

import com.birca.bircabackend.common.domain.BaseEntity;

public interface LikeValidator {

    void validate(LikeTarget target);
}
