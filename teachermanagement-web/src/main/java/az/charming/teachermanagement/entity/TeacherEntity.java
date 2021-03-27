package az.charming.teachermanagement.entity;

import javax.persistence.*;
import java.util.List;

@Entity(name="teacher")
public class TeacherEntity {

    @Id
    private Integer id;
    private String name;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REMOVE
    })
    @JoinTable(name = "student_teacher",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<StudentEntity> studentList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudentEntity> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentEntity> studentList) {
        this.studentList = studentList;
    }

    @Override
    public String toString() {
        return "TeacherEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
