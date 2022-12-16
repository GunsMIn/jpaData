package com.spring.jpadata.repository.jpadata;

import com.spring.jpadata.dto.MemberSearchCondition;
import com.spring.jpadata.dto.MemberTeamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
//interface로 제작
public interface MemberRepositoryCustom { // 사용자 정의 인터페이스

    List<MemberTeamDto> search(MemberSearchCondition condition);
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition,Pageable pageable);

}
