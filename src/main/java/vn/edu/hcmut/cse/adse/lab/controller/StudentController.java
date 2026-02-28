package vn.edu.hcmut.cse.adse.lab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmut.cse.adse.lab.entity.Student;
import vn.edu.hcmut.cse.adse.lab.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // API Lay danh sach: GET http://localhost:8080/api/students
    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAll();
    }

    // API Lay chi tiet: GET http://localhost:8080/api/students/{id}
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return service.getById(id);
    }
}
