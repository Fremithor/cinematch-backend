package co.edu.cinematch.core.repository;
import co.edu.cinematch.core.model.Identifiable; import java.util.*;
public interface Repository<T extends Identifiable> { T save(T entity); Optional<T> findById(String id); List<T> findAll(); boolean deleteById(String id); }
