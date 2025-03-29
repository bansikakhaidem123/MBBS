package Application.MBBS.repository;

import Application.MBBS.entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Long> {
    List<Documents> findBydType(String dType);
}