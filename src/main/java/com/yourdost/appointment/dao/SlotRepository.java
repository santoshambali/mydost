package com.yourdost.appointment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yourdost.appointment.model.Slot;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long>{
	List<Slot> findAllByExpertId(String str);
}
