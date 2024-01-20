package ua.kiev.prog;

import org.springframework.data.jpa.repository.JpaRepository;

// DAO
public interface GroupRepository extends JpaRepository<Group, Long> {
}
