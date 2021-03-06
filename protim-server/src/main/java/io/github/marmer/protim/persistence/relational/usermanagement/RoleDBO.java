package io.github.marmer.protim.persistence.relational.usermanagement;

import io.github.marmer.protim.api.configuration.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"id", "version"})
public class RoleDBO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    @Enumerated(EnumType.STRING)
    private Role name;
    @Version
    private Long version;
}
