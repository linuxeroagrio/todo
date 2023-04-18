package io.linuxeroagrio.todoapi.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@Table(name="todoitems")
@Entity
public class TodoItem extends PanacheEntityBase{

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int id;
    
    @NotBlank(message="Task Name is mandatory and should be provided")
    public String taskname;
    public Boolean status;
    
    @Override
    public String toString() {
        return "TodoItem {id=" + id + ", taskname=" + taskname + ", status=" + status + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,taskname,status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TodoItem other = (TodoItem) obj;
        if (id != other.id)
            return false;
        if (taskname == null) {
            if (other.taskname != null)
                return false;
        } else if (!taskname.equals(other.taskname))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }
}