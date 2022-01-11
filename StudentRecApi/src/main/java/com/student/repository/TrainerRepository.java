package com.student.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.entity.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Long>{	
	@Query(value = "select case when count(t)> 0 then true else false end from Trainer t where t.id=:id")
	boolean isExistTrainer(@Param("id") Long id);
	
	@Query(nativeQuery =true,value = "select count(*) from trainer")
	Long getTotalRecord();
	
	@Query(value = "select count(t) from Trainer t  where t.trainerId like %:trainerId% or t.trainerName like %:trainerName%")
	Long getTotalRecordWithFilter(@Param("trainerId") String trainerId, @Param("trainerName") String trainerNam);
	
	@Query(value = "select t from Trainer t where t.trainerId like %:trainerId% or t.trainerName like %:trainerName%")
	List<Trainer> getTrainerByPager(@Param("trainerId") String trainerId, @Param("trainerName") String trainerName,  Pageable page);
	
	@Query(value = "select case when count(t)> 0 then true else false end from Trainer t where t.trainerId =:trainerId")
	boolean isExistTrainerId(@Param("trainerId") String trainerId);
	
	@Query(value = "select case when count(t)> 0 then true else false end from Trainer t where t.trainerId =:trainerId and t.id !=:id")
	boolean isExistTrainerIdById(@Param("trainerId") String trainerId, @Param("id") Long id);
	
	
	Optional<Trainer> findByTrainerId(String trainerId);
	
}
