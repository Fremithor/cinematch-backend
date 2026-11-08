package co.edu.cinematch.core.repository;

import co.edu.cinematch.core.model.Identifiable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/** Adapta MongoDB al contrato de repositorio que ya usa el dominio. */
public class MongoRepositoryAdapter<T extends Identifiable> implements Repository<T> {
    private final MongoTemplate mongo;
    private final Class<T> type;
    private final String collection;
    public MongoRepositoryAdapter(MongoTemplate mongo, Class<T> type, String collection) { this.mongo = mongo; this.type = type; this.collection = collection; }
    @Override public T save(T entity) { return mongo.save(entity, collection); }
    @Override public Optional<T> findById(String id) { return Optional.ofNullable(mongo.findById(id, type, collection)); }
    @Override public List<T> findAll() { return mongo.findAll(type, collection); }
    @Override public boolean deleteById(String id) { return mongo.remove(Query.query(Criteria.where("_id").is(id)), type, collection).getDeletedCount() > 0; }
}
