package az.charming.teachermanagement.entity;

import javax.persistence.*;
import java.util.List;

@Entity(name="school")
public class SchoolEntity {

    @Id
    private Integer id;
    private String name;

    @OneToMany
    private List<StudentEntity> studentList;

    @OneToOne(mappedBy = "school", cascade = CascadeType.ALL)
    private SchoolAddress schoolAddress;

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
        return "SchoolEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
