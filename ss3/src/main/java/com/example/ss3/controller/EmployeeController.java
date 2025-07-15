package com.example.ss3.controller;

import com.example.ss3.entity.Employee;
import com.example.ss3.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "add-employee";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        try {
            employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("message", "Thêm nhân viên thành công!");
            return "redirect:/employees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm nhân viên!");
            return "redirect:/employees/add";
        }
    }
    @GetMapping("")
    public String getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String phone,
            Model model) {

        Page<Employee> employees;
        if (phone != null && !phone.trim().isEmpty()) {
            employees = employeeService.searchByPhone(phone.trim(), page, size, sortBy, sortDir);
            model.addAttribute("phone", phone);
        } else {
            employees = employeeService.getAllEmployeesPaginated(page, size, sortBy, sortDir);
        }
        model.addAttribute("employees", employees);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employees.getTotalPages());
        model.addAttribute("totalItems", employees.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "employee-list";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            model.addAttribute("employee", employee);
            return "edit-employee";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhân viên!");
            return "redirect:/employees";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute Employee employee,
                                 RedirectAttributes redirectAttributes) {
        try {
            employee.setId(id);
            employeeService.updateEmployee(employee);
            redirectAttributes.addFlashAttribute("message", "Cập nhật nhân viên thành công!");
            return "redirect:/employees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật nhân viên!");
            return "redirect:/employees/edit/" + id;
        }
    }
    @GetMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("message", "Xóa nhân viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa nhân viên!");
        }
        return "redirect:/employees";
    }
}