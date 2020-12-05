package ua.ugolek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.stream.Stream;

@NoRepositoryBean
public interface BaseEntityRepository<T> extends JpaRepository<T, Long>
{
    @Query("SELECT t from #{#entityName} t")
    Stream<T> findAllAsStream();
}
