package org.example.repository;

import org.example.model.Liability;
import java.util.List;

public interface LiabilityDAO {
    void saveLiability(Liability liability);
    void updateLiability(Liability liability);
    void deleteLiability(String liabilityId);
    Liability getLiabilityById(String liabilityId);
    List<Liability> getAllLiabilities();
}
