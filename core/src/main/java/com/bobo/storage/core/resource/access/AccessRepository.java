package com.bobo.storage.core.resource.access;

import com.bobo.semantic.TechnicalID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AccessRepository<T extends TechnicalID<ID>, ID> extends JpaRepository<T, ID> {

}
