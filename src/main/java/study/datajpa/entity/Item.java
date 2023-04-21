package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Getter
@Entity
@NoArgsConstructor
public class Item extends BaseEntity implements Persistable<String> {

    // No use @GeneratedValue
    @Id
    private String id;

    public Item(String id) {
        this.id = id;
    }

    // isNew() is called to check if this object is new in repository.save()
    // If using @GeneratedValue, isNew() check conditions that id is null, zero, or Persistable.
    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
