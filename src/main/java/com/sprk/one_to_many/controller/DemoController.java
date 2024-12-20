package com.sprk.one_to_many.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sprk.one_to_many.entity.Course;
import com.sprk.one_to_many.entity.Instructor;
import com.sprk.one_to_many.entity.InstructorDetail;
import com.sprk.one_to_many.repository.AppDao;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class DemoController {

    private final AppDao appDao;

    // Post Mapping to add instructor and InstructorDetail
    @PostMapping("/save")
    public Instructor saveInstructor(@RequestBody Instructor instructor) {

        if(instructor.getCourses() != null){
            List<Course> courses = instructor.getCourses();

            for(Course course : courses){
                course.setInstructor(instructor);
            }
        }else{
            instructor.setCourses(null);
        }
       
        return appDao.saveInstructor(instructor);
    }

    @PostMapping("/addDetail")
        public Instructor saveInstructorDetail(@RequestBody InstructorDetail instructorDetail){
            Instructor instructor = instructorDetail.getInstructor();
            instructor.setInstructorDetail(instructorDetail);

            return appDao.saveInstructor(instructor);
    }

    /*@PostMapping("/saveInstructor")
    public Instructor savInstructor(@RequestBody InstructorDetail instructorDetail){
        
    }*/

    // Get Mapping to get instructor by id
    @GetMapping("/showInstructors/{id}")
    public Instructor getInstructorById(@PathVariable int id) {

        Instructor instructor = appDao.findByInstructorId(id);

        System.out.println(instructor);

        // System.out.println("Courses:- "+instructor.getCourses());

        return instructor;
    }

    // Get InstructorDetail by Id
    @GetMapping("/getInstructorDetail/{id}")
    public InstructorDetail getInstructorDetailsById(@PathVariable int id){
        return appDao.findInstructorDetailById(id);
    }

    // Get mapping to get list of all instructors
    @GetMapping("/instructors")
    public List<Instructor> getAllInstructors() {

        List<Instructor> instructors = appDao.getAllInstructors();

        return instructors;

    }

    // Get Mapping to fetch instructor using join fetch 
    @GetMapping("/getInstructorJoinFetch/{id}")
    public Instructor getInstructorJoinFetch(@PathVariable int id){

        Instructor instructor = appDao.findByInstructorJoinFetch(id);
        return instructor;
    }

    // Get mapping to fetch all the courses 
    @GetMapping("/getCourses")
    public List<Course> getAllCourses(){

        return appDao.getAllCourses();
    }

    // delete mapping for Instructor 
    @DeleteMapping("/instructor/{id}")
    public String deleteInstructorById(@PathVariable int id) {

        String message = appDao.deleteInstructorById(id);

        return message;

    }

    // Delete mapping for InstructorDetail
    @DeleteMapping("/instructorDetailDelete/{id}")
    public String deleteInstructorDetailById(@PathVariable int id){

        String message = appDao.deleteInstructorDetailById(id);

        return message;
    }

    // update mapping to change the details
    @PutMapping("/updateInstructor/{id}")
    public Instructor updateByInstructorId(@PathVariable int id, @RequestBody Instructor instructor){
        Instructor savedInstructor = appDao.findByInstructorId(id);

        if(savedInstructor != null){
            InstructorDetail updateInstructorDetail = instructor.getInstructorDetail();

            updateInstructorDetail.setInstructorDetailId(savedInstructor.getInstructorDetail().getInstructorDetailId());

            instructor.setInstructorId(savedInstructor.getInstructorId());
            instructor.setInstructorDetail(updateInstructorDetail);

            return appDao.saveInstructor(instructor);
        }else{
            return null;
        }
    }

    // update mapping for instructor details which is deleted
    @PutMapping("/updateInstructorDetail/{id}")
    public Instructor updateInstructorDetails(@RequestBody InstructorDetail instructorDetail,@PathVariable int id){

        return appDao.updateInstructorDetails(id, instructorDetail);       
    }

    // Update Courses
    @PutMapping("/updateCourse/{id}")
    public String updateCourses(@RequestBody Course course, @PathVariable int id){
        return appDao.updateCourses(course, id);
    }

    // Update Courses which is deletd
    @PutMapping("/updateInstructorInCourse/{CourseId}/{InstructorId}")
    public String updateDetail(@PathVariable int CourseId, @PathVariable int InstructorId){
        return appDao.updateInstructorInCourse(CourseId, InstructorId);
    }

    // Finding courses  based on instructorId
    @GetMapping("/getCourseById/{id}")
    public List<Course> getCourseByInstructorId(@PathVariable int id){
        List<Course> courses = appDao.findCoursesByInstructorId(id);

        return courses;
    }

    // Put mapping to remove Instructor from Course
    @PutMapping("/deleteInstructorFromCourse/{CourseId}")
    public String deleteInstructorInCourse(@PathVariable int CourseId){
        return appDao.deleteInstructorFromCourse(CourseId);
    }
}