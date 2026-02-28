package vn.edu.hcmut.cse.adse.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmut.cse.adse.lab.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Tim kiem theo ten, khong phan biet hoa thuong
    List<Student> findByNameContainingIgnoreCase(String name);
}
