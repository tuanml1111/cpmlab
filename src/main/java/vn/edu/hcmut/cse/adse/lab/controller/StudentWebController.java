package vn.edu.hcmut.cse.adse.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmut.cse.adse.lab.entity.Student;
import vn.edu.hcmut.cse.adse.lab.service.StudentService;

import java.util.List;

@Controller
public class StudentWebController {

    private static final String STUDENT_ATTR = "student";
    private static final String REDIRECT_LIST = "redirect:/students";

    private final StudentService service;

    public StudentWebController(StudentService service) {
        this.service = service;
    }

    // GET / - Redirect ve trang danh sach
    @GetMapping("/")
    public String root() {
        return REDIRECT_LIST;
    }

    // GET /students - Trang danh sach co tim kiem
    @GetMapping("/students")
    public String listStudents(@RequestParam(required = false) String keyword, Model model) {
        List<Student> students;
        if (keyword != null && !keyword.isEmpty()) {
            students = service.searchByName(keyword);
        } else {
            students = service.getAll();
        }
        model.addAttribute("dsSinhVien", students);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "students";
    }

    // GET /students/new - Hien thi form them moi
    @GetMapping("/students/new")
    public String showCreateForm(Model model) {
        model.addAttribute(STUDENT_ATTR, new Student());
        model.addAttribute("formAction", "/students");
        model.addAttribute("pageTitle", "Them Sinh Vien Moi");
        return "student-form";
    }

    // POST /students - Luu sinh vien moi
    @PostMapping("/students")
    public String createStudent(@ModelAttribute Student student) {
        service.save(student);
        return REDIRECT_LIST;
    }

    // GET /students/{id} - Trang chi tiet sinh vien
    @GetMapping("/students/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Student student = service.getById(id);
        if (student == null) {
            return REDIRECT_LIST;
        }
        model.addAttribute(STUDENT_ATTR, student);
        return "student-detail";
    }

    // GET /students/{id}/edit - Hien thi form chinh sua
    @GetMapping("/students/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = service.getById(id);
        if (student == null) {
            return REDIRECT_LIST;
        }
        model.addAttribute(STUDENT_ATTR, student);
        model.addAttribute("formAction", "/students/" + id + "/edit");
        model.addAttribute("pageTitle", "Chinh Sua Sinh Vien");
        return "student-form";
    }

    // POST /students/{id}/edit - Luu cap nhat sinh vien
    @PostMapping("/students/{id}/edit")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
        student.setId(id);
        service.save(student);
        return "redirect:/students/" + id;
    }

    // POST /students/{id}/delete - Xoa sinh vien
    @PostMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {
        service.deleteById(id);
        return REDIRECT_LIST;
    }
}
