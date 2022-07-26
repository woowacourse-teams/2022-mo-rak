package com.morak.back.support;

import com.morak.back.poll.support.DomainSupplier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(DomainSupplier.class)
@DataJpaTest
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
public @interface RepositoryTest {
}
