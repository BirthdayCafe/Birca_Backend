package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.query.dto.CafeParams;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.query.repository.model.CafeView;

import java.util.List;

public interface CafeDynamicRepository {

    List<CafeView> searchCafes(LoginMember loginMember, CafeParams cafeParams, PagingParams pagingParams);
}
