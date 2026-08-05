package org.example.confidentialite.Repository;

import org.example.confidentialite.Entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepo extends JpaRepository<Personnel,String> {
    @Query("SELECT p FROM Personnel p WHERE p.IdPersonnel = :idPersonnel AND p.Department = :department")
    Optional<Personnel> findByIdInDep(
            @Param("idPersonnel") String idPersonnel,
            @Param("department") String department
    );

    @Query("SELECT DISTINCT p.Department FROM Personnel p WHERE p.Department <> 'labo'   ")
    List<String> findAllDepartement();

};