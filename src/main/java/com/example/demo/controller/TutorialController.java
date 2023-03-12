package com.example.demo.controller;

import com.example.demo.dto.TutorialDTO;
import com.example.demo.entity.Tutorial;
import com.example.demo.mapper.TutorialMapper;
import com.example.demo.repository.TutorialRepository;
import com.example.demo.util.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Controller
public class TutorialController {

    private final TutorialRepository tutorialRepository;

    private final TutorialMapper tutorialMapper;

    private final PageUtil pageUtil;

    public TutorialController(TutorialRepository tutorialRepository, TutorialMapper tutorialMapper,
                              PageUtil pageUtil) {
        this.tutorialRepository = tutorialRepository;
        this.tutorialMapper = tutorialMapper;
        this.pageUtil = pageUtil;
    }

    @GetMapping("/tutorials")
    public String getAll(Model model, @Param("keyword") String keyword,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "5") int size) {
        try {
            int currentPage = pageUtil.getCurrentPage(page);
            int pageSize = pageUtil.getCurrentPageSize(size);
            Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
            Page<TutorialDTO> tutorialPage = null;
            if (StringUtils.isEmptyOrWhitespace(keyword)) {
                tutorialPage = tutorialRepository.findAll(pageable).map(tutorialMapper::mapToTutorialDTO);
            } else {
                tutorialPage = tutorialRepository.findAllByTitleContainingIgnoreCase(keyword, pageable).map(tutorialMapper::mapToTutorialDTO);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("tutorialPage", tutorialPage);

            int totalPages = tutorialPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = pageUtil.getPageNumbers(totalPages);
                model.addAttribute("pageNumbers", pageNumbers);
            }
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "tutorials";
    }

    @GetMapping("/tutorials/new")
    public String addTutorial(Model model) {
        Tutorial tutorial = new Tutorial();
        tutorial.setPublished(true);

        model.addAttribute("tutorial", tutorial);
        model.addAttribute("pageTitle", "Create new Tutorial");

        return "tutorial_form";
    }

    @PostMapping("/tutorials/save")
    public String saveTutorial(Tutorial tutorial, RedirectAttributes redirectAttributes) {
        try {
            tutorialRepository.save(tutorial);

            redirectAttributes.addFlashAttribute("message", "The Tutorial has been saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
        }

        return "redirect:/tutorials";
    }

    @GetMapping("/tutorials/{id}")
    public String editTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Tutorial tutorial = tutorialRepository.findById(id).get();

            model.addAttribute("tutorial", tutorial);
            model.addAttribute("pageTitle", "Edit Tutorial (ID: " + id + ")");

            return "tutorial_form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/tutorials";
        }
    }

    @GetMapping("/tutorials/delete/{id}")
    public String deleteTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            tutorialRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The Tutorial with id=" + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/tutorials";
    }

    @GetMapping("/tutorials/{id}/published/{status}")
    public String updateTutorialPublishedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean published,
                                                Model model, RedirectAttributes redirectAttributes) {
        try {
            tutorialRepository.updatePublishedStatus(id, published);

            String status = published ? "published" : "disabled";
            String message = "The Tutorial id=" + id + " has been " + status;

            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/tutorials";
    }
}
