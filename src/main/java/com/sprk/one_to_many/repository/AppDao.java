
package com.sprk.one_to_many.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sprk.one_to_many.entity.Course;
import com.sprk.one_to_many.entity.Instructor;
import com.sprk.one_to_many.entity.InstructorDetail;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class AppDao {

    // @PersistenceContext
    private final EntityManager entityManager;

    // Save Mapping to add instructor
    @Transactional
    public Instructor saveInstructor(Instructor instructor) {
        return entityManager.merge(instructor);
    }

    // Save mapping to add instructorDetail
    @Transactional
    public InstructorDetail saveInstructorDetails(InstructorDetail instructorDetail) {
        return entityManager.merge(instructorDetail);
    }

    // Get mapping to fetch Instructor by Id
    @Transactional
    public Instructor findByInstructorId(int id) {
        // TODO Auto-generated method stub
        return entityManager.find(Instructor.class, id);
    }

    // Get Mapping to fetch InstructorDetail by Id
    public InstructorDetail findInstructorDetailById(int id) {
        return entityManager.find(InstructorDetail.class, id);
    }

    // Get mapping to get fetch List of Instructors
    @Transactional
    public List<Instructor> getAllInstructors() {
        return entityManager.createQuery("select i from Instructor i", Instructor.class).getResultList();
    }

    // Delete mapping to remove instructor by Id
    @Transactional
    public String deleteInstructorById(int id) {

        Instructor instructor = findByInstructorId(id);

        if (instructor != null) {

            List<Course> courses = instructor.getCourses();
            for (Course course : courses) {
                course.setInstructor(null);
            }
            entityManager.remove(instructor);
            return "Deleted Successfully";
        } else {
            return "Something went wrong";
        }
    }

    // Delete mapping for InstructorDetail
    @Transactional
    public String deleteInstructorDetailById(int id) {

        InstructorDetail instructorDetail = entityManager.find(InstructorDetail.class, id);

        if (instructorDetail != null) {
            Instructor instructor = instructorDetail.getInstructor();
            instructor.setInstructorDetail(null);
            entityManager.remove(instructorDetail);
            return "Delete Successfully";
        } else {
            return " Something went wrong";
        }

    }

    // Put Mapping to update info of Instructor and InstructorDetail by Id
    @Transactional
    public Instructor updateInstructorById(Instructor instructor) {
        return entityManager.merge(instructor);
    }

    @Transactional
    public InstructorDetail updateInstructorDetails(InstructorDetail instructorDetail) {
        return entityManager.merge(instructorDetail);
    }

    @Transactional
    public Instructor updateInstructorDetails(int id, InstructorDetail instructorDetail) {
        Instructor instructor = entityManager.find(Instructor.class, id);

        if (instructor != null) {
            instructor.setInstructorDetail(instructorDetail);
            return entityManager.merge(instructor);
        } else {
            return null;
        }
    }

    // Get Mapping for get List of courses by InstructorId
    public List<Course> findCoursesByInstructorId(int id) {

        TypedQuery<Course> query = entityManager.createQuery("from Course where instructor.id = :data", Course.class);

        query.setParameter("data", id);

        List<Course> courses = query.getResultList();

        return courses;
    }

    // Get mapping to get details of Instructor using jpql
    public Instructor findByInstructorJoinFetch(int id) {

        TypedQuery<Instructor> query = entityManager.createQuery(
                "from Instructor i join fetch i.courses join fetch i.instructorDetail where i.id = :data",
                Instructor.class);

        query.setParameter("data", id);

        Instructor instructor = query.getSingleResult();

        return instructor;
    }

    // Get All courses
    public List<Course> getAllCourses() {

        return entityManager.createQuery("from Course", Course.class).getResultList();
    }

    // Put mapping to update Courses
    @Transactional
    public String updateCourses(Course course, int id) {

        Course dbCourse = entityManager.find(Course.class, id);

        if (dbCourse != null) {
            course.setCourseId(id);

            entityManager.merge(course);
            return "updated Successfully...";
        }

        return "Course with ID: " + id + " not found...";

    }

    // Update mapping to update InstructorDetail
    @Transactional
    public String updateInstructorInCourse(int courseId, int instructorId) {

        Course dbCourse = entityManager.find(Course.class, courseId);
        Instructor instructor = findByInstructorId(instructorId);

        if (dbCourse != null && instructor != null) {

            dbCourse.setInstructor(instructor);
            entityManager.merge(dbCourse);

            return "update Successful...";
        } else if (dbCourse == null) {
            return "Course with Id" + courseId + " not found!";
        } else {
            return "Instructor with Id" + instructorId + " not found!";
        }
    }

    @Transactional
    public String deleteInstructorFromCourse(int CourseId) {
        Course dbCourse = entityManager.find(Course.class, CourseId);
        // Instructor instructor = findByInstructorId(instructorId);

        if (dbCourse != null) {
            dbCourse.setInstructor(null);
            entityManager.merge(dbCourse);

            return "Instructor Removed Successfully....";
        } else {
            return "Course with Id" + CourseId + " not found!";
        }
    }

}
