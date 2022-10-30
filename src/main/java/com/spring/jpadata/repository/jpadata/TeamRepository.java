package com.spring.jpadata.repository.jpadata;

import com.spring.jpadata.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository  extends JpaRepository<Team,Long> {
                                        //<타입,pk매팽타입>
}
