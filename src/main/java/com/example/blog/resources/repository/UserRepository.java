package com.example.blog.resources.repository;
import com.example.blog.resources.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

//    Die Methode findById(Long id) ist bereits in CrudRepository oder JpaRepository enthalten
//    @Query("SELECT u FROM User u WHERE u.id = :id")
//    User findById(@Param("id") Long id);

}
