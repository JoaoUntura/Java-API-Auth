package dev.joaountura.auth.auth.DeviceFingerPrint;
import dev.joaountura.auth.user.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeviceFingerPrintRepository extends JpaRepository<DeviceFingerPrint, Long>{

    DeviceFingerPrint findByFingerPrintAndUsers(String fingerPrint, Users users);



}
